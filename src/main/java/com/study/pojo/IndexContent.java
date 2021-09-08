package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*首页正文内容*/
@Data
public class IndexContent {

    /*发表文章的用户id*/
    private Integer userId;
    /*发表文章的用户用户昵称*/
    private String username;
    /*发表文章用户头像*/
    private String userHead;
    /*博客的id*/
    private Integer blogId;
    /*正文插图*/
    private String contentImg;
    /*正文*/
    private String mainBody;
    /*发表时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date newDate;
    /*当前登录用户的头像*/
    private String loginUserHead;
    /*总评论数*/
    private Integer commentCount;
    /*总点赞数*/
    private Integer giveCount;
    /*总转发数*/
    private Integer transmitCount;

    private List<String> imgs = new ArrayList<>();

    private String type;
/*图片的数量*/
    private Integer imgCount;

    /*一页查询的数量*/
    private Integer pageSize = 5;

    /*当前页，默认是第0页*/
    private Integer page=0;



}
