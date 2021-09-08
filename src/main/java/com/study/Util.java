package com.study;

import com.study.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Component
public class Util {

    @Autowired
    private  JavaMailSender mail;

    @Autowired
    private RedisUtil redis;

    /*邮件发送的工具方法，@Async：这个方法是一个异步方法*/
    @Async
    public  String  getEmailCode(HttpSession session,String email)  {
        try{
            MimeMessage mimeMessage = mail.createMimeMessage();
            /*这个对象可以发送复杂的邮件*/
            MimeMessageHelper min = new MimeMessageHelper(mimeMessage,true);
            String code = code();
            if(code.length()<4){
                code=code+"0";
            }
            code="8888";
            System.out.println(code);
            /*发送人*/
            min.setFrom("2233534032@qq.com");
            /*接收人*/
            min.setTo(email);
            /*标题*/
            min.setSubject("验证码");
            /*邮件内容*/
            min.setText("验证码为："+code);
           /* *//*将验证码存入redis中*//*
            redis.set(email,code);*/
            /*核心发送验证码的操作*/
            /*将code存入session*/
            session.setAttribute("code",code);
          /*  mail.send(mimeMessage);*/
            return code;
        }catch(Exception e){
            new RuntimeException("邮件发送可能失败");
        }
        return null;
    }

    public  String code(){
        Random random = new Random();
        int i = random.nextInt(10000);
        return i+"";
    }
}
