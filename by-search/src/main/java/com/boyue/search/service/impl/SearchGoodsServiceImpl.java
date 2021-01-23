package com.boyue.search.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.utils.JsonUtils;
import com.boyue.common.vo.PageResult;
import com.boyue.item.client.*;
import com.boyue.item.dto.*;
import com.boyue.search.dto.GoodsDTO;
import com.boyue.search.dto.SearchRequest;
import com.boyue.search.entity.Goods;
import com.boyue.search.repository.GoodsRepository;
import com.boyue.search.service.SearchGoodsService;
import com.boyue.search.utils.HandlerGoodsUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 19:04
 * @Author: Jacky
 * @Description: 搜索的service层
 */
@Service
public class SearchGoodsServiceImpl implements SearchGoodsService {

    /**
     * sku的feignClient接口
     */
    @Autowired
    private SkuClient skuClient;

    /**
     * specParamClient的feignClient接口
     */
    @Autowired
    private SpecParamClient specParamClient;

    /**
     * spuDetailClient的feignClient接口
     */
    @Autowired
    private SpuDetailClient spuDetailClient;

    /**
     * CategoryClient的feignClient接口
     */
    @Autowired
    private CategoryClient categoryClient;

    /**
     * BrandClient的feignClient接口
     */
    @Autowired
    private BrandClient brandClient;

    /**
     * 注入elasticsearchTemplate
     */
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 注入spu的feign接口
     */
    @Autowired
    private SpuClient spuClient;

