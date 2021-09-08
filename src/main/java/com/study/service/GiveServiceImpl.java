package com.study.service;

import com.study.dao.GiveDao;
import com.study.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class GiveServiceImpl implements GiveService {

    @Autowired
    private GiveDao giveDao;

    /*子评论回复的点赞信息*/
    @Override
    @Transactional
    public Integer addReplySonCommentGive(HttpSession session, Integer id) {
        User user = (User) session.getAttribute("user");
        Integer findCount = giveDao.findSonCommentReplyGiveByUserIdAndSonCommentReplyId(user.getId(), id);
        if(findCount==1){
            return 501;
        }
        int updateCount = giveDao.replySonCommentGIve(id);
        int insertCount = giveDao.addSonCommentReplyGive(id,user.getId(),new Date());
        if(updateCount!=1||insertCount!=1){
            return 500;
        }
        return 200;
    }

    /*获取子评论的点赞信息用于判断是否已经被点赞了*/
    @Override
    public Integer findReplySonCommentGiveByUserIdAndSonCommentReplyId(HttpSession session, Integer sonCommentReplyId) {
        User user = (User) session.getAttribute("user");
        Integer count = giveDao.findSonCommentReplyGiveByUserIdAndSonCommentReplyId(user.getId(), sonCommentReplyId);
        if(count!=1){
            return 500;
        }
        return 200;
    }

    /*子评论的点赞*/
    @Override
    @Transactional
    public Integer addSonCommentGive(HttpSession session, Integer id) {
        User user = (User) session.getAttribute("user");
        Integer count = giveDao.findSonComentGiveByUserIdAndSonCommentId(user.getId(),id);
        if(count==1){
            return 501;
        }
        Integer updateCount = giveDao.sonCommentGive(id);
        Integer insertCount = giveDao.addSonCommentGive( user.getId(),id, new Date());
        if(updateCount!=1||insertCount!=1){
            return 500;
        }
        return 200;
    }

    @Override
    public Integer findSonCommentGiveByUserIdAndSonCommentId(HttpSession session, Integer sonCommentId) {
        User user = (User) session.getAttribute("user");
        Integer count = giveDao.findSonComentGiveByUserIdAndSonCommentId(user.getId(), sonCommentId);
        if(count!=1){
            return 500;
        }
        return 200;
    }
}
