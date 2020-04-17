package com.pyb.blog.service.impl;

import com.pyb.blog.dao.UserDao;
import com.pyb.blog.domain.User;
import com.pyb.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, password);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    @Transactional
    @Override
    public boolean saveUser(String username, String password, String avatar) {
        String type = "非管理员";
        User user = findByUsername(username);
        if (user != null){
            return false;
        }
        User u = new User(username,password,avatar,type);
        userDao.save(u);
        return true;
    }


}
