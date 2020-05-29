package com.imodou.blog.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> implements java.io.Serializable {

    private Long total;
    private List<T> rows;
}
