package com.pyb.blog.service.impl;

import com.pyb.blog.dao.TypeDao;
import com.pyb.blog.domain.BlogNotFoundException;
import com.pyb.blog.domain.Type;
import com.pyb.blog.service.ITypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeImpl implements ITypeService {

    @Autowired
    TypeDao typeDao;

    /*保存修改方法参数一般都要传入type*/
    /*保存*/
    @Transactional
    @Override
    public Type saveType(Type type) {
        if (typeDao.findByName(type.getName()) != null){
            /*已经有数据了，不再保存重复的标签*/
            return null;
        }
        return typeDao.save(type);
    }
    /*修改*/
    @Transactional
    @Override
    public Type update(Long id, Type type) {

        if (typeDao.findById(id).isPresent()){
            Type type1 = typeDao.findById(id).get();
            /*type已经存在，有数据了，不再保存重复的标签*/
            if (typeDao.findByName(type.getName()) != null){
                return null;
            }
            /*把type的值赋值给type1*/
            BeanUtils.copyProperties(type,type1);
            /*把更新后的值存到数据库中*/
            return typeDao.save(type1);

        }
        throw new BlogNotFoundException("找不到该类别~");
    }
    /*删除*/
    @Transactional
    @Override
    public void delete(Type type) {
        typeDao.delete(type);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        typeDao.deleteById(id);
    }

    /*查找*/
    @Override
    public Type findType(Long id) {
        /*或者typeDao.getOne()*/
        if (typeDao.findById(id).isPresent()){
            /*默认返回一个集合*/
            return typeDao.findById(id).get();
        }
        return null;

    }


    @Override
    public Page<Type> TypeList(Pageable pageable) {
        return typeDao.findAll(pageable);
    }

    @Override
    public List<Type> TypeList() {
        //System.out.println("进入typelist");
        return typeDao.findAll();
    }

    @Override
    public List<Type> TypeList(Integer num) {
        /*根据所属的博客数目从高到低列出num个tag*/
        /*参数：第一个page是展示出查询结果的第几页（从0开始），第二个，每一页的tag条数，第三个排序的方式:倒序，根据blogs.size*/
        Pageable pageable = PageRequest.of(0,num,Sort.by(Sort.Direction.DESC,"blogs.size"));
        return typeDao.findAllByBlogSize(pageable);
    }


}
