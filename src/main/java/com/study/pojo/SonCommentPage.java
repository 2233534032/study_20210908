package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*前端展示子评论的对象*/
@Data
public class SonCommentPage {

    private Integer id;
    private Integer commentId;
    private Integer userId;
    private Integer userCommentId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;
    private Integer give;
    /*子评论的评论人的用户名*/
    private String sonCommentUsername;

}
