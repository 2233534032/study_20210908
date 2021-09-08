package com.study.service;

import com.study.pojo.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CommentService {

    List<Comment> findComment(Integer id);

    int addComment(HttpSession session,Integer blogId,String comment);

    Integer addSonComment(HttpSession session,Integer commentId,String content);

    List<SonCommentPage> findSonComment(Integer commentId);

    Integer giveComment(HttpSession session,Integer commentId);

    Integer getCommentGiveCount(Integer commentId);

    Integer ifUserIsGiveComment(HttpSession session,Integer commentId);

    Map<String,Object> addReplySonComment(HttpSession session, Integer id, Integer userId, String content);

    List<ReplySonCommentPage> findSonCommentRepltByCommentId(Integer commentId);

    Integer findSonCommentCount(Integer commentId);

    String findSonCommmentByCommentId(Integer commentId);

    Comment_entry findCommentById(Integer id);

    SonCommentPage findSonCommentByCommentId(Integer commentId);
}
