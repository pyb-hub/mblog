package com.pyb.blog.service;

import com.pyb.blog.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IBlogService {

    /*分页条件查询*/
    Page<Blog> blogList(Pageable pageable,BlogQuery blog);
    /*分页查询*/
    Page<Blog> blogList(Pageable pageable);
    /*首页分页查询，published=true的blog*/
    Page<Blog> blogListIndex(Pageable pageable);
    /*搜索框分页查询*/
    Page<Blog> blogSearch(String search, Pageable pageable);
    /*根据tag分页查询*/
    Page<Blog> blogList(Long tagId, Pageable pageable);
    /*通过id查询*/
    Blog findById(Long id);
    /*通过id查询，把content的markdown文本内容转化为html语法*/
    Blog findBlogAndConvert(Long id);
    /*根据浏览量从高到低查询*/
    List<Blog> blogList(Integer num);

    /*归档页，根据时间查询blog*/
    /*所有用户:*/
    Map<String,List<Blog>> archiveBlog();
    /*返回数据库所有的blog数目*/
    Long countBlog();

    /*一个用户:*/
    Map<String,List<Blog>> archiveBlogOne(Long userId);
    /*返回某个用户的数据库所有的blog数目*/
    Long countBlogByUser(Long userId);
    
    /*保存*/
    Blog save(Blog blog);

    /*修改*/
    Blog update(Long id,Blog blog);

    /*删除*/
    void delete(Long id);

}
