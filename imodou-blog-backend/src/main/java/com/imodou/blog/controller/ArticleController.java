package com.imodou.blog.controller;

import com.imodou.blog.common.constant.MessageConstant;
import com.imodou.blog.common.entity.PageResult;
import com.imodou.blog.common.entity.QueryPageBean;
import com.imodou.blog.common.entity.Result;
import com.imodou.blog.pojo.Article;
import com.imodou.blog.service.ArticleService;
import com.imodou.blog.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    ArticleService articleService;

    @PostMapping("/upload")
    public Result uploadCoverphoto(MultipartFile file){
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + suffix;

            QiniuUtil.upload(file.getInputStream(), fileName);
            return new Result(true, MessageConstant.UPLOAD_IMAGE_SUCCESS,"http://cdn.imodou.com.cn/"+fileName);

        }catch(Exception ex){
            ex.printStackTrace();
            return new Result(false,MessageConstant.UPLOAD_IMAGE_FAIL);
        }
    }


    @PostMapping
    public Result saveArticle(@RequestBody Map<String,Object> map){

        String title = (String)map.get("title");
        String summary = (String)map.get("summary");
        List<Integer> categories = (List<Integer>)map.get("categories");
        String content = (String)map.get("content");
        String coverPhoto = (String)map.get("coverPhoto");

        Article article= new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setCoverphoto(coverPhoto);

        try {
            articleService.save(article,categories);
            return new Result(true,MessageConstant.ADD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ADD_ERROR);
    }


    @PostMapping("/findpage")
    public Result findByPage(@RequestBody QueryPageBean<Article> queryPageBean){
        try {
            PageResult<Article> pageResult = articleService.findByPage(queryPageBean);
            return new Result(true,MessageConstant.QUERY_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ERROR);
        }
    }


    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Long id){
        try {
            Article article = articleService.findByid(id);
            return new Result(true,MessageConstant.QUERY_SUCCESS,article);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ERROR);
        }
    }


}
