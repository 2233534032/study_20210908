package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*数据库blogComment实体类*/
@Data
public class Comment_entry {

    private Integer id;
    private Integer blogId;
    private Integer userId;
    private Integer blogUserId;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;
    private Integer give;
    private Integer state;

    private String username;
    private String userHead;
}
