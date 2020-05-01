package com.pyb.blog.controller.web;

import com.pyb.blog.domain.BlogQuery;
import com.pyb.blog.domain.Type;
import com.pyb.blog.service.IBlogService;
import com.pyb.blog.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private ITypeService typeService;

    @Autowired
    private IBlogService blogService;

    //登录前分类的页面
    @GetMapping("/types/{id}")
    public String type(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model){
        /*初始化，总共的分类种类，之前首页定义过的根据博客数排序取每一页10000000个，为了全部展示在一页*/
        List<Type> types = typeService.TypeList(1000000);
        model.addAttribute("types", types);
        /*定义从导航栏进入的时候id=-1，展示第一个type下的blog*/
        if (id == -1){
            id = types.get(0).getId();
        }
        /*某个分类对应的blog列表*/
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        blogQuery.setPublished(true);
        model.addAttribute("page",blogService.blogList(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "types";
    }


    //登录后分类的页面
    @GetMapping("/admin/types/{id}")
    public String admin_type(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable Long id, Model model){

        /*初始化，总共的分类种类，之前首页定义过的根据博客数排序取每一页10000000个，为了全部展示在一页*/
        List<Type> types = typeService.TypeList(1000000);
        model.addAttribute("types", types);
        /*定义从导航栏进入的时候id=-1，展示第一个type下的blog*/
        if (id == -1){
            id = types.get(0).getId();
        }
        /*某个分类对应的blog列表*/
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        blogQuery.setPublished(true);
        model.addAttribute("page",blogService.blogList(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "admin/admin_types";
    }

}
