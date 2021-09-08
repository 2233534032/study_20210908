package com.study.service;

import com.study.pojo.User;

import javax.servlet.http.HttpSession;

public interface UserService {

    Integer registryUser(HttpSession session,String username,String password,String email,String code);

    User loginUser(String username,String password);

    User findUserById(Integer id);

    Integer usernameIsExist(String username);

    String updateUserHeadImg(HttpSession session,String path);

    /*关注操作*/
    Integer addAttention(HttpSession session,Integer attentionUserId);

    Integer findLoginUserIsAttentionUser(HttpSession session,Integer userId);
}

