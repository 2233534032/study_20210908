package com.study.service;

import javax.servlet.http.HttpSession;

public interface GiveService {

    /*添加点赞信息*/
    Integer addReplySonCommentGive(HttpSession session,Integer id);
    /*根据用户id和SonCommentReplyId查询当前用户是否点赞该条评论*/
    Integer findReplySonCommentGiveByUserIdAndSonCommentReplyId(HttpSession session,Integer sonCommentReplyId);

    Integer addSonCommentGive(HttpSession session,Integer id);

    Integer findSonCommentGiveByUserIdAndSonCommentId(HttpSession session,Integer sonCommentId);

}
