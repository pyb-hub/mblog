package com.pyb.blog.service;

import com.pyb.blog.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITypeService {

    /*crud*/
    /*增*/
    Type saveType(Type type);

    /*删除*/
    void delete(Type type);

    void deleteById(Long id);

    /*查询*/
    Type findType(Long id);
    Page<Type> TypeList(Pageable pageable);
    List<Type> TypeList();
    /*查询几个tag*/
    List<Type> TypeList(Integer num);

    /*修改*/
    Type update(Long id,Type type);
}
