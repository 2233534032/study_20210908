package com.study.dao;

import com.study.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserDao {

    int addUser(@Param("username") String username, @Param("password") String password, @Param("email") String email,@Param("registryDate") Date registryDate);

    User loginUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    User findUserById(@Param("id") Integer id);

    /*判断用户名是否存在*/
    User userIsExist(String username);
    /*更新用户头像的操作*/
    int updateUserHeadImg(@Param("id") Integer id,@Param("path") String path);

    /*修改粉丝数量*/
    Integer updateBeanVermicelliCount(@Param("id") Integer id);
    /*修改关注数量*/
    Integer updateAttentionCount(@Param("id") Integer id);
    /*添加操作关注的记录*/
    Integer insertAttention(@Param("userId") Integer userId,@Param("attentionUserId") Integer attentionUserId,@Param("attentionDate") Date attentionDate,@Param("state") Integer state);

    /*根据用户id和被关注用户id查询数据*/
    Integer findUserAttentionByUserIdAndAttentionUserId(@Param("userId") Integer userId,@Param("attentionUserId") Integer attentionUserId);
}
