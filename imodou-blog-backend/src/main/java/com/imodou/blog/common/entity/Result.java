package com.imodou.blog.common.entity;

import lombok.Data;

@Data
public class Result implements java.io.Serializable {

    private boolean flag;
    private String message;
    private Object data;

    public Result(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
}
