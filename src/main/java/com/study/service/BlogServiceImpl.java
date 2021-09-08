package com.study.service;

import com.study.dao.BlogDao;
import com.study.dao.CommentDao;
import com.study.dao.GiveDao;
import com.study.dao.UserDao;
import com.study.pojo.Blog;
import com.study.pojo.IndexContent;
import com.study.pojo.Page;
import com.study.pojo.User;
import com.study.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements  BlogService{
    @Autowired
    private UserDao userDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private GiveDao giveDao;


    /*获取登录用户自己的博客*/
    @Override
    public Page<IndexContent> findMyBlog(HttpSession session,Integer page) {
        Page<IndexContent> p = new Page<>();
        /*设置当前是第几页*/
        if(page!=null){
            p.setCurrentPage(page);
        }
        /*当前查询的offset*/
        Integer offset = (p.getCurrentPage()-1)*p.getPageSize();
        /*从session中获取当前登录的对象*/
        User u = (User) session.getAttribute("user");
        List<IndexContent> list = blogDao.findMyBlog(offset, p.getPageSize(), u.getId());
        /*未完成*/
        /*获取总记录数*/
        int myBlogCount = blogDao.findMyBlogCount(u.getId());
        /*根据总记录数计算得出总页数*/
        int sum = myBlogCount/p.getPageSize();
        /*如果除不尽，则页数+1*/
        if(sum%p.getPageSize()!=0){
            sum++;
        }
        /*设置总页数*/
        p.setPageCount(sum);
        /*处理返回的数据*/
        for (IndexContent co:list) {
            String contentImg = co.getContentImg();
            if(contentImg.length()==0){
                co.setImgCount(0);
            }else{
                String[] split = contentImg.split(",");
                /*只有一张图片的情况*/
                if (split.length == 1) {
                    List<String> l = co.getImgs();
                    l.add(split[0]);
                    co.setContentImg(split[0]);
                    co.setImgCount(1);
                    /*有多张图片的情况*/
                } else if(split.length>=2){
                    for (int i = 0; i < split.length; i++) {
                        List<String> imgs = co.getImgs();
                        imgs.add(split[i]);
                        co.setImgCount(split.length);
                    }
                    /*没有图片的情况*/
                }else{
                    co.setContentImg("无图片");
                    co.setImgCount(0);
                }
            }
            co.setLoginUserHead(u.getUserHead());
            /*评论的总数*/
            int commentCount = commentDao.findCommentCountByBlogId(co.getBlogId());
            co.setCommentCount(commentCount);
            /*获取点赞数*/
            int blogGiveCount = blogDao.getBlogGiveCount(co.getBlogId());
            co.setGiveCount(blogGiveCount);
        }
        /*TODD:这里可以给当前文章添加评论数和转发等数*/
        p.setData(list);
        User userById = userDao.findUserById(u.getId());
        p.setUser(userById);
        return p;
    }

    /*获取登录用户自己的博客*/
    @Override
    public Page<IndexContent> findBlogByUserIdToHomePage(Integer userId,Integer page) {
        Page<IndexContent> p = new Page<>();
        /*设置当前是第几页*/
        if(page!=null){
            p.setCurrentPage(page);
        }
        /*当前查询的offset*/
        Integer offset = (p.getCurrentPage()-1)*p.getPageSize();
        User u = userDao.findUserById(userId);
        List<IndexContent> list = blogDao.findMyBlog(offset, p.getPageSize(), userId);
        /*未完成*/
        /*获取总记录数*/
        int myBlogCount = blogDao.findMyBlogCount(userId);
        /*根据总记录数计算得出总页数*/
        int sum = myBlogCount/p.getPageSize();
        /*如果除不尽，则页数+1*/
        if(sum%p.getPageSize()!=0){
            sum++;
        }
        /*设置总页数*/
        p.setPageCount(sum);
        /*处理返回的数据*/
        for (IndexContent co:list) {
            String contentImg = co.getContentImg();
            if(contentImg.length()==0){
                co.setImgCount(0);
            }else{
                String[] split = contentImg.split(",");
                /*只有一张图片的情况*/
                if (split.length == 1) {
                    List<String> l = co.getImgs();
                    l.add(split[0]);
                    co.setContentImg(split[0]);
                    co.setImgCount(1);
                    /*有多张图片的情况*/
                } else if(split.length>=2){
                    for (int i = 0; i < split.length; i++) {
                        List<String> imgs = co.getImgs();
                        imgs.add(split[i]);
                        co.setImgCount(split.length);
                    }
                    /*没有图片的情况*/
                }else{
                    co.setContentImg("无图片");
                    co.setImgCount(0);
                }
            }
            co.setLoginUserHead(u.getUserHead());
            /*评论的总数*/
            int commentCount = commentDao.findCommentCountByBlogId(co.getBlogId());
            co.setCommentCount(commentCount);
            /*获取点赞数*/
            int blogGiveCount = blogDao.getBlogGiveCount(co.getBlogId());
            co.setGiveCount(blogGiveCount);
        }
        /*TODD:这里可以给当前文章添加评论数和转发等数*/
        p.setData(list);
        User userById = userDao.findUserById(u.getId());
        p.setUser(userById);
        return p;
    }

    /*获取首页的数据*/
    @Override
    public Page<IndexContent> findIndexContent(HttpSession session,Integer currentPage) {

        Page<IndexContent> page = new Page<>();
        if(currentPage!=null){
            page.setCurrentPage(currentPage);
        }
        /*当前查询的offset*/
        Integer offset = (page.getCurrentPage()-1)*page.getPageSize();

        /*从session中获取当前登录的对象*/
        User u = (User) session.getAttribute("user");
        /*根据id获取当前用户对象*/
        User user = userDao.findUserById(u.getId());

        List<IndexContent> list = blogDao.findIndexContent(offset,page.getPageSize());
        /*获取记录数数*/
        int blogCount = blogDao.findBlogCount();
        /*计算得出总页数*/
        int sum = blogCount/page.getPageSize();
        /*如果除不尽则页数+1*/
        if(blogCount%page.getPageSize()!=0){
            sum++;
        }
        /*设置总页数*/
        page.setPageCount(sum);

        for (IndexContent co:list) {
            String contentImg = co.getContentImg();
            if(contentImg.length()==0){
                co.setImgCount(0);
            }else{
                String[] split = contentImg.split(",");
                /*只有一张图片的情况*/
                if (split.length == 1) {
                    List<String> l = co.getImgs();
                    l.add(split[0]);
                    co.setContentImg(split[0]);
                    co.setImgCount(1);
                    /*有多张图片的情况*/
                } else if(split.length>=2){
                    for (int i = 0; i < split.length; i++) {
                        List<String> imgs = co.getImgs();
                        imgs.add(split[i]);
                        co.setImgCount(split.length);
                    }
                    /*没有图片的情况*/
                }else{
                    co.setContentImg("无图片");
                    co.setImgCount(0);
                }
            }
            co.setLoginUserHead(user.getUserHead());
            /*评论的总数*/
            int commentCount = commentDao.findCommentCountByBlogId(co.getBlogId());
            co.setCommentCount(commentCount);
            /*获取点赞数*/
            int blogGiveCount = blogDao.getBlogGiveCount(co.getBlogId());
            co.setGiveCount(blogGiveCount);
        }
        /*TODD:这里可以给当前文章添加评论数和转发等数*/
        page.setData(list);
        page.setUser(user);
        return page;
    }

    /*发表博客*/
    @Override
    public int addBlog(User user, String paths, String content) {
        StringBuilder pathString = new StringBuilder();
        if(paths.length()!=0){
            String[] split = paths.split(",");
            String path [] = new String[split.length];
            for(int i =0;i<split.length;i++){
                String p = FileUploadUtil.copyFile(split[i], "content");
                path[i] = p;
            }

            for (int i = 0;i<path.length;i++){
                String[] split1 = path[i].split("/");
                String p = "./"+split1[split1.length-2]+"/"+split1[split1.length-1]+",";
                pathString.append(p);
            }
        }
        Date date = new Date();
        int i = blogDao.addBlog(pathString.toString(), user.getId(), date, content);
        return i;
    }

    /*根据博客id获取总点赞数据*/
    @Override
    public int findBlogGiveCount(Integer blogId) {
          return   blogDao.getBlogGiveCount(blogId);

    }

    /*点赞的操作*/
    @Override
    public int addBlogGive(Integer blogId, HttpSession session) {
        Integer state = 0;
        User user = (User) session.getAttribute("user");
        if(user==null){
            new RuntimeException("用户未登录");
            return 0;
        }
        /*根据blogId，获取被点赞人*/
        Blog blogById = blogDao.findBlogById(blogId);
        /*添加点赞信息*/
        blogDao.addGive(blogId, user.getId(),blogById.getUserId(),new Date(),state);
        /*更新点赞信息*/
        int i = blogDao.updateGiveCount(blogId);
        return i;
    }

    /*判断用户是否已经点过赞*/
    @Override
    public int findUserIsGive(Integer blogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return  blogDao.findUserIsGive(user.getId(),blogId);
    }

    /*根据id获取评论的总数*/
    @Override
    public int findBlogCommentCount(Integer blogId) {
        return   blogDao.findBlogCommentCount(blogId);
    }

    /*根据用户id查询最新发布的一条数据*/
    @Override
    public IndexContent findBlogByUserId(HttpSession session) {
        User user = (User) session.getAttribute("user");
        IndexContent co = blogDao.findBlogByUserId(user.getId());
        /*设置登录用户的头像*/
        co.setLoginUserHead(user.getUserHead());
        /*解析用户的图片*/
        String contentImg = co.getContentImg();
        if (contentImg.length() == 0) {
            co.setImgCount(0);
        } else {
            String[] split = contentImg.split(",");
            /*只有一张图片的情况*/
            if (split.length == 1) {
                List<String> l = co.getImgs();
                l.add(split[0]);
                co.setContentImg(split[0]);
                co.setImgCount(1);
                /*有多张图片的情况*/
            } else if (split.length >= 2) {
                for (int i = 0; i < split.length; i++) {
                    List<String> imgs = co.getImgs();
                    imgs.add(split[i]);
                    co.setImgCount(split.length);
                }
                /*没有图片的情况*/
            } else {
                co.setContentImg("无图片");
                co.setImgCount(0);
            }
        }
        /*获取评论数和点赞数*/
        /*评论的总数*/
        int commentCount = commentDao.findCommentCountByBlogId(co.getBlogId());
        co.setCommentCount(commentCount);
        /*获取点赞数*/
        int blogGiveCount = blogDao.getBlogGiveCount(co.getBlogId());
        co.setGiveCount(blogGiveCount);
        return co;
    }

    @Override
    public List<Blog> findIndexSearchDate() {
        List<Blog> list = blogDao.findIndexSearchData();
        return list;
    }

    /*删除blog  同时删除点赞信息和评论信息*/
    @Override
    @Transactional
    public Integer deleteBlog(Integer blogId) {
        /*删除评论*/
        commentDao.deleteCommentByBlogId(blogId);
        /*删除点赞信息*/
        giveDao.deleteGiveByBlogId(blogId);
        /*删除blog*/
        Integer count = blogDao.deleteBlog(blogId);
        return count;
    }
}
