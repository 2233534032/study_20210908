package com.study.dao;

import com.study.pojo.Blog;
import com.study.pojo.IndexContent;
import com.study.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Mapper
public interface BlogDao {

    /*查询自己的bolgs*/
    List<IndexContent> findMyBlog(@Param("pageCount") Integer pageCount,@Param("pageSize") Integer pageSize,@Param("userId") Integer userId);

    /*首页推荐数据*/
    List<IndexContent> findIndexContent(@Param("pageCount") Integer pageCount,@Param("pageSize") Integer pageSize);

    /*新增博客*/
    int addBlog(@Param("filePath") String filePath, @Param("userId") Integer userId, @Param("newDate") Date newDate, @Param("content") String content);

    /*点赞功能*/
    int addGive(@Param("blogId") Integer blogId,@Param("userId") Integer userId,@Param("blogUserId") Integer blogUserId,@Param("giveDate") Date giveDate,@Param("state")Integer state);

    /*根据id查询博客*/
    Blog findBlogById(@Param("blogId") Integer blogId);
    /*更新点赞数*/
    int updateGiveCount(@Param("blogId") Integer blogId);
    /*根据id获取点赞总数*/
    int getBlogGiveCount(@Param("blogId") Integer blogId);
    /*根据用户id和blogId，查询用户点赞信息*/
    int findUserIsGive(@Param("userId") Integer userId,@Param("blogId") Integer blogId);
    /*根据博客id获取评论的总数*/
    int findBlogCommentCount(@Param("blogId") Integer blogId);
    /*获取总发表数*/
    int findBlogCount();
    /*根据用户id查询发布的博客，封装成IndexContent返回*/
    IndexContent findBlogByUserId(@Param("userId") Integer userId);
    /*获取单个用户的博客总数*/
    int findMyBlogCount(@Param("userId") Integer userId);

    /*请求热搜榜数据，这里就请求最新的十条数据即可    */
    List<Blog> findIndexSearchData();

    /*删除自己的blog*/
    Integer deleteBlog(@Param("blogId") Integer blogId);


}
