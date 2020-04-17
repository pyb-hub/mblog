package com.pyb.blog.service;

import com.pyb.blog.domain.User;

public interface IUserService {
    User checkUser(String username,String password);
    User findByUsername(String username);
    boolean saveUser(String username,String password,String avatar);
}
