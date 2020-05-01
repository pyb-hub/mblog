package com.pyb.blog.service.impl;

import com.pyb.blog.dao.CommentDao;
import com.pyb.blog.domain.Comment;
import com.pyb.blog.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentImpl implements ICommentService {

    @Autowired
    CommentDao commentDao;

    @Override
    public List<Comment> findAllCommentsByBlogId(Long id) {
        /*返回所有的一级父类评论集合*/
        List<Comment> comments = commentDao.findCommentsByBlogIdAndParentCommentNull(id, Sort.by(Sort.Direction.ASC, "createTime"));
        //复制父类集合，存放修改后的评论对象，因为后面要修改子孙评论集合，所以防止破坏数据的原始结构；
        return copy(comments);
    }

    @Override
    public Comment findById(Long id) {
        return commentDao.getOne(id);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {

        comment.setCreateTime(new Date());
        return commentDao.save(comment);
    }

    //复制父类集合，并把所有的子孙评论，合并在二级子评论
    private List<Comment> copy(List<Comment> list){
        List<Comment> temp = new ArrayList<>();
        for (Comment c:list) {
            Comment comment = new Comment();
            /*把c复制到comment中，再存储在集合里*/
            BeanUtils.copyProperties(c,comment);
            temp.add(comment);
        }
        /*改造一级评论集合，把所有的子孙评论，合并在一级评论的二级子评论中*/
        mergeAll(temp);
        return temp;
    }

    private List<Comment> childComments = new ArrayList<>();
    //合并每一个父类comment所有的子孙评论放入comment对象下的replyList，作为子孙评论
    /*root为全部的父类评论的集合*/
    private void mergeAll(List<Comment> root){
        for (Comment father : root) {
            /*把每一个father的子孙评论合并为同一级级评论*/
            mergeOne(father);
            /*把临时集合中的所有评论存在father的子评论中，并清空临时集合*/
            father.setChildComments(childComments);
            childComments = new ArrayList<>();
        }
    }
    /*递归取得一个评论下所有的子孙评论：dfs：图的深度优先遍历，所以最后页面评论的顺序是深度优先遍历的顺序：广度优先遍历顺序很奇怪*/
    private void mergeOne(Comment father){
        if (father.getChildComments().size() > 0){
            for (Comment child : father.getChildComments()) {
                childComments.add(child);
                if (child.getChildComments().size() > 0){
                    mergeOne(child);
                }
            }
        }
    }
}
