package com.study.controller;

import com.study.pojo.Blog;
import com.study.pojo.IndexContent;
import com.study.pojo.Page;
import com.study.pojo.User;
import com.study.service.BlogService;
import com.study.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*请求博客文章*/
@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;


    /*查询用户自己的博客*/
    @RequestMapping("/findMyBlog")
    @ResponseBody
    public Page<IndexContent> findBlogBySession(HttpSession session,@RequestParam("page") Integer page){
        Page<IndexContent> p = blogService.findMyBlog(session, page);
        System.out.println(p);
        return p;
    }
    /*根据用户id查询用户的博客*/
    @RequestMapping("/findBlogByUserId")
    @ResponseBody
    public Page<IndexContent> findBlogByUserId(@RequestParam("userId") Integer userId,@RequestParam("page") Integer page){
        Page<IndexContent> blogByUserIdToHomePage = blogService.findBlogByUserIdToHomePage(userId, page);
        return blogByUserIdToHomePage;
    }

    /*发表博客*/
    @RequestMapping("/newBlog")
    @ResponseBody
    public Map<String,Object> addBlog(HttpSession session, @RequestParam(value = "paths",required = false) String paths, @RequestParam(value = "content",required = false) String content) throws InterruptedException {
        Map<String,Object> map = new HashMap<>();
       User user = (User) session.getAttribute("user");
        if(user==null){
            map.put("code",501);
            map.put("msg","登录后可发布");
            return map;
        }
        int id = blogService.addBlog(user, paths, content);
        IndexContent blogByUserId = blogService.findBlogByUserId(session);
        map.put("value",blogByUserId);
        map.put("code",200);
        map.put("msg","发表成功");
        return map;
    }


    /*获取用户最新一条博客*/
    @RequestMapping("/findBlogByNew")
    @ResponseBody
    public Map<String,Object> findBlogByNew(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        IndexContent blogByUserId = blogService.findBlogByUserId(session);
        map.put("code",200);
        map.put("msg","执行成功");
        map.put("value",blogByUserId);
        return map;
    }

    /*用户删除自己发布的blog*/
    @RequestMapping("/userDeleteBlog")
    @ResponseBody
    public Map<String,Object> deleteBlog(@RequestParam("blogId")Integer blogId){
        Map<String,Object> map = new HashMap<>();
        blogService.deleteBlog(blogId);
        map.put("code",200);
        map.put("msg","删除成功");
        return map;
    }

}
