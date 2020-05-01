package com.pyb.blog.dao;

import com.pyb.blog.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*JpaSpecificationExecutor为复杂分页查询的接口，service中该方法要实现topredict方法，自己定义多个查询条件*/
public interface BlogDao extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog> {

    Blog findByTitleAndContent(String name,String content);
    /*首页根据浏览量从高到低查询num条,因为是从高到低的范围查询模板不存在，所以要自己定义一个分页查询，里面放置排序等条件*/
    @Query("select b from Blog b where b.published=true and b.recommend=true")
    List<Blog> findBlogs(Pageable pageable);
    /*首页查询的published=true的blogs的方法*/
    @Query("select b from Blog b where b.published=true")
    Page<Blog> findAllIndex(Pageable pageable);
    /*首页分页搜索published=true的blogs的方法,1代表第1个参数*/
    @Query("select b from Blog b where b.published=true and b.title like ?1 ")
    Page<Blog> findAllSearch(String search,Pageable pageable);

    /*所有用户：*/
    /*查找用户所有有更新博客的年份,别名的使用，要看执行的顺序，group 后不能用year别名*/
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published=true group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findYears();
    /*根据查找年份查找博客*/
    @Query("select b from Blog b where b.published=true and function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
    @Query("select count(b.id) from Blog b where b.published=true")
    Long countBlogs();
    /*一个用户：*/
    /*查找用户所有有更新博客的年份,别名的使用，要看执行的顺序，group 后不能用year别名*/
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published=true and b.user.id=?1 group by function('date_format',b.updateTime,'%Y') order by year desc")
    List<String> findYearsOne(Long userId);
    /*根据查找年份查找博客*/
    @Query("select b from Blog b where b.published=true and function('date_format',b.updateTime,'%Y') = ?1 and b.user.id=?2")
    List<Blog> findByYearOne(String year,Long userId);
    /*计算某个user下面的blog数*/
    @Query("select count(b.id) from Blog b where b.published=true and b.user.id = ?1")
    Long countBlogsByUser(Long userId);

}
