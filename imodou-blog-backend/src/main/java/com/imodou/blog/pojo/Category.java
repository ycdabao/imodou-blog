package com.imodou.blog.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name="t_category")
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private String alias;
    private String description;
    private Long parentid;
    private Integer sort;
    private String linkurl;

    private List<Category> children;
}
