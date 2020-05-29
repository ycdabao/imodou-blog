package com.imodou.blog.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="t_article")
public class Article {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String title;
    private String summary;
    private String content;
    private Long viewscount;
    private Long commentcount;
    private Date pubdate;
    private Date modifydate;
    private Long likecount;
    private Long userid;
    private String coverphoto;
    private int status;

}
