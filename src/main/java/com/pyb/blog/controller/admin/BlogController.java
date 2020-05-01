package com.pyb.blog.controller.admin;

import com.pyb.blog.domain.Blog;
import com.pyb.blog.domain.BlogQuery;
import com.pyb.blog.domain.User;
import com.pyb.blog.service.IBlogService;
import com.pyb.blog.service.ITagService;
import com.pyb.blog.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private IBlogService blogImpl;

    @Autowired
    private ITagService tagImpl;

    @Autowired
    private ITypeService typeImpl;


    /*个人中心博客页面初始化分页展示*/
    @GetMapping("/blogs_manage")
    public String blog_manage(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              BlogQuery blog, Model model,HttpSession session){
        /*存入初始化数据*/
        model.addAttribute("types",typeImpl.TypeList());

        /*在这里service的方法上，加上查询条件里面的用户的限制*/
        User user = (User) session.getAttribute("user");
        if (user.getType().equals("管理员")){
            /*管理员可以查到所有的博客，userId设置为-1*/
            blog.setUserId(-1L);
        }else {
            /*不是管理员，设置查询条件里的userId限制*/
            blog.setUserId(user.getId());
        }

        /*存入分页查询的内容*/
        model.addAttribute("page",blogImpl.blogList(pageable,blog));

        return "admin/blogs_manage";
    }

    /*个人中心页面博客分页ajax搜索*/
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model,HttpSession session){
        /*在这里service的方法上，加上查询条件里面的用户的限制*/
        User user = (User) session.getAttribute("user");
        if (user.getType().equals("管理员")){
            /*管理员可以查到所有的博客，blog查询条件里面把userId设置为-1，不影响数据库的值*/
            blog.setUserId(-1L);
        }else {
            /*不是管理员，设置查询条件里的userId限制*/
            blog.setUserId(user.getId());
        }

        model.addAttribute("page",blogImpl.blogList(pageable,blog));

        return "admin/blogs_manage :: blogList";
    }

    /*个人中心页面博客发布页面*/
    @GetMapping("/blogs_manage-input")
    public String blog_input( HttpServletRequest request){
        /*存入初始化标签和type数据*/
        request.setAttribute("types",typeImpl.TypeList());
        request.setAttribute("tags",tagImpl.TagList());
        return "admin/blogs_manage-input";
    }

    /*个人中心页面博客发布提交地址*/
    @PostMapping("/blogs_add")
    public String blog_add(Blog blog , RedirectAttributes attributes, HttpSession session){
        /*设置博客的归属=session里面的user*/
        blog.setUser((User) session.getAttribute("user"));

        /*获取前端页面传来的type的id对应的type存入blog中*/
        blog.setType(typeImpl.findType(blog.getType().getId()));
        /*获取前端页面传来的tag的ids对应的tag存入blog中,ids用后端使用字符串和"，"拼接*/
        String tagIds = blog.getTagIds();
        blog.setTags(tagImpl.TagList(tagIds));

        /*把博客信息输出到数据库*/
        Blog blog1 = blogImpl.save(blog);
        if (blog1 != null){
            attributes.addFlashAttribute("sucmessage","提交成功！");
        }else {
            attributes.addFlashAttribute("errormessage","提交失败~ ");
        }

        /*发布成功跳转的页面*/
        return "redirect:/admin/blogs_manage";
    }

    /*博客修改页面*/
    @GetMapping("/blogs_manage-editor/{id}")
    public String editor(@PathVariable Long id, Model model){
        /*每个页面设置一次就行了，存入初始化标签和type数据*/
        model.addAttribute("types",typeImpl.TypeList());
        model.addAttribute("tags",tagImpl.TagList());

        /*原来的blog内容*/
        Blog blog = blogImpl.findById(id);
        /*为了把tagIds得到，本来为空，没有存入数据库*/
        blog.init();

        model.addAttribute("blog",blog);

        return "admin/blogs_manage-editor";
    }
    /*博客修改后提交的地方*/
    @PostMapping("/blogs_editor/{id}")
    public String editorblog( @PathVariable Long id, Blog blog, HttpSession session, RedirectAttributes attributes){

        /*设置博客的归属=session里面的user*/
        blog.setUser((User) session.getAttribute("user"));
        /*获取前端页面传来的type的id对应的type存入blog中*/
        blog.setType(typeImpl.findType(blog.getType().getId()));
        /*获取前端页面传来的tag的ids对应的tag存入blog中,ids用后端使用字符串和"，"拼接*/

        String tagIds = blog.getTagIds();
        blog.setTags(tagImpl.TagList(tagIds));

        /*把修改后的博客提交到数据库*/
        blogImpl.update(id,blog);
        attributes.addFlashAttribute("sucmessage","修改成功！");
        return "redirect:/admin/blogs_manage";
    }

    /*博客删除处理*/
    @GetMapping("/blogs_manage/del/{id}")
    public String blog_del(@PathVariable Long id, RedirectAttributes attributes){
        /*存入页面初始化标签和type数据*/
        attributes.addFlashAttribute("types",typeImpl.TypeList());
        attributes.addFlashAttribute("tags",tagImpl.TagList());
        /*删除操作*/
        blogImpl.delete(id);
        attributes.addFlashAttribute("sucmessage","删除成功！");
        return "redirect:/admin/blogs_manage";
    }
}
