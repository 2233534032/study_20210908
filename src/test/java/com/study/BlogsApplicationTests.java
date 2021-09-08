package com.study;

import com.study.dao.BlogDao;
import com.study.dao.CommentDao;
import com.study.dao.GiveDao;
import com.study.pojo.*;
import com.study.service.BlogService;
import com.study.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class BlogsApplicationTests {
    @Autowired
    private BlogService service;
    @Autowired
    GiveDao giveDao;

    @Autowired
    MessageServiceImpl messageService;

    @Autowired
    CommentDao commentDao;


    @Test
    public void test(){
        List<Give> giveByUserId = giveDao.findGiveByUserId(5, 0);
        for (Give g:giveByUserId) {
            System.out.println(g);
        }
    }

   /* @Test
    public void test2(){
        Page<MessageData> giveByUserIdAndState = messageService.findGiveByUserIdAndState();
        System.out.println(giveByUserIdAndState);
    }*/


    @Test
   public void test2(){
       List<SonCommentPage> sonCommentByCommentId = commentDao.findSonCommentByCommentId(68);
       for (SonCommentPage s:sonCommentByCommentId){
           System.out.println(s);
       }
   }



/*    @Test
    void contextLoads() throws MessagingException {

        List<Blog> blogByUserId = service.findBlogByUserId("1");
        for (Blog b:blogByUserId) {
            System.out.println(b);
        }

    }*/













}
