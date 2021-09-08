package com.study.dao;

import com.study.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommentDao {

        /*根据博客id获取总评论*/
        List<Comment> findCommentByBlogId(@Param("id") Integer id);
        /*添加评论*/
        int  addComment(@Param("blogId") Integer blogId,@Param("userId") Integer userId, @Param("comment") String comment, @Param("commentDate") Date commentDate,@Param("blogUserId") Integer blogUserId);
        /*根据博客id获取评论总数*/
        int findCommentCountByBlogId(@Param("id") Integer id);

        /*根据被点赞人（这里表示当前用户）id和状态码获取未读消息*/
        List<Comment_entry> findCommentByBlogUserId(@Param("blogUserId") Integer blogUserId, @Param("state") Integer state);

        Integer isCommentUnRead(@Param("blogUserId") Integer userId,@Param("state") Integer state);
        /*清除未读评论*/
        Integer clearComment(@Param("blogUserId") Integer blogUserId,@Param("state") Integer state);

        /*根据commentId获取评论*/
        Comment_entry findCommentById(@Param("id") Integer id);


        Integer addSonComment(@Param("blogId")Integer blogId,@Param("commentId")Integer commentId,@Param("userId")Integer userId,
                              @Param("commentUserId")Integer commentUserId,@Param("content")String content,@Param("commentDate")Date commentDate,@Param("give")Integer give);

        /*根据commentId获取评论对应的子评论集*/
        List<SonCommentPage> findSonCommentByCommentId(@Param("commentId") Integer commentId);

        /*点赞评论的功能*/
        Integer giveComment(@Param("id") Integer id);
        /*删除评论信息*/
        Integer deleteCommentByBlogId(@Param("blogId")Integer blogId);

        /*根据id查询sonComment*/
        SonComment findSonCommentById(@Param("id") Integer id);

        /*获取最新的一条数据*/
        SonCommentPage findSonCommentByCommentIdAndNewSonComment(@Param("commentId") Integer commentId);

        /*返回Soncomment表中对应的id*/
        List<Integer> findSonCommentIdByCommentId(@Param("commentId") Integer commentId);

        Integer  addUserGiveComment(@Param("commentId") Integer commentId,@Param("giveUserId") Integer giveUserId,@Param("commentUserId") Integer commentUserId,@Param("giveDate") Date giveDate);

        /*根据commentId 和giveUserId 查询符合条件的字段*/
        Integer findGiveCommentByGiveUserIdAndCommentId(@Param("commentId") Integer commentId,@Param("giveUserId") Integer giveUserId);
        /*请求总的点赞数*/
        Integer findCommentGiveCount(@Param("commentId") Integer commentId);

        Integer findCommentUserIsGive(@Param("commentId") Integer commentId,@Param("userId") Integer userId);

        /*返回一条数据*/
        SonComment findSonCommentByCommentIdReturnOne(@Param("commentId") Integer commentId);

        Comment findThisClickCommentById(@Param("id") Integer id);


        /*以下是访问son_comment_reply的-------------------------------------*/
        Integer addSonCommentReply(Integer blogId,Integer commentId,Integer sonCommentId, Integer userId, Integer sonCommentUserId,
                                   String content,Date commentDate);

        /*根据commentId获取子评论的回复信息*/
        List<ReplySonCommentDate> findReplySonCommentByCommentId(@Param("commentId") Integer commentId);

        /*获取子评论的回复的个数*/
        Integer findReplyCommentCount(@Param("sonCommentId") Integer sonCommentId);

        ReplySonCommentDate findReplySonCommentByCommentIdAndNewComment(@Param("commentId") Integer commentId);


}
