package com.imodou.blog.service;

import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.pojo.Article;
import com.imodou.blog.pojo.Category;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    public void save(Article article, List<Integer> categorIds);

    public Article findByid(Long id);

    public PageResult<Article> findByPage(QueryPageBean<Article> queryPageBean);
}
