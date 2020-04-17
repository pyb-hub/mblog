package com.pyb.blog.dao;

import com.pyb.blog.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/*实现jpa接口内有基本增删改查*/
public interface TypeDao extends JpaRepository<Type,Integer> {

    /*方法库查找方法的时候只有byid*/
    Type findByName (String name);
}
