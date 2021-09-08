package com.study.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*数据库实例*/
@Data
public class ReplySonCommentDate {
    private Integer id;
    private Integer blogId;
    private Integer commentId;
    private Integer sonCommentId;
    private Integer userId;
    private Integer  sonCommentUserId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;
    private Integer give;
    private Integer state;

}
