package com.study.service;

import com.study.pojo.MessageCommentData;
import com.study.pojo.MessageData;
import com.study.pojo.Page;

import javax.servlet.http.HttpSession;

public interface MessageService {

    /*根据用户id获取未读的点赞信息*/
    Page<MessageData> findGiveByUserIdAndState(HttpSession session,Integer state);

    /*获取未读消息的数量*/
    Integer findUserUnReadMessageCount(HttpSession session);

    /*清除未读的点赞提示信息*/
    Integer clearGiveMessage(HttpSession session);

    /*获取用户未读的评论信息*/
    Page<MessageCommentData> findCommentByUserIdAndState(HttpSession session,Integer state);

    /*判断是否有未读评论*/
    Integer isCommentUnRead(HttpSession session);

    Integer clearComment(HttpSession session);
}
