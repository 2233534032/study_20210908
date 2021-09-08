package com.study.service;

import com.study.dao.UserDao;
import com.study.pojo.User;
import com.study.util.FileUploadUtil;
import com.study.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil redis;




    /*用户注册的方法*/
    @Override
    public Integer registryUser(HttpSession session,String username, String password, String email, String code) {
       /* String o = (String) redis.get(email);*/
        String o = (String) session.getAttribute("code");
        if(o==null){
            /*501表示验证码已过期*/
            return 501;
        }else if(!o.equals(code)){
            /*502表示验证码错误*/
            return 502;
        }
        int i = userDao.addUser(username, password, email, new Date());
        return i;
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userDao.loginUserByUsernameAndPassword(username, password);
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        return  userDao.findUserById(id);
    }


    /*判断用户名是否存在*/
    @Override
    public Integer usernameIsExist(String username) {
        User user = userDao.userIsExist(username);
        if(user==null){
            return 200;
        }
        return 500;
    }

    /*更新用户头像*/
    @Override
    public String updateUserHeadImg(HttpSession session, String path) {
        User user = (User) session.getAttribute("user");
        String title = FileUploadUtil.copyFile(path, "title");
        String[] split = title.split("/");
        String filePath = "./"+split[split.length-2]+"/"+split[split.length-1];
        int i = userDao.updateUserHeadImg(user.getId(), filePath);
        if(i!=1){
            return "501";
        }
        return filePath;
    }

    /*关注操作
    * 500：表示已经关注过了
    * 501: 表示自己关注自己
    * */
    @Override
    @Transactional
    public Integer addAttention(HttpSession session, Integer attentionUserId) {
        User user = (User) session.getAttribute("user");
        if(user.getId()==attentionUserId){
            return 501;
        }
        /*判断是否已经被关注*/
        Integer count = userDao.findUserAttentionByUserIdAndAttentionUserId(user.getId(), attentionUserId);
        if(count!=0){
            return 500;
        }
        /*修改关注总数*/
        userDao.updateAttentionCount(user.getId());
        /*修改粉丝总数*/
        userDao.updateBeanVermicelliCount(attentionUserId);
        /*添加关注记录*/
        Integer integer = userDao.insertAttention(user.getId(), attentionUserId, new Date(), 0);
        return integer;
    }

    /*判断当前对象是否被登录用户关注*/
    @Override
    public Integer findLoginUserIsAttentionUser(HttpSession session, Integer userId) {
        User user = (User) session.getAttribute("user");
        if(user.getId()==userId){
            return 501;
        }
        Integer count = userDao.findUserAttentionByUserIdAndAttentionUserId(user.getId(), userId);
        if(count!=0){
            return 500;
        }else{
            return 200;
        }
    }
}
