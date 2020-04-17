package com.pyb.blog.service;

import com.pyb.blog.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITypeService {

    /*crud*/
    /*增*/
    Type saveType(Type type);

    /*删除*/
    void delete(Type type);

    void deleteById(Integer integer);

    /*查询*/
    Type findType(Integer id);
    Page<Type> TypeList(Pageable pageable);

    /*修改*/
    Type update(Integer id,Type type);
}
