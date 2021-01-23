package com.boyue.search.dto;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/22 16:38
 * @Author: Jacky
 * @Description: 接收查询参数对象
 */
public class SearchRequest {
    /**
     * 搜索关键词
     */
    private String  key;
    /**
     * 当前页码
     */
    private Integer page;

    private Map<String,String> filter;

    /**
     * 每页大小，不从页面接收，而是固定大小
     */
    private static final Integer DEFAULT_SIZE = 20;
    /**
     * 默认页
     */
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }
}
