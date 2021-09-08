package com.study.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Give {

    private Integer id;
    private Integer blogId;
    private Integer userId;
    private Integer blogUserId;
    private Date giveDate;
    private Integer state;

}
