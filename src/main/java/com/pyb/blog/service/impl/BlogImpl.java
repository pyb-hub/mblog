package com.pyb.blog.service.impl;

import com.pyb.blog.dao.BlogDao;
import com.pyb.blog.domain.*;
import com.pyb.blog.service.IBlogService;
import com.pyb.blog.util.MarkdownUtils;
import com.pyb.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogImpl implements IBlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    /*分页查询所有*/
    public Page<Blog> blogList(Pageable pageable) {
        return blogDao.findAll(pageable);
    }

    @Override
    /*分页查询所有published=true的blog*/
    public Page<Blog> blogListIndex(Pageable pageable) {
        return blogDao.findAllIndex(pageable);
    }

    @Override
    public Page<Blog> blogSearch(String search, Pageable pageable) {
        return blogDao.findAllSearch(search,pageable);
    }

    /*多表查询用复杂查询：tag和blog是多对多*/
    @Override
    public Page<Blog> blogList(Long tagId, Pageable pageable) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                /*存放查询条件的集合*/
                List<Predicate> predicates = new ArrayList<>();
                /*查询条件1.：是否发布*/
                predicates.add(cb.equal(root.<Boolean>get("published"),true));

                /*查询blog中的tags的一个tag对应的全部blog；参数写blog里面的多的属性：tags，（blog属性里面的tags是集合，所以不能直接id=tgaId）*/
                Join join = root.join("tags");
                /*返回查询结果只要blog里面的tags集合中某一个tag的id属性和tagId相等的符合条件的所有blog*/
                predicates.add(cb.equal(join.get("id"),tagId));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    /*分页根据传入的条件查询，后台的博客查询页面*/
    public Page<Blog> blogList(Pageable pageable,BlogQuery blog) {

        /*那个复杂查询接口实现的方法，第一个参数为查询条件的动态组合,第二个为pageable，顺序不能换*/
        return blogDao.findAll(new Specification<Blog>() {

            /*root为数据库所有的blog*/
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                /*存放查询条件的集合*/
                List<Predicate> predicates = new ArrayList<>();

                //首先要是自己的user对应的blog，才能操作（在controller设置管理员的id=-1）；
                if (blog.getUserId() != null && blog.getUserId()!=-1){
                    /*user不是为管理员，只能查看自己的blog*/
                    predicates.add(cb.equal(root.<User>get("user").get("id"),blog.getUserId()));
                }

                /*第一个框的查询条件：标题*/
                if (blog.getTitle() != null && !"".equals(blog.getTitle())){
                    predicates.add(cb.like(root.<String>get("title"),"%"+ blog.getTitle() +"%"));
                }
                /*第二个框的查询条件：类别*/
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                /*第三个框的查询条件：是否推荐*/
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                /*第四个框的查询条件：是否发布*/
                if (blog.isPublished()){
                    predicates.add(cb.equal(root.<Boolean>get("published"),blog.isPublished()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
            }
        },pageable);
    }

    @Override
    public Blog findById(Long id) {
        return blogDao.getOne(id);
    }

    @Transactional
    /*根据id查询，返回后把content转化html语法*/
    @Override
    public Blog findBlogAndConvert(Long id) {
        Blog blog = blogDao.getOne(id);
        /*博客浏览数加1*/
        blog.setView(blog.getView()+1);
        blogDao.save(blog);
        /*把markdown内容转为html语法，前端展示的是blog1*/
        Blog blog1 = new Blog();
        /*把前面的copy到后面的对象*/
        BeanUtils.copyProperties(blog,blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return blog1;
    }

    /*根据浏览量从高到低查询*/
    @Override
    public List<Blog> blogList(Integer num) {
        /*参数：第一个page是展示出查询结果的第几页，第二个，每一页的blog条数，第三个排序的对象:倒序，根据view*/
        Pageable pageable = PageRequest.of(0,num,Sort.by(Sort.Direction.DESC,"view"));
        return blogDao.findBlogs(pageable);
    }

    /*查找所有用户所有的活跃年份published的博客*/
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findYears();
        Map<String, List<Blog>> blogMap = new LinkedHashMap<>();
        for (String year : years) {
            blogMap.put(year,blogDao.findByYear(year));
        }
        return blogMap;
    }

    /*查询所有的用户的published的博客数*/
    @Override
    public Long countBlog() {
        return blogDao.countBlogs();
    }


    /*查找一个用户所有的活跃年份的published博客*/
    @Override
    public Map<String, List<Blog>> archiveBlogOne(Long userId) {
        List<String> years = blogDao.findYearsOne(userId);
        Map<String, List<Blog>> blogMap = new LinkedHashMap<>();
        for (String year : years) {
            blogMap.put(year,blogDao.findByYearOne(year,userId));
        }
        return blogMap;
    }

    /*查询一个用户的published博客数*/
    @Override
    public Long countBlogByUser(Long userId) {
        return blogDao.countBlogsByUser(userId);
    }

    @Transactional
    @Override
    /*保存到数据库*/
    public Blog save(Blog blog) {
        blog.setView(0);
        /*传入数据库的时候有八小时时差，注意在配置文件中设置GTM+8*/
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        /*blog内容允许重复，不做重复验证*/
        return blogDao.save(blog);
    }

    @Transactional
    @Override
    /*更新*/
    public Blog update(Long id, Blog blog) {

        if (!blogDao.findById(id).isPresent()){
            throw new BlogNotFoundException("该博客不存在~");
        }
        blog.setUpdateTime(new Date());
        Blog blog1 = blogDao.findById(id).get();
        /*把前面的参数赋值给后面的参数,过滤属性为空的属性*/
        BeanUtils.copyProperties(blog,blog1,MyBeanUtils.getNullPropertyNames(blog));
        /*把更新后的值存到数据库中,更新同一个id的值会覆盖*/
        return blogDao.save(blog1);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        blogDao.deleteById(id);
    }
}
