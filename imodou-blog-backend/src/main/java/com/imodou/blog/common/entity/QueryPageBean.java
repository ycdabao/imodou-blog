package com.imodou.blog.common.entity;

import lombok.Data;

@Data
public class QueryPageBean<T> {
    private Integer pageIndex;
    private Integer pageSize;
    private T data;
}
