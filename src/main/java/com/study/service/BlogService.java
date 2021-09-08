package com.study.service;

import com.study.pojo.Blog;
import com.study.pojo.IndexContent;
import com.study.pojo.Page;
import com.study.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface BlogService {

    Page<IndexContent> findMyBlog(HttpSession session,Integer page);

    Page<IndexContent> findBlogByUserIdToHomePage(Integer userId,Integer page);

    Page<IndexContent> findIndexContent(HttpSession session, Integer currentPage);

    int addBlog(User user,String paths,String content);

    int findBlogGiveCount(Integer blogId);

    int addBlogGive(Integer blogId,HttpSession session);

    int findUserIsGive(Integer blogId,HttpSession session);

    int findBlogCommentCount(Integer blogId);

    IndexContent findBlogByUserId(HttpSession session);

    List<Blog> findIndexSearchDate();

    Integer deleteBlog(Integer blogId);
}
