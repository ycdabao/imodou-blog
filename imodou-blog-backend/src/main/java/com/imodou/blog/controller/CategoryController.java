package com.imodou.blog.controller;

import com.imodou.blog.common.constant.MessageConstant;
import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.common.entity.Result;
import com.imodou.blog.pojo.ArticleCategory;
import com.imodou.blog.pojo.Category;
import com.imodou.blog.service.ArticleService;
import com.imodou.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/tree")
    public List<Map> getTree(){
        List<Map> tree = categoryService.getTree();
        return tree;
    }

    @PostMapping("/findpage")
    public Result findPage( @RequestBody  QueryPageBean<Category> queryPageBean){
        try {
            PageResult<Category> page = categoryService.findPage(queryPageBean);
            return new Result(true, MessageConstant.CATEGORY_GET_SUCCESS,page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.CATEGORY_GET_FAIL);
    }

    @PostMapping
    public Result save(@RequestBody Category category){
        try {
            categoryService.save(category);
            return new Result(true,MessageConstant.ADD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ADD_ERROR);
    }

    @GetMapping("/article/{id}")
    public Result findCategriesByArticleId(@PathVariable("id") Integer id){
        try {
            List<ArticleCategory> categries = categoryService.findCategriesByArticleId(id);
            List<Long> categryIds = new ArrayList<>();
            if (categries != null) {
                for (ArticleCategory categry : categries) {
                    categryIds.add(categry.getCategoryId());
                }
            }

            return new Result(true, MessageConstant.QUERY_SUCCESS, categryIds);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new Result(false,MessageConstant.QUERY_ERROR);
    }
}
