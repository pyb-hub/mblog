package com.pyb.blog.dao;

import com.pyb.blog.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long> {

    /*显示为一页，不分页显示评论,查找所有的一级评论*/
    List<Comment> findCommentsByBlogIdAndParentCommentNull(Long id, Sort sort);

    /*分页查询，带一个参数，要是多个参数，就要实现复杂分页查询的接口,舍去，最后展示不合理*/
    @Query("select c from Comment c where c.blog.id = ?1 ")
    Page<Comment> findAll(Long id, Pageable pageable);

}
