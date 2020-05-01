package com.pyb.blog.service;

import com.pyb.blog.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITagService {

    /*crud*/
    /*增*/
    Tag saveTag(Tag tag);

    /*删除*/
    void delete(Tag tag);

    void deleteById(Long Long);

    /*查询*/
    Tag findTag(Long id);
    Page<Tag> TagList(Pageable pageable);
    List<Tag> TagList();
    /*通过前端表单传来的ids字符串获取Tag对象集合*/
    List<Tag> TagList(String ids);
    /*查询几个tag*/
    List<Tag> TagList(Integer num);

    /*修改*/
    Tag update(Long id, Tag tag);
}
