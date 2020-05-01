package com.pyb.blog.dao;

import com.pyb.blog.domain.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*实现jpa接口内有基本增删改查*/
public interface TypeDao extends JpaRepository<Type,Long> {

    /*方法库查找方法的时候只有byid*/
    Type findByName (String name);

    /*自定义方法，根据type里面的List<blog>.size来排序,用pageable来实现排序*/
    @Query("select t from Type t")
    List<Type> findAllByBlogSize(Pageable pageable);
}
