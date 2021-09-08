package com.study.controller;

import com.study.Util;
import com.study.config.MyHttpSessionListener;
import com.study.pojo.Blog;
import com.study.pojo.User;
import com.study.service.BlogService;
import com.study.service.UserService;
import com.study.util.FileUploadUtil;
import com.study.util.RedisUtil;
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


@Controller
public class UserController {


    @Autowired
    private Util util;
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;



    /*更新用户头像的操作*/
    @RequestMapping("/update_userHeadImg")
    @ResponseBody
    public Map<String,Object> updateUserHead(HttpSession session,@RequestParam("path")String path){
        Map<String,Object> map = new HashMap<>();
        String p = userService.updateUserHeadImg(session, path);
        if(p.equals("501")){
            map.put("code",501);
            map.put("msg","更新失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","更新成功");
        map.put("path",p);

        return map;
    }


/*获取邮箱注册码*/
    @RequestMapping("/email_code")
    @ResponseBody
    public Map<String,Object> registryCode(HttpSession session,@RequestParam("email") String email){
        Map<String,Object> map = new HashMap<>();
        System.out.println("email:"+email);
        String emailCode = util.getEmailCode(session,email);
        System.out.println(emailCode);
        map.put("code",200);
        map.put("msg","发送成功");
        return map;
    }

    /*用户注册 \
    * */
    @RequestMapping("/registry")
    @ResponseBody
    public Map<String,Object> registry(HttpSession session,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email,@RequestParam("email_code") String code){
        Map<String,Object> map = new HashMap<>();
        Integer i = userService.registryUser(session,username,password,email,code);
        if(i==501){
            System.out.println("验证码异常");
            map.put("code",501);
            map.put("msg","验证码过期");
            return map;
        }
        if(i==502){
            System.out.println("验证码错误");
            map.put("code",502);
            map.put("msg","验证码错误");
            return map;
        }

        toLogin(session,username,password);
        map.put("code",200);
        map.put("msg","注册成功");
        map.put("location_r","index");
        return map;
    }

    /*登录*/
    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(@RequestParam("username") String username,@RequestParam("password") String password, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User user = userService.loginUser(username, password);
        if(user==null){
            map.put("code",501);
            map.put("msg","用户名或密码错误");
            return map;
        }
        session.setAttribute("user",user);
        System.out.println(user);
        map.put("code",200);
        map.put("location_r","index");
        return map;
    }
    /*注销登录*/
    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:index";
    }

        /*文件上传*/
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String,Object> fileUpload(@RequestParam("file") MultipartFile file){
        Map<String,Object> map = new HashMap<>();
        String s = FileUploadUtil.fileUploadTitle(file,"title");
        if(s==null){
            map.put("msg","文件删除遇到问题，上传失败");
        }
        map.put("msg","文件上传成功");
        String[] split = s.split("/");
        String path = "http://localhost:8080/blogs/"+split[split.length-1];
        System.out.println(Arrays.toString(split));
        map.put("src",path);
        return map;
    }

    /*p判断用户名是否存在的接口*/
    @RequestMapping("/usernameIsRegistry")
    @ResponseBody
    public Map<String,Object> usernameExist(@RequestParam("username")String username){
        Map<String,Object> map =new HashMap<>();
        Integer integer = userService.usernameIsExist(username);
        if(integer==500){
            map.put("code",500);
            map.put("msg","用户名已存在");
            return map;
        }
        map.put("code",200);
        return map;
    }

    public void toLogin(HttpSession session,String username,String password){
        User user = userService.loginUser(username, password);
        session.setAttribute("user",user);
    }


    @RequestMapping("/findUserByid")
    @ResponseBody
    public User findUserById(@RequestParam("userId") Integer userId){
        User user = userService.findUserById(userId);
        return user;
    }

    @RequestMapping("/homePage_getLogInUser")
    @ResponseBody
    public User getLoginUset(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user;
    }

    /*关注用户的接口*/
    @RequestMapping("/attentionUser")
    @ResponseBody
    public Map<String,Object> attention(HttpSession session,@RequestParam("attentionUserId") Integer attentionUserId){
        Map<String,Object> map = new HashMap<>();
        Integer count = userService.addAttention(session, attentionUserId);
        if(count==500){
            map.put("code",500);
            map.put("msg","已经关注");
            return map;
        }
        if(count==501){
            map.put("code",501);
            map.put("msg","不可关注");
            return map;
        }
        map.put("code",200);
        map.put("msg","关注成功");
        return map;
    }

    /*判断当前对象是否被登录对象关注*/
    @RequestMapping("/findLoginUserIsAttentionUser")
    @ResponseBody
    public Map<String,Object> findLoginUserIsAttentionUser(HttpSession session,@RequestParam("userId")Integer userId){
        Map<String,Object> map = new HashMap<>();
        Integer count = userService.findLoginUserIsAttentionUser(session, userId);
        if(count==500){
            map.put("code",500);
            map.put("msg","已关注");
            return map;
        }else if(count==501){
            map.put("code",501);
            map.put("msg","自己");
            return map;
        }
        map.put("code",200);
        map.put("msg","未关注");
        return map;
    }

    /*根据用户id判断当前用户的身份*/
    @RequestMapping("/userIsVipUser")
    @ResponseBody
    public Map<String,Object> findUserIsVip(@RequestParam("userId") Integer userId){
        Map<String,Object> map = new HashMap<>();
        User user = userService.findUserById(userId);
        map.put("vip",user.getVip());
        return map;
    }

}
