package com.pyb.blog.dao;

import com.pyb.blog.domain.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*实现jpa接口内有基本增删改查*/
public interface TagDao extends JpaRepository<Tag,Long> {

    /*jpa接口查找方法的时候只有byid*/
    Tag findByName(String name);

    /*自定义方法，根据tag里面的List<blog>.size来排序,用pageable来实现排序*/
    @Query("select t from Tag t")
    List<Tag> findAllByBlogSize(Pageable pageable);
}
