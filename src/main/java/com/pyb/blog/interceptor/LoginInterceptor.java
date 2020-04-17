package com.pyb.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    /*拦截没登录的用户 访问登录后页面*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*用户没有登录，拦截*/
        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
