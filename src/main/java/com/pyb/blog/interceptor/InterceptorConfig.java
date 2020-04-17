package com.pyb.blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    /*指定要拦截的地址*/
    public void addInterceptors(InterceptorRegistry registry) {
        //LoginInterceptor要拦截的对象地址
        registry.addInterceptor(new LoginInterceptor())
                /*除了login和admin下面的地址其他的地址拦截*/
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/admin");
    }

    /*把静态资源不拦截*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**");
    }
}
