package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/*博客列表*/
@Data
public class Blog {

    private Integer id;
    private Integer userId;
    /*正文*/
    private String mainBody;
    /*发布时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date newDate;
    /*文章图片*/
    private String contentImg;
    /*总点赞数*/
    private Integer blogGive;

}
