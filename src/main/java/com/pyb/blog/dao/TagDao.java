package com.pyb.blog.dao;

import com.pyb.blog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/*实现jpa接口内有基本增删改查*/
public interface TagDao extends JpaRepository<Tag,Integer> {

    /*jpa接口查找方法的时候只有byid*/
    Tag findByName(String name);
}
