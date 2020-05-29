package com.imodou.blog.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="t_article_category")
public class ArticleCategory {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long articleId;
    private Long categoryId;
}
