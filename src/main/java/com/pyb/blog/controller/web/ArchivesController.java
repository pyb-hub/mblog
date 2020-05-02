package com.pyb.blog.controller.web;

import com.pyb.blog.domain.User;
import com.pyb.blog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ArchivesController {

    @Autowired
    private IBlogService blogService;

    /*登录前:显示所有用户的博客归档*/
    @GetMapping("/archives")
    public String archive(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogsNum",blogService.countBlog());
        return "archives";
    }


    /*登录后：
    1.显示全站的博客归档*/
    @GetMapping("/admin/archives")
    public String admin_archive(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogsNum",blogService.countBlog());
        return "admin/admin_archives_1";
    }
    //2.显示自己的博客归档
    @GetMapping("/admin/archives/2")
    public String admin_MyArchive(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        Long userId = user.getId();
        model.addAttribute("archiveMap",blogService.archiveBlogOne(userId));
        model.addAttribute("blogsNum",blogService.countBlogByUser(userId));
        return "admin/admin_archives_2";
    }
}
