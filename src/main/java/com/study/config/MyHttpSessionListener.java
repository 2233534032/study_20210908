package com.study.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
@WebListener
public class MyHttpSessionListener implements HttpSessionListener {

    /*在线人数。默认启动项目是0*/
    private static Integer COUNT = 0;
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        COUNT++;
        System.out.println("当前在线人数："+COUNT);
        System.out.println("有人来了");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        COUNT--;
        System.out.println("当前在线人数："+COUNT);
        System.out.println("有人走了");
    }

    /*获取在线人数*/
    public static Integer getCOUNT() {
        return COUNT;
    }
}
