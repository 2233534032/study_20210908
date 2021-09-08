package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MessageCommentData {

    private Integer id;
    /*当前用户被评论的id(面向自己发起获取点赞信息这个就是自己的id)*/
    private Integer blogUserId;
    /*博客id*/
    private Integer blogId;
    /*点赞状态 0表示未读 1表示已读*/
    private Integer state;
    /*表示被评论的博客的图片*/
    private String img;
    /*表示评论人的id*/
    private Integer userId;
    /*被评论的博客内容*/
    private String content;
    /*被评论人的nice*/
    private String username;
    /*评论的头像*/
    private String commentUserHead;
    /*评论人的昵称*/
    private String commentUsername;
    /*评论时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;
    /*评论内容*/
    private  String commentContent;

}
