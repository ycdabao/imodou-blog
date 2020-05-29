package com.imodou.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.mapper.ArticleCategoryMapper;
import com.imodou.blog.mapper.CategoryMapper;
import com.imodou.blog.pojo.ArticleCategory;
import com.imodou.blog.pojo.Category;
import com.imodou.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Override
    public List<Map> getTree() {

        Example example  = new Example(Category.class);

        example.createCriteria().andIsNull("parentid");

        List<Category> categories = categoryMapper.selectByExample(example);
        List<Map> categoris_map = new ArrayList<>();
        for (Category category : categories) {
            Map map = new HashMap();
            map.put("label",category.getName());
            map.put("value",category.getId());
            categoris_map.add(map);

            buildTree(map);
        }

        return categoris_map;
    }


    private void buildTree(Map map){

        Example example  = new Example(Category.class);
        example.createCriteria().andEqualTo("parentid",map.get("value"));
        List<Category> categories = categoryMapper.selectByExample(example);
        List<Map> categoris_map = new ArrayList<>();
        for (Category category : categories) {
            Map map1 = new HashMap();
            map1.put("label",category.getName());
            map1.put("value",category.getId());
            categoris_map.add(map1);
        }
        if(categoris_map.size()>0){
            map.put("children",categoris_map);
        }

        for (Map category1 : categoris_map) {
            buildTree(category1);
        }

    }

    @Override
    public PageResult<Category> findPage( QueryPageBean<Category> queryPageBean) {

        PageHelper.startPage(queryPageBean.getPageIndex(),queryPageBean.getPageSize());

        Example example = new Example(Category.class);
        if(queryPageBean.getData().getName()!=null&&!"".equals(queryPageBean.getData().getName())) {
            example.createCriteria().andLike("name", "%"+queryPageBean.getData().getName()+"%");

        }
        List<Category> categoryList = categoryMapper.selectByExample(example);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);

        PageResult<Category> result = new PageResult<>();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());

        return result;
    }


    @Override
    public void save(Category category) {
        categoryMapper.insertSelective(category);
    }


    @Override
    public List<ArticleCategory> findCategriesByArticleId(Integer articleId) {
        Example example = new Example(ArticleCategory.class);
        example.createCriteria().andEqualTo("articleId",articleId);

        List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(example);

        return articleCategories;
    }
}
