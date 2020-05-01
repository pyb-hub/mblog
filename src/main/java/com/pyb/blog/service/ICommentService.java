package com.pyb.blog.service;

import com.pyb.blog.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ICommentService {

    /*根据blog的id查找对应的评论*/
    List<Comment> findAllCommentsByBlogId(Long id);
    /*根据评论的id查找评论*/
    Comment findById(Long id);
    /*保存评论*/
    Comment saveComment(Comment comment);
}
