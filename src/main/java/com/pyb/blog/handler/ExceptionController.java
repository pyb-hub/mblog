package com.pyb.blog.handler;
/*自定义拦截器，拦截异常，返回到自己定义的错误页面*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/*注解：能拦截所有带有controller注解的类的方法中的异常，返回到自己定义的error页面,
除了少量的自己定义的有状态码的异常抛出去，给springboot解决*/
@ControllerAdvice
public class ExceptionController {

    /*获取日志对象，需要把异常输出到日志中*/
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*参数：request用来获取请求的url信息；exception用来获取异常信息*/
    @ExceptionHandler(Exception.class)/*表示这个方法是拦截异常的方法，能拦截Exception类的异常*/
    public ModelAndView handlerException(HttpServletRequest request,Exception e) throws Exception {
        /*把请求的url和异常记住到日志中*/
        logger.error("Request URL : {}, Exception : {}",request.getRequestURL(),e);

        /*判断，一些有标识状态码的异常，不捕获，抛出来*/
        if (AnnotationUtils.findAnnotation(e.getClass(),ResponseStatus.class) != null){
            throw e;
        }
        /*把异常存在ModelAndView，输出到错误页面中*/
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        /*返回到哪个页面*/
        if (request.getSession().getAttribute("user") != null){
            mv.setViewName("error/admin_error");
        }else {
            mv.setViewName("error/error");
        }
        return mv;
    }
}
