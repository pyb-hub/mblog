package com.pyb.blog.controller;

import com.pyb.blog.domain.User;
import com.pyb.blog.service.impl.UserImpl;
import com.pyb.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    private UserImpl userService;

    /*没登录前端的界面*/
    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/blog")
    public String blog(){
        return "blog";
    }
    //注册
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    /*登录*/
    @GetMapping("/login")
    public String login(){
        return "login";
    }




    @GetMapping("/archive")
    public String archive(){
        return "archives";
    }
    @GetMapping("/about/01")
    public String about1(){
        return "about/01";
    }
    @GetMapping("/about/02")
    public String about2(){
        return "about/02";
    }


    /*业务处理*/
    /*处理登录，服务端采用MD5加密存储密码*/
    @PostMapping("/login_do")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username,MD5Utils.code(password));
        if (user != null){
            /*防止把密码传输过去*/
            user.setPassword(null);
            session.setAttribute("user",user);
            return "redirect:/admin/types_manage";/*index*//**/
        }
        attributes.addFlashAttribute("msg","用户名或者密码错误!");
        return "redirect:/login";
    }
    /*登出*/
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login";
    }
    /*处理注册*/
    @PostMapping("/register_do")
    public String register(@RequestParam String username,@RequestParam String password, RedirectAttributes attributes) {
        /*对密码进行MD5加密，在服务端存储的密码为加密后的密码*/
        boolean result = userService.saveUser(username, MD5Utils.code(password),"https://picsum.photos/id/1005/100/100");
        if (result){
            /*存放注册成功请登录消息*/
            attributes.addFlashAttribute("msg","注册成功！");
            return "redirect:/login";
        }
        attributes.addFlashAttribute("message","来晚一步，用户名已经被注册啦~");
        return "redirect:/register";
    }


    /*登录后各种页面*/
    @GetMapping("/admin/blog_input")
    public String blog_input(){
        return "/admin/blog_input";
    }
    /*登录后各种页面*/
    @GetMapping("/admin/blogs_manage")
    public String blog_manage(){
        return "/admin/blogs_manage";
    }
    /*登录后各种页面*/
    @GetMapping("/admin/index")
    public String admin_index(){
        return "/admin/index";
    }
    @GetMapping("/admin/index1")
    public String admin_index1(){
        return "/admin/index1";
    }


}
