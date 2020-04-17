package com.pyb.blog.service.impl;

import com.pyb.blog.dao.TypeDao;
import com.pyb.blog.domain.BlogNotFoundException;
import com.pyb.blog.domain.Type;
import com.pyb.blog.service.ITypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TypeImpl implements ITypeService {

    @Autowired
    TypeDao typeDao;

    /*保存修改一般都要传入type*/
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
    public Type update(Integer id, Type type) {

        if (typeDao.findById(id).isPresent()){
            Type type1 = typeDao.findById(id).get();
            /*type已经存在，有数据了，不再保存重复的标签*/
            if (typeDao.findByName(type.getName()) != null){
                return null;
            }
            /*把type的值赋值给type1*/
            BeanUtils.copyProperties(type,type1);
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
    public void deleteById(Integer id) {
        typeDao.deleteById(id);
    }

    /*查找*/
    @Transactional
    @Override
    public Type findType(Integer id) {
        /*或者typeDao.getOne()*/
        if (typeDao.findById(id).isPresent()){
            /*默认返回一个集合*/
            return typeDao.findById(id).get();
        }
        return null;

    }

    @Transactional
    @Override
    public Page<Type> TypeList(Pageable pageable) {
        return typeDao.findAll(pageable);
    }


}
