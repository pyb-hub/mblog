
package com.pyb.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
/*标注为切面*/
public class blogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*切入点方法*/

    @Pointcut("execution(* com.pyb.blog.controller.*.*(..))")
    public void log() {
    }


    @Before("log()")
    private void beforePrintLogger(JoinPoint joinPoint) {
        //logger.info("-----前置方法执行------");

        /*获得用户的url和ip,先获取页面的request对象*/

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        StringBuffer url = request.getRequestURL();
        String ip = request.getRemoteAddr();

        /*获取用户请求的切入点的方法和参数信息,通过内置对象jointpoint拿取
         * joinPoint.getSignature()获取相关切入点方法的信息；getDeclaringTypeName为类名；getName()为方法名
         * joinPoint.getArgs()为获得方法参数*/

        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("用户访问的URL : {}", url);
        logger.info("用户的ip : {}", ip);
        logger.info("用户请求的方法 : {}", method);
        logger.info("用户请求的参数 : {}", args);

    }
    @AfterReturning(pointcut= "log()",returning = "o")
    private void afterPrintLogger(Object o) {
        //logger.info("-----后置方法执行------");
        logger.info("返回内容：{}", o);
    }

    /*private void exceptionPrintLogger(){
        logger.info("-----异常方法执行------");
        System.out.println("-----异常方法执行------");
    }

    private void finalPrintLogger() {
        logger.info("-----最终方法执行------");
    }*/
}

