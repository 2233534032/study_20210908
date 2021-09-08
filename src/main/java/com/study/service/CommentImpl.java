package com.study.service;

import com.study.dao.BlogDao;
import com.study.dao.CommentDao;
import com.study.dao.UserDao;
import com.study.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CommentImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Comment> findComment(Integer id) {
        List<Comment> list = commentDao.findCommentByBlogId(id);
        return list;
    }

    /*添加评论*/
    @Override
    public int addComment(HttpSession session, Integer blogId, String comment) {
        User user = (User) session.getAttribute("user");
        Blog blog = blogDao.findBlogById(blogId);

        int i = commentDao.addComment(blogId, user.getId(), comment, new Date(),blog.getUserId());
        return i;
    }

    /*添加子评论的方法*/
    @Override
    public Integer addSonComment(HttpSession session, Integer commentId,String content) {
        User user = (User) session.getAttribute("user");
        Comment_entry comm = commentDao.findCommentById(commentId);
        Integer i = commentDao.addSonComment(comm.getBlogId(), comm.getId(), user.getId(), comm.getUserId(), content, new Date(), 0);
        if(i!=1){
            return 500;
        }
        return i;
    }

    /*查询子评论*/
    @Override
    public List<SonCommentPage> findSonComment(Integer commentId) {
        List<SonCommentPage> sonCommentByCommentId = commentDao.findSonCommentByCommentId(commentId);
        return sonCommentByCommentId;
    }

    /*点赞评论*/
    @Override
    @Transactional
    public Integer giveComment(HttpSession session, Integer commentId) {
        User user = (User) session.getAttribute("user");
        Comment_entry comment = commentDao.findCommentById(commentId);
        /*查看用户是否已经点赞*/
        Integer isGive = commentDao.findGiveCommentByGiveUserIdAndCommentId(commentId, user.getId());
        if(isGive!=0){
            /*400表示用户已经点赞过了*/
            return 400;
        }
        /*修改总的点赞*/
        Integer i = commentDao.giveComment(commentId);
        /*增加点赞记录*/
        Integer count = commentDao.addUserGiveComment(commentId, user.getId(), comment.getUserId(), new Date());
        if(count==500){
            return 500;
        }
        return i;
    }

    /*获取总的评论点赞数*/
    @Override
    public Integer getCommentGiveCount(Integer commentId) {
        Integer commentGiveCount = commentDao.findCommentGiveCount(commentId);
        return commentGiveCount;
    }

    @Override
    public Integer ifUserIsGiveComment(HttpSession session, Integer commentId) {
        User user = (User) session.getAttribute("user");
        Integer commentUserIsGive = commentDao.findCommentUserIsGive(commentId, user.getId());
        /*不等于0表示已经点赞过了*/
        if(commentUserIsGive!=0){
            return 500;
        }
        return commentUserIsGive;
    }

    /*子评论的回复信息*/
    @Override
    public Map<String,Object> addReplySonComment(HttpSession session, Integer id,Integer userId, String content) {
        Map<String,Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        SonComment son = commentDao.findSonCommentById(id);
        Integer count =  commentDao.addSonCommentReply(son.getBlogId(),son.getCommentId(),son.getId(),user.getId(),userId,content,new Date());
        if(count!=1){
            map.put("code",500);
            map.put("msg","评论失败");
            return map;
        }
        ReplySonCommentDate comment = commentDao.findReplySonCommentByCommentIdAndNewComment(son.getCommentId());
        ReplySonCommentPage reply = new ReplySonCommentPage();
        reply.setId(comment.getId());
        reply.setCommentId(comment.getCommentId());
        reply.setUserId(comment.getUserId());
        User replyUser = userDao.findUserById(comment.getUserId());
        reply.setReplyUserName(replyUser.getUsername());
        reply.setCommentUserId(comment.getSonCommentUserId());
        User commentUser = userDao.findUserById(comment.getSonCommentUserId());
        reply.setCommentUserName(commentUser.getUsername());
        reply.setReplyContent(comment.getContent());
        reply.setCommentDate(comment.getCommentDate());
        reply.setGive(comment.getGive());
        map.put("code",200);
        map.put("msg","回复成功");
        map.put("data",reply);
        return map;
    }

    @Override
    public List<ReplySonCommentPage> findSonCommentRepltByCommentId(Integer commentId) {
        List<ReplySonCommentPage> list= new ArrayList<>();
        List<ReplySonCommentDate> repList = commentDao.findReplySonCommentByCommentId(commentId);
        for(ReplySonCommentDate r :repList){
            ReplySonCommentPage page = new ReplySonCommentPage();
            page.setId(r.getId());
            page.setCommentId(r.getCommentId());
            page.setUserId(r.getUserId());
            User replyUser = userDao.findUserById(r.getUserId());
            page.setReplyUserName(replyUser.getUsername());
            page.setCommentUserId(r.getSonCommentUserId());
            User commentUser = userDao.findUserById(r.getSonCommentUserId());
            page.setCommentUserName(commentUser.getUsername());
            page.setReplyContent(r.getContent());
            page.setCommentDate(r.getCommentDate());
            page.setGive(r.getGive());
            list.add(page);
        }
        return list;
    }

    /*获取子评论和子评论回复的个数*/
    @Override
    public Integer findSonCommentCount(Integer commentId) {
        int sonCommentCount=0;
        List<Integer> sonCommentIds = commentDao.findSonCommentIdByCommentId(commentId);
        sonCommentCount=sonCommentIds.size();
        for (int id:sonCommentIds){
            Integer replyCommentCount = commentDao.findReplyCommentCount(id);
            sonCommentCount+=replyCommentCount;
        }
        return sonCommentCount;
    }

    /*获取一条数据*/
    @Override
    public String findSonCommmentByCommentId(Integer commentId) {
        SonComment sonComment = commentDao.findSonCommentByCommentIdReturnOne(commentId);
        User userById = userDao.findUserById(sonComment.getUserId());
        return userById.getUsername();

    }

    /*根据id获取评论的详细数据*/
    @Override
    public Comment_entry findCommentById(Integer id) {
        Comment_entry commentById = commentDao.findCommentById(id);
        User user = userDao.findUserById(commentById.getUserId());
        commentById.setUsername(user.getUsername());
        commentById.setUserHead(user.getUserHead());
        return commentById;
    }

    /*获取最新一条数据*/
    @Override
    public SonCommentPage findSonCommentByCommentId(Integer commentId) {
        SonCommentPage sonComment = commentDao.findSonCommentByCommentIdAndNewSonComment(commentId);
        return sonComment;
    }
}
