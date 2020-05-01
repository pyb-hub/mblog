package com.pyb.blog.controller.web;


import com.pyb.blog.domain.Comment;
import com.pyb.blog.domain.User;
import com.pyb.blog.service.IBlogService;
import com.pyb.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AboutController {

    @Autowired
    private ICommentService commentService;
    @Autowired
    private IBlogService blogService;
    /*取配置文件的值,属性注入用value，对象注入用autowired*/
    @Value("${comment.avatar}")
    private String avatar;


    /*登录前*/
    @GetMapping("/about/1")
    public String about1(){
        return "about_1";
    }

    @GetMapping("/about/2")
    public String about2(){
        return "about_2";
    }
    //提交评论（保存到数据库）
    @PostMapping("/web/comments")
    public String comment_do(Comment comment, HttpSession session) {

        /*设置对应回复显示的用户头像，设置评论昵称*/
        comment.setAvatar(avatar);

        /*前端的参数会封装为comment对象，自动创建它里面对应的属性：这里的ParentComments只有id，所以要重新保存父评论*/
        /*保存的评论的父评论值设置*/
        Long parentId = comment.getParentComment().getId();
        //如果存在父评论
        if (parentId != -1){
            comment.setParentComment(commentService.findById(parentId));
        } else {
            //数据库重置保存父评论设置为null，对应前端保存的-1，防止报错
            comment.setParentComment(null);
        }
        /*同理还要保存对应的Blog对象具体内容*/
        comment.setBlog(blogService.findById(0L));

        //判断是否为管理员.getUsername()
        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("管理员")){
            comment.setAdminComment(true);
        }else {
            comment.setAdminComment(false);
        }
        commentService.saveComment(comment);
        return "redirect:/comments";
    }
    /*ajax请求更新评论列表*/
    @GetMapping("/comments")
    public String fresh(Model model){
        /*根据博客返回博客下的评论内容*/
        model.addAttribute("comments",commentService.findAllCommentsByBlogId(0L));
        return "about_2 :: commentList";
    }



    /*登录后*/
    @GetMapping("/admin/about/1")
    public String admin_about1(){
        return "admin/admin_about_1";
    }
    @GetMapping("/admin/about/2")
    public String admin_about2(){
        return "admin/admin_about_2";
    }
    //提交评论（保存到数据库）
    @PostMapping("/web/admin_comments")
    public String comment_do_admin(Comment comment, HttpSession session) {

        /*前端的参数会封装为comment对象，自动创建它里面对应的属性：这里的ParentComments只有id，所以要重新保存父评论*/
        /*保存的评论的父评论值设置*/
        Long parentId = comment.getParentComment().getId();
        //如果存在父评论
        if (parentId != -1){
            comment.setParentComment(commentService.findById(parentId));
        } else {
            //数据库重置保存父评论设置为null，对应前端保存的-1，防止报错
            comment.setParentComment(null);
        }
        /*同理还要保存对应的Blog对象具体内容*/
        comment.setBlog(blogService.findById(0L));

        //判断是否为管理员.getUsername()
        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("管理员")){
            /*设置管理员用户头像*/
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            /*设置对应回复显示的用户头像，设置评论昵称*/
            comment.setAvatar(avatar);
            comment.setAdminComment(false);
        }
        commentService.saveComment(comment);
        return "redirect:/admin_comments";
    }
    /*ajax请求更新评论列表*/
    @GetMapping("/admin_comments")
    public String fresh_admin(Model model){
        /*根据博客返回博客下的评论内容*/
        model.addAttribute("comments",commentService.findAllCommentsByBlogId(0L));
        return "about_2 :: commentList";
    }

}
