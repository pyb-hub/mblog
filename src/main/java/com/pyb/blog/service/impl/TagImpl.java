package com.pyb.blog.service.impl;

import com.pyb.blog.dao.TagDao;
import com.pyb.blog.domain.Tag;
import com.pyb.blog.service.ITagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TagImpl implements ITagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public Tag saveTag(Tag tag) {
        /*如果数据库已存在tag就返回null*/
        Tag name = tagDao.findByName(tag.getName());
        if (name != null){
            return null;
        }else {
            return tagDao.save(tag);
        }

    }

    @Override
    public void delete(Tag tag) {
        tagDao.delete(tag);
    }

    @Override
    public void deleteById(Integer id) {
        tagDao.deleteById(id);
    }

    @Override
    public Tag findTag(Integer id) {
        return tagDao.getOne(id);
    }

    @Override
    public Page<Tag> TagList(Pageable pageable) {
        return tagDao.findAll(pageable);
    }

    @Override
    public Tag update(Integer id, Tag tag) {
        Tag byId = tagDao.getOne(id);
        if (tagDao.findByName(tag.getName()) != null){
            /*更新失败*/
            return null;
        }
        BeanUtils.copyProperties(tag,byId);
        return byId;
    }
}
