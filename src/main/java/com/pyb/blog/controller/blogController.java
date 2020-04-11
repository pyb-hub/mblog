package com.pyb.blog.controller;

import com.pyb.blog.domain.BlogNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class blogController {

    @RequestMapping("/")
    public String test() {
        //int i = 10/0;

        String blog = null;
        if (blog == null){
            throw new BlogNotFoundException("博客不存在~");
        }
        return "index";
    }

    @RequestMapping("/log")
    @ResponseBody
    public String testlog(String msg) {
        System.out.println("testlog执行");
        return msg;
    }
}
