package com.imodou.blog.service;

import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.pojo.ArticleCategory;
import com.imodou.blog.pojo.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    public List<Map> getTree();

    public PageResult<Category> findPage(QueryPageBean<Category> queryPageBean);

    public void save(Category category);

    public List<ArticleCategory> findCategriesByArticleId(Integer articleId);
}
