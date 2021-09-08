package com.study.controller;

import com.study.pojo.Comment;
import com.study.pojo.Comment_entry;
import com.study.service.CommentService;
import com.study.service.GiveService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/*评论的控制层*/
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private GiveService giveService;

    /*点赞评论的接口*/
    @RequestMapping("/comment_give")
    @ResponseBody
    public Map<String,Object> UserGiveComment(HttpSession session, @RequestParam("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();
        Integer count = commentService.giveComment(session, commentId);
        if(count==500){
            map.put("code",500);
            map.put("msg","点赞失败");
            return map;
        }else if(count==400){
            map.put("code",400);
            map.put("msg","已经赞过");
            return map;
        }
        map.put("code",200);
        map.put("msg","点赞成功");
        map.put("count",count);
        return map;
    }

    /*判断用户是否点赞*/
    @RequestMapping("/commentIsGive")
    @ResponseBody
    public Map<String,Object> commentUserIsGive(HttpSession session,@Param("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();
        Integer count = commentService.ifUserIsGiveComment(session, commentId);
        if(count==500){
            map.put("code",500);
            map.put("msg","已经赞过");
            return map;
        }
        map.put("code",200);
        map.put("msg","还未点赞");
        return map;
    }
    /*请求评论的总点赞数*/
    @RequestMapping("/getCommentGiveCount")
    @ResponseBody
    public Map<String,Object> getCommentGiveCount(@RequestParam("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();
        Integer commentGiveCount = commentService.getCommentGiveCount(commentId);
        map.put("code",200);
        map.put("count",commentGiveCount);
        return map;
    }

    /*获取当前被点击的评论的详细信息*/
    @RequestMapping("/getThisClickComment")
    @ResponseBody
    public Map<String,Object> getThisClickComment(@RequestParam("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();
        Comment_entry comment = commentService.findCommentById(commentId);
        Integer count = commentService.findSonCommentCount(commentId);
        map.put("comment",comment);
        map.put("count",count);
        return map;
    }

    /*子评论的点赞事件*/
    @RequestMapping("/SonCommentGive")
    @ResponseBody
    public Map<String,Object> sonCommentGive(HttpSession session ,@RequestParam("id") Integer id){
        Map<String,Object> map = new HashMap<>();
        Integer count = giveService.addSonCommentGive(session, id);
        if(count==500){
            map.put("code",500);
            map.put("msg","点赞失败");
            return map;
        }
        if(count==501){
            map.put("code",501);
            map.put("msg","已经点赞");
            return map;
        }
        map.put("code",200);
        map.put("msg","点赞成功");
        return map;
    }

    /*子评论的回复的点赞*/
    @RequestMapping("/replySonCommentGive")
    @ResponseBody
    public Map<String,Object> replySonCommentGive(HttpSession session,@RequestParam("id") Integer id){
        Map<String,Object> map = new HashMap<>();
        Integer count = giveService.addReplySonCommentGive(session, id);
        if(count==501){
            map.put("code",501);
            map.put("msg","已经点赞");
            return map;
        }
        if(count==500){
            map.put("code",500);
            map.put("msg","点赞失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","点赞成功");
        return map;
    }


    /*判断子评论是否被点赞*/
    @RequestMapping("/sonCommentLoginUserIsGive")
    @ResponseBody
    public Map<String,Object> loginUserIsGiveSonComment(HttpSession session,@RequestParam("id")Integer id){
        Map<String,Object> map = new HashMap<>();
        Integer count = giveService.findSonCommentGiveByUserIdAndSonCommentId(session, id);
        if(count==500){
            map.put("code",500);
            map.put("msg","未点赞");
            return map;
        }
        map.put("code",200);
        map.put("msg","已点赞");
        return map;
    }

    /*判断子评论的回复评论是否被点赞*/
    @RequestMapping("/sonReplyCommentLoginUserIsGive")
    @ResponseBody
    public Map<String,Object> loginUserIsGiveReplySonComment(HttpSession session,@RequestParam("id")Integer id){
        Map<String,Object> map = new HashMap<>();
        Integer count = giveService.findReplySonCommentGiveByUserIdAndSonCommentReplyId(session,id);
        if(count==500){
            map.put("code",500);
            map.put("msg","未点赞");
            return map;
        }
        map.put("code",200);
        map.put("msg","已点赞");
        return map;
    }
}
