package com.fox.es_canal.entity;

import lombok.Data;

/**
 * 统一返回对象
 *
 * @author 狐狸半面添
 * @create 2023-03-22 18:34
 */
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    private Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result ok() {
        return new Result(200, "success");
    }

    public static Result ok(Object data) {
        return new Result(200, "success", data);
    }

    public static Result error(String msg) {
        return new Result(500, msg);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }

}
