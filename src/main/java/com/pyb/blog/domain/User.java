package com.pyb.blog.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*用户类*/
@Entity
@Table(name = "t_user")
public class User {

    /*主键*/
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    /*头像*/
    private String avatar;
    /*类型：是否管理员*/
    private String type;

    @OneToMany(mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String avatar, String type) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.type = type;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
