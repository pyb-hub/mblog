package com.pyb.blog.dao;

import com.pyb.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*实现jpa接口内有基本增删改查*/
public interface UserDao extends JpaRepository<User,Integer> {

    User findByUsernameAndPassword(String username,String password);
    User findByUsername(String username);

}
