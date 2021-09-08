package com.study.dao;

import com.study.pojo.Give;
import com.study.pojo.MessageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface GiveDao {

    /*根据被点赞人（这里表示当前用户）id和状态码获取未读消息*/
    List<Give> findGiveByUserId(@Param("blogUserId") Integer blogUserId, @Param("state") Integer state);
    /*获取未读消息的数量*/
    Integer findUserUnReadMessageCount(@Param("userId") Integer userId,@Param("state") Integer state);

    /*清除未读提示信息*/
    Integer clearGiveMessage(@Param("userId") Integer userId);

    /*根据blogId删除点赞信息*/
    Integer deleteGiveByBlogId(@Param("blogId")Integer blogId);

    /*以下是操作son_comment_reply表的*/
    Integer replySonCommentGIve(@Param("id") Integer id);
    /*向son_comment_reply_give表中添加点赞信息*/
    Integer addSonCommentReplyGive(@Param("sonCommentReplyId") Integer sonCommentReplyId, @Param("userId") Integer userId, @Param("giveDate") Date giveDate);

    Integer findSonCommentReplyGiveByUserIdAndSonCommentReplyId(@Param("userId") Integer userId,@Param("sonCommentReplyId") Integer sonCommentReplyId);


    /*子评论的点赞事件*/
    Integer sonCommentGive(@Param("id") Integer id);
    Integer addSonCommentGive(@Param("userId") Integer userId,@Param("sonCommentId") Integer sonCommentId,@Param("giveDate") Date giveDate);

    Integer findSonComentGiveByUserIdAndSonCommentId(@Param("userId") Integer userId,@Param("sonCommentId") Integer sonCommentId);
}