    /**
     * 注入es的crud对象
     */
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 构造goods对象
     * 将spuDTO对象转换为Goods对象
     *
     * @param spuDTO 抽象的商品对象
     * @return goods对象
     */
    @Override
    public Goods createGoods(SpuDTO spuDTO) {
        //判断参数是否有效
        if (spuDTO == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //1.获取spuId
        Long spuId = spuDTO.getId();

        //2.获取全部检索字段的字符串， 商品名、品牌、分类、规格信息
        //2.1 获取全部商品名
        String name = spuDTO.getName();
        String brandName = spuDTO.getBrandName();
        String categoryName = spuDTO.getCategoryName();
        String all = name + brandName + categoryName;

        //获取品牌id
        Long brandId = spuDTO.getBrandId();

        //获取分类id
        Long categoryId = spuDTO.getCid3();

        //获取创建时间的毫秒值
        long createTime = spuDTO.getCreateTime().getTime();

        //获取促销信息
        String subTitle = spuDTO.getSubTitle();

        //获取sku ---> id、image、price、title
        List<SkuDTO> skuDTOList = skuClient.findSkuBySpuId(spuId);
        //收集价格为set集合
        Set<Long> priceSet = skuDTOList.stream().map(SkuDTO::getPrice).collect(Collectors.toSet());

        //封装sku
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (SkuDTO skuDTO : skuDTOList) {
            Map<String, Object> map = new HashMap<>();
            //设置skuId
            map.put("id", skuDTO.getId());
            //设置image
            map.put("image", StringUtils.substringBefore(skuDTO.getImages(), ","));
            //设置price
            map.put("price", skuDTO.getPrice());
            //设置title
            map.put("title", skuDTO.getTitle());
            //添加到list集合中
            skuList.add(map);
        }
        //转换为json对象
        String skus = JsonUtils.toString(skuList);

        //获取spec规格参数对象
        List<SpecParamDTO> specParamDTOList = specParamClient.findSpecParam(null, categoryId, true);
        //查询出详细的规格参数
        SpuDetailDTO spuDetailDTO = spuDetailClient.findSpuDetailBySpuId(spuId);

        //将通用规格参数转换为map对象
        String genericSpec = spuDetailDTO.getGenericSpec();
        Map<Long, Object> genericSpecMap = JsonUtils.toMap(genericSpec, Long.class, Object.class);

        //将私有的规格参数转换为map对象
        String specialSpec = spuDetailDTO.getSpecialSpec();
        Map<Long, List<String>> specialSpecMap = JsonUtils.nativeRead(specialSpec, new TypeReference<Map<Long, List<String>>>() {
        });

        //创建map集合存储规格参数
        Map<String, Object> specMap = new HashMap<>();
        for (SpecParamDTO specParamDTO : specParamDTOList) {
            //过去specParam的id
            Long specParamId = specParamDTO.getId();
            //获取参数的name
            String specParamName = specParamDTO.getName();
            //获取该规格参数的值
            Object specValue = null;
            if (specParamDTO.getGeneric()) {
                //通用规格参数
                assert genericSpecMap != null;
                specValue = genericSpecMap.get(specParamId);
            } else {
                //私有规格参数
                assert specialSpecMap != null;
                specValue = specialSpecMap.get(specParamId);
            }
            if (specParamDTO.getIsNumeric()) {
                specValue = HandlerGoodsUtils.chooseSegment(specValue, specParamDTO);
            }
            specMap.put(specParamName, specValue);
        }

        //获取私有属性值的字符串作为搜索条件
        assert specialSpecMap != null;
        Set<Long> specialSpecMapKeySet = specialSpecMap.keySet();
        //构建stringBuilder存储集合
        StringBuilder stringBuilder = new StringBuilder();
        for (Long key : specialSpecMapKeySet) {
            List<String> list = specialSpecMap.get(key);
            stringBuilder.append(list.toString());
        }

        //添加到all中
        all += stringBuilder.toString();

        //构建goods对象
        Goods goods = new Goods();
        //设置唯一标识id，就是spiDTO的id
        goods.setId(spuId);
        //设置促销标题
        goods.setSubTitle(subTitle);
        //设置全部检索字段，名，品牌，分类
        goods.setAll(all);
        //设置品牌id
        goods.setBrandId(brandId);
        //设置分类id
        goods.setCategoryId(categoryId);
        //设置创建时间的毫秒值
        goods.setCreateTime(createTime);
        //设置价格区间
        goods.setPrice(priceSet);
        //设置skuId,图片，价格，标题
        goods.setSkus(skus);//null
        //设置规格参数
        goods.setSpecs(specMap);//null

        return goods;
    }

    /**
     * 商城关键词搜索,key查询
     * POST /page
     *
     * @param searchRequest 请求参数
     * @return 分页查询结果
     */
    @Override
    public PageResult<GoodsDTO> findSearchByKey(SearchRequest searchRequest) {
        //获取搜索条件
        String key = searchRequest.getKey();
        //判断参数是否有效
        if (StringUtils.isBlank(key)) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //构造原生查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //设置过滤条件,添加源过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        //设置查询条件
        queryBuilder.withQuery(basicQuery(searchRequest));

        //构建分页查询条件
        int page = searchRequest.getPage() - 1;
        Integer size = searchRequest.getSize();
        PageRequest pageRequest = PageRequest.of(page, size);
        //设置分页查询条件
        queryBuilder.withPageable(pageRequest);

        //发送查询
        AggregatedPage<Goods> goodsAggregatedPage = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //处理查询结果
        List<Goods> goodsList = goodsAggregatedPage.getContent();

        //判断查询结果
        if (CollectionUtils.isEmpty(goodsList)) {
            throw new ByException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //类型转化
        List<GoodsDTO> goodsDTOS = BeanHelper.copyWithCollection(goodsList, GoodsDTO.class);

        if (CollectionUtils.isEmpty(goodsDTOS)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        //获取总条数
        long total = goodsAggregatedPage.getTotalElements();
        Long pages = (long) goodsAggregatedPage.getTotalPages();

        return new PageResult<GoodsDTO>(goodsDTOS, total, pages);
    }

    /**
     * 商城查询过滤条件
     * 请求路径  POST /filter
     *
     * @param searchRequest 请求参数封装的对象  key -- 搜索关键字  page--当前页码
     * @return Map<String, List> 查询的map集合结果集
     */
    @Override
    public Map<String, List<?>> searchGoodsFilter(SearchRequest searchRequest) {
        //获取查询关键词
        String key = searchRequest.getKey();
        //创建封装查询结果的map
        Map<String, List<?>> filterMap = new LinkedHashMap<>();
        //创建原生查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //设置返回字段
        queryBuilder.withSourceFilter(new FetchSourceFilterBuilder().build());
        //设置搜素条件
        queryBuilder.withQuery(basicQuery(searchRequest));
        //设置聚合
        //分类聚合条件
        String categoryAggs = "categoryAggs";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggs).field("categoryId"));
        //设置品牌聚合条件
        String brandAggs = "brandAggs";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggs).field("brandId"));
        //将条件发送到es
        AggregatedPage<Goods> aggregatedPage = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //处理查询结果
        Aggregations aggregations = aggregatedPage.getAggregations();

        //获取分类的结果
        LongTerms categoryTerms = aggregations.get(categoryAggs);
        List<Long> cids = handlerCategory(categoryTerms, filterMap);

        //获取品牌的结果
        LongTerms brandAggsTerms = aggregations.get(brandAggs);
        handlerBrand(brandAggsTerms, filterMap);

        if (!CollectionUtils.isEmpty(cids) && cids.size() == 1) {
            handlerSpecParam(searchRequest, cids.get(0), filterMap);
        }

