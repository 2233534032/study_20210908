package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*展示到页面的子评论的回复评论信息*/
@Data
public class ReplySonCommentPage {

    /*回复评论的主键*/
    private Integer id;
    /*被回复评论的id*/
    private Integer commentId;
    /*回复评论的人的id*/
    private Integer userId;
    /*回复人的username*/
    private String replyUserName;
    /*被回复的评论的id*/
    private Integer commentUserId;
    /*被回复的评论人的username*/
    private String commentUserName;
    /*被回复的评论的内容*/
    private String content;
    /*回复的内容*/
    private String replyContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;
    /*点赞次数*/
    private Integer give;


}
