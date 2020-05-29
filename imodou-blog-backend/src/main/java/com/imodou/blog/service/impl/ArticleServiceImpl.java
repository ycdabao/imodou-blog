package com.imodou.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.mapper.ArticleCategoryMapper;
import com.imodou.blog.mapper.ArticleMapper;
import com.imodou.blog.pojo.Article;
import com.imodou.blog.pojo.ArticleCategory;
import com.imodou.blog.pojo.Category;
import com.imodou.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleCategoryMapper articleCategoryMapper;

    @Override
    public void save(Article article, List<Integer> categorIds) {

        article.setCommentcount(0L);
        article.setPubdate(new Date());
        article.setModifydate(new Date());
        article.setLikecount(0L);
        article.setViews(0L);

        articleMapper.insertSelective(article);

        if(categorIds!=null){
            for (Integer categorId : categorIds) {
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setArticleId(article.getId());
                articleCategory.setCategoryId(new Long(categorId));
                articleCategoryMapper.insertSelective(articleCategory);
            }
        }

    }


    @Override
    public Article findByid(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult<Article> findByPage( QueryPageBean<Article> queryPageBean) {

        PageHelper.startPage(queryPageBean.getPageIndex(),queryPageBean.getPageSize());

        Example example = new Example(Article.class);

        Article article = queryPageBean.getData();
        if(article!=null){
            if(!StringUtils.isEmpty(article.getTitle())){
                example.createCriteria().andLike("title","%"+article.getTitle()+"%");
            }
        }

        example.setOrderByClause("pubdate desc");

        List<Article> articleList = articleMapper.selectByExample(example);


        PageInfo<Article> pageInfo = new PageInfo<>(articleList);

        PageResult<Article> pageResult= new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());

        return pageResult;
    }
}
