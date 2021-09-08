package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*页面请求转发的Controller*/
@Controller
public class PageTranspond {

    /*跳转用户主页*/
    @RequestMapping("/user_index")
    public String userIndex(){
        return "user/user_index";
    }

    /*跳转文件上传页*/
    @RequestMapping("/load")
    public String uploadPage(){
        return "upload";
    }

    /*跳转主页*/
    @RequestMapping({"/","index","index.html"})
    public String index(){
        return "index";
    }

    /*跳转登录页*/
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login_new";
    }
    /*跳转注册页*/
    @RequestMapping("/toRegistry")
    public String toRegistry(){
        return "registry";
    }

    /*跳转新的注册登录页*/
    @RequestMapping("/login_new")
    public String toLogin_new(){
        return "login_new";
    }

    @RequestMapping("/user_index_new")
    public String userIndexNew(){
        return "user/user_index_new";
    }

    /*请求转发到message*/
    @RequestMapping("/message_center")
    public String messageCenter(){
        return "message_center";
    }


    @RequestMapping("/home")
    public String homePage(){
        return "homePage";
    }


}
