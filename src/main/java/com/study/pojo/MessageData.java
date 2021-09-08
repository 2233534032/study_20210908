package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class MessageData {

    private Integer id;
    /*当前用户被点赞的id(面向自己发起获取点赞信息这个就是自己的id)*/
    private Integer blogUserId;
    /*博客id*/
    private Integer blogId;
    /*点赞状态 0表示未读 1表示已读*/
    private Integer state;
    /*表示被点赞的博客的图片*/
    private String img;
    /*表示点赞人的id*/
    private Integer userId;
    /*被点赞的博客内容*/
    private String content;
    /*被点赞人的nice*/
    private String username;
    /*点赞人的头像*/
    private String giveUserHead;
    /*点赞人的昵称*/
    private String giveUsername;
    /*点赞时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date giveDate;

}
