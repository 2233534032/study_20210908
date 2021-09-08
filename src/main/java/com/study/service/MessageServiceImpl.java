package com.study.service;

import com.study.dao.BlogDao;
import com.study.dao.CommentDao;
import com.study.dao.GiveDao;
import com.study.dao.UserDao;
import com.study.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private BlogDao blogDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GiveDao giveDao;
    @Autowired
    CommentDao commentDao;

    /*获取未读消息封装数据*/
    @Override
    public Page<MessageData> findGiveByUserIdAndState(HttpSession session,Integer state) {
        User sessionUser = (User) session.getAttribute("user");
        Page<MessageData> page = new Page<>();
        List<MessageData> list = new ArrayList<>();
        /*获取当前登录用户信息*/
        User user = userDao.findUserById(sessionUser.getId());
        /*设置当前登录的用户*/
        page.setUser(user);
        /*获取被当前用户的点赞信息*/
        List<Give> giveByUserId = giveDao.findGiveByUserId(user.getId(), state);
        /*等于0表示没有记录*/
        if(giveByUserId.size()==0){
            page.setState("无信息");
            return page;
        }
        /*循环遍历获取到的点赞信息*/
        for (Give g:giveByUserId) {
            MessageData me = new MessageData();
            /*设置登录用户信息*/
            me.setBlogUserId(user.getId());
            /*设置博客id*/
            me.setBlogId(g.getBlogId());
            /*设置点赞时间*/
            me.setGiveDate(g.getGiveDate());
            /*设置id*/
            me.setId(g.getId());
            /*设置状态*/
            me.setState(g.getState());
            /*设置当前登录用户（被点赞人的nice）*/
            me.setUsername(user.getUsername());
            Blog blog = blogDao.findBlogById(g.getBlogId());
            /*设置被点赞的正文内容*/
            me.setContent(blog.getMainBody());
            /**/
            String[] split = blog.getContentImg().split(",");
            if(split.length!=0){
                /*设置正文图片*/
                me.setImg(split[0]);
            }
            /*获取点赞人的信息*/
            User giveUser = userDao.findUserById(g.getUserId());
            me.setUserId(giveUser.getId());
            me.setGiveUsername(giveUser.getUsername());
            me.setGiveUserHead(giveUser.getUserHead());
            list.add(me);
        }
        page.setState("有信息");

        page.setData(list);
        return page;
    }

    /*获取未读消息的数量*/
    @Override
    public Integer findUserUnReadMessageCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer count = giveDao.findUserUnReadMessageCount(user.getId(), 0);
        if(count==0){
            return 500;
        }
        return count;
    }

    /*清除未读点赞提示信息*/
    @Override
    public Integer clearGiveMessage(HttpSession session){
        User user = (User) session.getAttribute("user");
        Integer i = giveDao.clearGiveMessage(user.getId());
        return i;
    }

    /*获取用户未读评论*/
    @Override
    public Page<MessageCommentData> findCommentByUserIdAndState(HttpSession session,Integer state) {
        Page<MessageCommentData> page = new Page<>();
        List<MessageCommentData> list = new ArrayList<>();
        User sessionUser = (User) session.getAttribute("user");
        User user = userDao.findUserById(sessionUser.getId());
        page.setUser(user);
        List<Comment_entry> comms = commentDao.findCommentByBlogUserId(user.getId(), state);
        if(comms.size()==0){
            page.setState("无数据");
            return page;
        }
        for (Comment_entry c:comms) {
            MessageCommentData data = new MessageCommentData();
            data.setId(c.getId());
            data.setBlogUserId(user.getId());
            Blog blog = blogDao.findBlogById(c.getBlogId());

            data.setBlogId(blog.getId());
            data.setState(c.getState());
            String imgs = blog.getContentImg();
            String[] split = imgs.split(",");
            if(split.length!=0){
                data.setImg(split[0]);
            }
            data.setUserId(c.getUserId());
            data.setContent(blog.getMainBody());
            data.setUsername(user.getUsername());
            User commentUser = userDao.findUserById(c.getUserId());
            data.setCommentUserHead(commentUser.getUserHead());
            data.setCommentUsername(commentUser.getUsername());
            data.setCommentDate(c.getCommentDate());
            data.setCommentContent(c.getComment());
            list.add(data);
        }
        page.setState("有数据");
        page.setData(list);
        return page;
    }

    /*判断未读信息*/
    @Override
    public Integer isCommentUnRead(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer count = commentDao.isCommentUnRead(user.getId(), 0);
        if(count==0){
            return 500;
        }
        return count;
    }

    /*清除已读评论*/
    @Override
    public Integer clearComment(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer integer = commentDao.clearComment(user.getId(), 0);
        return integer;
    }

}
