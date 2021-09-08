package com.study.pojo;

import lombok.Data;

import java.util.Date;

/*子评论的数据库对象*/
@Data
public class SonComment {

    private Integer id;
    private Integer blogId;
    private Integer commentId;
    private Integer userId;
    private Integer commentUserId;
    private String content;
    private Date commentDate;
    private Integer give;
    private Integer state;
}
