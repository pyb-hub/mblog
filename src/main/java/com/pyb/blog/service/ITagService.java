package com.pyb.blog.service;

import com.pyb.blog.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITagService {

    /*crud*/
    /*增*/
    Tag saveTag(Tag tag);

    /*删除*/
    void delete(Tag tag);

    void deleteById(Integer integer);

    /*查询*/
    Tag findTag(Integer id);
    Page<Tag> TagList(Pageable pageable);

    /*修改*/
    Tag update(Integer id, Tag tag);
}
