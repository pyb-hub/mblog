package com.pyb.blog.controller.admin;

import com.pyb.blog.domain.Tag;
import com.pyb.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private ITagService tagImpl;

    /*个人中心页面分类管理页面*/
    /*访问后直接进行业务逻辑初始化处理，把所有分类数据返回到前端*/
    @GetMapping("/tags_manage")
    public String tags_manage(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable
    , HttpServletRequest request){
        /*分页查询，根据数据库里面的id字段排序（可以多个值）,再调用service的tagList获得分页的数据，传回前端页面*/
       request.setAttribute("page",tagImpl.TagList(pageable));
        return "/admin/tags_manage";
    }
    /*标签增加的编辑页面*/
    @GetMapping("/tags_manage/input")
    public String input(){
        /*跳转的地址*/
        return "/admin/tags_manage-input";
    }
    /*增加分类的编辑页面的增加表单提交的地址*/
    @PostMapping("/tags_add")
    public String addtag(Tag tag, RedirectAttributes attributes){
        Tag tag1 = tagImpl.saveTag(tag);
        if (tag1 != null){
            attributes.addFlashAttribute("sucmessage","提交成功！");
        }else {
            attributes.addFlashAttribute("errormessage","标签已存在，提交失败~ ");
        }

        return "redirect:/admin/tags_manage";
    }

    /*修改编辑按钮的跳转地址*/
    @GetMapping("/tags_manage/editor/{id}")
    public String editor(@PathVariable Long id, Model model){
        /*把id对应的tag存入model中*/
        model.addAttribute("tag",tagImpl.findTag(id));
        /*跳转的地址*/
        return "/admin/tags_manage-editor";
    }
    /*修改编辑页面重新编辑的表单提交的地址*/
    @PostMapping("/tags_editor/{id}")
    public String editortag(Tag tag, @PathVariable Long id,RedirectAttributes attributes){
        Tag updateTag = tagImpl.update(tag.getId(),tag);
        if (updateTag != null){
            attributes.addFlashAttribute("sucmessage","修改成功！");
        }else {
            attributes.addFlashAttribute("errormessage","标签已存在，修改失败~ ");
        }

        return "redirect:/admin/tags_manage";
    }
    /*删除的逻辑处理*/
    @GetMapping("/tags_manage/del/{id}")
    public String del(@PathVariable Long id){
        tagImpl.deleteById(id);
        /*跳转的地址*/
        return "redirect:/admin/tags_manage";
    }





}
