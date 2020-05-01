package com.pyb.blog.controller.web;

import com.pyb.blog.domain.User;
import com.pyb.blog.service.IUserService;
import com.pyb.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Random;


@Controller
public class UserController {

    @Autowired
    private IUserService userService;

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

    /*业务处理*/
    /*处理登录，服务端采用MD5加密存储密码*/
    @PostMapping("/login_do")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username,MD5Utils.code(password));
        if (user != null){
            /*防止把密码传输过去*/
            user.setPassword(null);
            session.setAttribute("user",user);
            return "redirect:/admin/blogs_manage";
        }
        attributes.addFlashAttribute("errormessage","用户名或者密码错误!");
        return "redirect:/login";
    }
    /*登出*/
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login";
    }
    //处理注册
    @PostMapping("/register_do")
    public String register(@RequestParam String username,@RequestParam String password,
                           @RequestParam String identityPassword, RedirectAttributes attributes) {

        /*先判断二次的密码输入是否一致*/
        if (!password.equals(identityPassword)){
            attributes.addFlashAttribute("errormessage","二次的密码输入不一致，请确认~");
            return "redirect:/register";
        }
        int id = new Random().nextInt(1000)+1;
        /*对密码进行MD5加密，在服务端存储的密码为加密后的密码*/
        boolean result = userService.saveUser(username, MD5Utils.code(password),"https://picsum.photos/id/" + id + "/100");
        if (result){
            /*存放注册成功请登录消息*/
            attributes.addFlashAttribute("sucmessage","注册成功！");
            return "redirect:/login";
        }
        attributes.addFlashAttribute("errormessage","来晚一步，用户名已经被注册啦~");
        return "redirect:/register";
    }
    /*处理注册 上面和下面的方法都可以，区别在于把参数封装为user对象，不用加@RequestParam注解，但是属性名要一一对应*//*
    @PostMapping("/register_do")
    public String register(User user, RedirectAttributes attributes) {
        *//*对密码进行MD5加密，在服务端存储的密码为加密后的密码*//*
        boolean result = userService.saveUser(user.getUsername(), MD5Utils.code(user.getPassword()),"https://picsum.photos/id/1005/100/100");
        if (result){
            *//*存放注册成功请登录消息*//*
            attributes.addFlashAttribute("sucmessage","注册成功！");
            return "redirect:/login";
        }
        attributes.addFlashAttribute("message","来晚一步，用户名已经被注册啦~");
        return "redirect:/register";
    }
*/



}
