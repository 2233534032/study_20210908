package com.study.pojo;


/*评论区的实体类*/

import lombok.Data;

@Data
public class Comment {

    private Integer id;
    private Integer userId;
    private String username;
    private String userHead;
    private Integer blogUserId;
    private String comment;
    private String commentDate;
    private String give;
}
