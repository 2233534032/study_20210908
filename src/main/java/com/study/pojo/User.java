package com.study.pojo;

import lombok.Data;

import java.sql.Date;


@Data
public class User {

    private Integer id;
    private String username;
    private String  password;
    private String isPassword;
    private String gender;
    private Date birth;
    private Date registryDate;
    private String email;
    private String emailCode;
    private String brief;
    /*用户的身份*/
    private Integer vip;
    private String userHead;
    /*粉丝数*/
    private Integer beanVermicelli;
    /*关注数*/
    private Integer attention;



}
