package com.pyb.blog.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*标签类*/
@Entity
@Table(name = "t_tag")
public class Tag {

    /*主键自增*/
    @Id
    @GeneratedValue
    private int id;
    private String name;

    /*关系被维护端指定map映射*/
    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs = new ArrayList<>();

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Tag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
