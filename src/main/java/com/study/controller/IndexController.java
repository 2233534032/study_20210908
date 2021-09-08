package com.study.controller;

import com.study.pojo.*;
import com.study.service.BlogService;
import com.study.service.CommentService;
import com.study.util.FileUploadUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*首页*/
@Controller
public class IndexController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    /*ajax获取评论的接口*/
    @RequestMapping(value = "/getComment",consumes = "application/json")
    @ResponseBody
    public Map<String,Object> getComment(String id) {
        Map<String,Object> map = new HashMap<>();
        Integer i = Integer.parseInt(id);
        List<Comment> comment = commentService.findComment(i);
        map.put("commentList",comment);
        return map;
    }

    /*ajax获取首页的中间正文部分内容*/
    @RequestMapping("/findIndexContent")
    @ResponseBody
    public Page<IndexContent> findIndexPageContent(HttpSession session,@RequestParam(value = "page",required = false) Integer page){
        Page<IndexContent> indexContent = blogService.findIndexContent(session, page);
        return indexContent;
    }

    /*请求热搜榜数据*/
    @RequestMapping("/findTopSearchData")
    @ResponseBody
    public Map<String,Object> findIndexTopSearch(){
        Map<String,Object> map = new HashMap<>();
        List<Blog> list = blogService.findIndexSearchDate();
        map.put("code",200);
        map.put("data",list);
        return map;
    }


    /*添加评论的ajax接口
    * session :获取当前登录的对象
    * blogId :当前博客的id
    * comment:当前用户的评论
    * */
    @RequestMapping("/userComment")
    @ResponseBody
    public Map<String,Object> addComment(HttpSession session,@RequestParam("blogId") Integer blogId,@RequestParam("comment") String comment){
        Map<String,Object> map = new HashMap<>();
        int i = commentService.addComment(session, blogId, comment);
        if(i!=1){
            map.put("code",501);
            map.put("msg","评论可能失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","评论成功");
        return map;
    }

    /*点赞接口，点赞业务*/
    @RequestMapping("/praise")
    @ResponseBody
    public Map<String,Object> praise(HttpSession session,@RequestParam("blogId") Integer blogId){
        Map<String,Object> map = new HashMap<>();
        /*判断用户是否已经点过赞了*/
        int isGive = blogService.findUserIsGive(blogId, session);
        if(isGive!=0){
            map.put("code",500);
            map.put("msg","已经赞过");
            return map;
        }
        /*评论userId和blogId*/
        int i = blogService.addBlogGive(blogId, session);
        if(i!=1){
            map.put("code",500);
            map.put("msg","点赞失败");
            return map;
        }
        /*获取点赞总数*/
        int blogGiveCount = blogService.findBlogGiveCount(blogId);
        map.put("code",200);
        map.put("count",blogGiveCount);
        map.put("blogId",blogId);
        map.put("msg","点赞成功");
        return map;
    }

    /**
     *
     * 返回0表示没有点赞过，返回非0表示已经点赞过
     * @param session
     * @param blogId
     * @return
     */
    @RequestMapping("/userIsGive")
    @ResponseBody
    public Map<String,Object> isGive(HttpSession session,@RequestParam("blogId") Integer blogId){
        Map<String,Object> map = new HashMap<>();
        int userIsGive = blogService.findUserIsGive(blogId, session);
        map.put("code",200);
        map.put("isGive",userIsGive);
        return map;
    }
    /*获取评论的总数 接口*/
    @RequestMapping("/getCommentCount")
    @ResponseBody
    public Map<String,Object> getCommentCount(@RequestParam("blogId")Integer blogId){
        Map<String,Object> map = new HashMap<>();
        int blogCommentCount = blogService.findBlogCommentCount(blogId);
        map.put("code",200);
        map.put("count",blogCommentCount);
        return map;
    }

    /**
     * 异步上传图片的接口,该接口将上传的图片放到缓存中，如果用户确认上传，会将图片上传到正式存放文件夹，
     * 缓存文件夹会定时清除
     * @param file
     * @return
     */
    @RequestMapping("/upload_content_img")
    @ResponseBody
    public Map<String,Object> uploadContentImg(@RequestParam("file") MultipartFile file){
        String path = FileUploadUtil.fileUploadTitle(file, "cache");
        String[] split = path.split("/");
        String callbackPath = "./"+split[split.length-2]+"/"+split[split.length-1];
        /*图片名称，不带后缀的*/
        String imgName = split[split.length - 1].split("\\.")[0];
        System.out.println(Arrays.toString(split));
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("src",callbackPath);
        map.put("name",imgName);
        return map;
    }

   /* 根据id获取子评论的情况*/
    @RequestMapping("/findSonComment")
    @ResponseBody
    public Map<String,Object> findSonComment(@RequestParam(value = "commentId",required = false)Integer commentId){
        Map<String,Object> map = new HashMap<>();
        List<SonCommentPage> sonComment = commentService.findSonComment(commentId);
        if(sonComment.size()==0){
            map.put("code",500);
            map.put("msg","没有子评论");
            return map;
        }
        map.put("code",200);
        map.put("msg","有子评论");
        map.put("sonComment",sonComment);
        return map;
    }


    /*添加子评论的方法*/
    @RequestMapping("/comment_son_comment")
    @ResponseBody
    public Map<String,Object> addSonComment(HttpSession session,@RequestParam("commentId") Integer commentId,@RequestParam("content") String content){
        Map<String,Object> map = new HashMap<>();
        System.out.println("commentId:"+commentId+" content:"+content);
        Integer integer = commentService.addSonComment(session, commentId, content);
        if(integer==500){
            map.put("code",500);
            map.put("msg","回复失败");
            return map;
        }
        /*根据commentId，获取刚才的那条数据*/
        SonCommentPage sonComment = commentService.findSonCommentByCommentId(commentId);
        map.put("code",200);
        map.put("msg","回复成功");
        map.put("sonComment",sonComment);
        return map;
    }


    /*子评论被评论的事件*/
    @RequestMapping("/reply_son_comment")
    @ResponseBody
    public Map<String,Object> replyComment(HttpSession session,@RequestParam("id") Integer id,@RequestParam("userId")Integer userId,@RequestParam("content") String content){
        Map<String, Object> map = commentService.addReplySonComment(session, id, userId, content);
        return map;
    }

    /*获取子评论的回复评论*/
    @RequestMapping("/findReplySonComment")
    @ResponseBody
    public Map<String,Object> findResplySonComment(@RequestParam("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();
        List<ReplySonCommentPage> list = commentService.findSonCommentRepltByCommentId(commentId);
        for (ReplySonCommentPage p:list) {
            System.out.println(p);
        }
        if(list.size()<=0){
            map.put("code",500);
            map.put("msg","无数据");
            return map;
        }
        map.put("replySonComment",list);
        map.put("code",200);
        map.put("msg","有数据");
        return map;
    }

    /*获取子评论和子评论回复的个数*/
    @RequestMapping("/findSonCommentAndReplySonCommentCount")
    @ResponseBody
    public Map<String,Object> findSonCommentCount(@RequestParam("commentId") Integer commentId){
        Map<String,Object> map = new HashMap<>();

        Integer sonCommentCount = commentService.findSonCommentCount(commentId);
        if(sonCommentCount==0){
            map.put("code",500);
            return map;
        }
        /*获取排在最前面的子评论的用户的username*/
        String username = commentService.findSonCommmentByCommentId(commentId);
        map.put("code",200);
        map.put("count",sonCommentCount);
        map.put("username",username);
        return map;
    }


}
