package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

      /*
         *   以下的不需要登录页可以访问的请求，
        *  /toLogin: 获取登录页面的请求
        * /toRegistry:获取注册页面
        * /login:登录请求
        *  /index,/,index.html:首页
        *   /findIndexContetn :首页获取推荐页数据的请求
        *   /getComment:获取评论
        *
      */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration inter = registry.addInterceptor(new MyInterceptor());
        inter.excludePathPatterns("/toLogin","/toRegistry","/login","/registry","login_new","/email_code","/usernameIsRegistry");
        /*需要被拦截的请求*/
        inter.addPathPatterns("/*");
    }
}
