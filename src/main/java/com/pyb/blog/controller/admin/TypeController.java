package com.pyb.blog.controller.admin;

import com.pyb.blog.domain.Tag;
import com.pyb.blog.domain.Type;
import com.pyb.blog.domain.User;
import com.pyb.blog.service.ITypeService;
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
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private ITypeService typeImpl;


    /*个人中心页面分类管理页面*/
    /*访问后直接进行业务逻辑初始化处理，把所有分类数据返回到前端*/
    @GetMapping("/types_manage")
    public String types_manage(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable
    , HttpServletRequest request, HttpSession session){
        /*分页查询，根据数据库里面的id字段排序（可以多个值）,再调用service的TypeList获得分页的数据，传回前端页面*/
       request.setAttribute("page",typeImpl.TypeList(pageable));
       User user = (User) session.getAttribute("user");
       request.setAttribute("user",user.getType());
       return "admin/types_manage";
    }
    /*标签增加的编辑页面*/
    @GetMapping("/types_manage/input")
    public String input(){
        /*跳转的地址*/
        return "admin/types_manage-input";
    }
    /*增加分类的编辑页面的增加表单提交的地址*/
    @PostMapping("/types_add")
    public String addType(Type type, RedirectAttributes attributes){
        Type saveType = typeImpl.saveType(type);
        if (saveType != null){
            attributes.addFlashAttribute("sucmessage","提交成功！");
        }else {
            attributes.addFlashAttribute("errormessage","类别已存在，提交失败~ ");
        }

        return "redirect:/admin/types_manage";
    }

    /*修改编辑按钮的跳转地址*/
    @GetMapping("/types_manage/editor/{id}")
    public String editor(@PathVariable Long id, Model model){
        /*把id对应的type存入model中*/
        model.addAttribute("type",typeImpl.findType(id));
        /*跳转的地址*/
        return "admin/types_manage-editor";
    }
    /*修改编辑页面重新编辑的表单提交的地址*/
    @PostMapping("/types_editor/{id}")
    public String editorType(Type type, @PathVariable Long id,RedirectAttributes attributes,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user.getType().equals("管理员")){
            Type updateType = typeImpl.update(type.getId(),type);
            if (updateType != null){
                attributes.addFlashAttribute("sucmessage","修改成功！");
            }else {
                attributes.addFlashAttribute("errormessage","类别已存在，修改失败~ ");
            }
        }else {
            /*不是管理员*/
            attributes.addFlashAttribute("errormessage","需要管理员权限，修改失败~ ");
        }

        return "redirect:/admin/types_manage";
    }
    /*删除的逻辑处理*/
    @GetMapping("/types_manage/del/{id}")
    public String del(@PathVariable Long id,HttpSession session,RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        if (user.getType().equals("管理员")){
            /*管理员*/
            typeImpl.deleteById(id);
            attributes.addFlashAttribute("sucmessage","删除成功！");
        }else {
            /*不是管理员*/
            attributes.addFlashAttribute("errormessage","需要管理员权限，删除失败~ ");
        }
        /*跳转的地址*/
        return "redirect:/admin/types_manage";
    }





}