        return filterMap;
    }

    /**
     * 添加新的goods对象到es
     *
     * @param id 商品id
     */
    @Override
    public void createIndex(Long id) {
        //判断参数
        if (id == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //添加商品到es
        SpuDTO spuDTO = spuClient.findSpuById(id);

        //获取品牌名称
        BrandDTO brandDTO = brandClient.findBrandListById(spuDTO.getBrandId());
        spuDTO.setBrandName(brandDTO.getName());

        //获取分类名称
        List<CategoryDTO> categoryByIds = categoryClient.findCategoryByIds(spuDTO.getIds());
        String categoryName = categoryByIds.stream().map(CategoryDTO::getName).collect(Collectors.joining("/"));
        spuDTO.setCategoryName(categoryName);

        //设置详细参数
        SpuDetailDTO spuDetail = spuDetailClient.findSpuDetailBySpuId(id);
        spuDTO.setSpuDetail(spuDetail);


        Goods goods = this.createGoods(spuDTO);
        //把goods写入到es中
        goodsRepository.save(goods);
    }

    /**
     * 商品下架时删除es中的索引
     *
     * @param id 商品id
     */
    @Override
    public void removeIndex(Long id) {
        //删除商品索引
        goodsRepository.deleteById(id);
    }


    /**
     * 基础查询，构建match 和 filter
     *
     * @param searchRequest 前端传递过来的对象
     * @return 查询条件
     */
    private BoolQueryBuilder basicQuery(SearchRequest searchRequest) {
        //构建组合查询对象
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //设置必须存在的内容
        boolQuery.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));

        //如果存在过滤条件需要添加过滤条件
        if (!CollectionUtils.isEmpty(searchRequest.getFilter())) {
            //获取过滤条件的map集合
            Map<String, String> filterMap = searchRequest.getFilter();
            for (String key : filterMap.keySet()) {
                String fieldName = "specs." + key;
                if ("分类".equals(key)) {
                    //存在分类则
                    fieldName = "categoryId";
                } else if ("品牌".equals(key)) {
                    fieldName = "brandId";
                }
                boolQuery.filter(QueryBuilders.termQuery(fieldName, filterMap.get(key)));
            }
        }
        //返回组合后的查询条件
        return boolQuery;
    }

    /**
     * 通过聚合函数查询的商品属性结果，封装到filterMap中
     *
     * @param searchRequest 请求参数封装的对象  key -- 搜索关键字  page--当前页码
     * @param cid           分类的id
     * @param filterMap     存储对象
     */
    private void handlerSpecParam(SearchRequest searchRequest, Long cid, Map<String, List<?>> filterMap) {
        //获取关键词
        String key = searchRequest.getKey();
        //创建原生查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //设置返回字段
        queryBuilder.withSourceFilter(new FetchSourceFilterBuilder().build());
        //设置搜索条件
        queryBuilder.withQuery(basicQuery(searchRequest));
        //设置分页
        queryBuilder.withPageable(PageRequest.of(0, 1));
        //设置聚合函数
        List<SpecParamDTO> specParamList = specParamClient.findSpecParam(null, cid, true);
        for (SpecParamDTO specParamDTO : specParamList) {
            String specParamDTOName = specParamDTO.getName();
            String fieldName = "specs." + specParamDTOName;
            queryBuilder.addAggregation(AggregationBuilders.terms(specParamDTOName).field(fieldName));
        }
        //将条件发送到es端
        AggregatedPage<Goods> aggregatedPage = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //处理聚合查询结果
        Aggregations aggregations = aggregatedPage.getAggregations();

        for (SpecParamDTO specParamDTO : specParamList) {
            String specParamName = specParamDTO.getName();
            StringTerms stringTerms = aggregations.get(specParamName);
            List<String> specParam = stringTerms.getBuckets()
                    .stream().map(StringTerms.Bucket::getKeyAsString)
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList());
            filterMap.put(specParamName, specParam);
        }

    }

    /**
     * 通过聚合函数查询的品牌结果，查询品牌对象
     *
     * @param brandAggsTerms 聚合函数查询查出来的品牌结果
     * @param filterMap      存储对象
     */
    private void handlerBrand(LongTerms brandAggsTerms, Map<String, List<?>> filterMap) {
        List<LongTerms.Bucket> brandIdList = brandAggsTerms.getBuckets();

        //获取品牌对象的id集合
        List<Long> brandIds = brandIdList.stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());

        //通过brandIds查询品牌对象
        List<BrandDTO> brandDTOList = brandClient.findBrandListByIds(brandIds);
        filterMap.put("品牌", brandDTOList);
    }

    /**
     * 通过获取到的聚合函数查询出的分类结果，查询分类对象
     *
     * @param categoryAggsTerms 聚合函数查询出来的分类结果
     * @param filterMap         存储对象
     * @return 返回结果
     */
    private List<Long> handlerCategory(LongTerms categoryAggsTerms, Map<String, List<?>> filterMap) {
        List<LongTerms.Bucket> buckets = categoryAggsTerms.getBuckets();

        //获取category的id集合
        List<Long> categoryIds = new ArrayList<>();
        for (LongTerms.Bucket bucket : buckets) {
            long key = bucket.getKeyAsNumber().longValue();
            categoryIds.add(key);
        }

        if (CollectionUtils.isEmpty(categoryIds)) {
            return null;
        }
        //查询category对象
        List<CategoryDTO> categoryDTOList = categoryClient.findCategoryByIds(categoryIds);

        //判断获取的值是否为空
        if (CollectionUtils.isEmpty(categoryDTOList)) {
            throw new ByException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        filterMap.put("分类", categoryDTOList);

        return categoryIds;
    }
}
