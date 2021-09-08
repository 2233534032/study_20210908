package com.study.pojo;

import lombok.Data;

import java.util.List;

/*
* 返回到页面的数据
* */
@Data
public class Page <T> {
    /*一次查询的条数*/
    private Integer pageSize = 4;
    /*当前页*/
    private Integer currentPage=1;
    /*总页数*/
    private Integer pageCount;
    /*当前页数据*/
    private List<T>  data;

    /*当前登录用户*/
    private User user;

    private String state;

}
