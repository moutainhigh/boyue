package com.boyue.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/17 21:02
 * @Author: Jacky
 * @Description: 分页的vo对象，封装分页查询的结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 查询你结果的list集合
     */
    private List<T> items;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long totalPage;
}
