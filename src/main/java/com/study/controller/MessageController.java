package com.study.controller;

import com.study.pojo.MessageCommentData;
import com.study.pojo.MessageData;
import com.study.pojo.Page;
import com.study.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取消息的控制层
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;


    /*ajax接口，获取用户未读的点赞*/
    @RequestMapping("/findUserUnReadMessage")
    @ResponseBody
    public Page<MessageData> findUserUnReadGiveMessage(HttpSession session){
        Page<MessageData> page = messageService.findGiveByUserIdAndState(session,0);
        return page;
    }
    /*请求已读的点赞信息*/
    @RequestMapping("/findUserReadGive")
    @ResponseBody
    public Page<MessageData> findUserReadGive(HttpSession session){
        Page<MessageData> page = messageService.findGiveByUserIdAndState(session, 1);
        return page;
    }

    /*获取用户未读的评论*/
    @RequestMapping("/findUserUnReadComment")
    @ResponseBody
    public Page<MessageCommentData> findUserUnReadCommentMessage(HttpSession session){
        Page<MessageCommentData> page = messageService.findCommentByUserIdAndState(session, 0);
        return page;
    }

    /*获取用户历史已读评论*/
    @RequestMapping("/findUserReadComment")
    @ResponseBody
    public Page<MessageCommentData> findUserReadComment(HttpSession session){
        Page<MessageCommentData> page = messageService.findCommentByUserIdAndState(session, 1);
        return page;
    }


    /*判断是否有未读的评论信息*/
    @RequestMapping("/isCommentUnRead")
    @ResponseBody
    public Map<String,Object> isCommentUnRead(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        Integer count = messageService.isCommentUnRead(session);
        if(count==500){
            map.put("code",500);
            map.put("msg","无数据");
            return map;
        }
        map.put("code",200);
        map.put("msg","有数据");
        return map;
    }

    /*判断用户是否有未读点赞信息的方法*/
    @RequestMapping("/unReadMessageCount")
    @ResponseBody
    public Map<String,Object> unReadMessageCount(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        Integer count = messageService.findUserUnReadMessageCount(session);
        if(count==500){
            map.put("code",500);
            map.put("msg","没有数据");
            return map;
        }
        map.put("code",200);
        map.put("count",count);
        return map;
    }


    /*清除未读的点赞信息的提示*/
    @RequestMapping("/clearMessage")
    @ResponseBody
    public Map<String,Object> clearMessage(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        Integer integer = messageService.clearGiveMessage(session);
        map.put("code",200);
        map.put("msg","清除成功");
        map.put("count",integer);
        return map;
    }

    /*点击了获取评论后清除未读提示的方法*/
    @RequestMapping("/clearReadComment")
    @ResponseBody
    public Map<String,Object> clearReadComment(HttpSession session){
        Map<String,Object> map  =new HashMap<>();
        Integer i = messageService.clearComment(session);
        map.put("code",200);
        map.put("count",i);
        map.put("msg","未读消息清除成功");
        return map;
    }


}
