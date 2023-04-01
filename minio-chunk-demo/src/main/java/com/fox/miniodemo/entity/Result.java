package com.fox.miniodemo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static com.fox.miniodemo.constant.HttpStatus.HTTP_INTERNAL_ERROR;
import static com.fox.miniodemo.constant.HttpStatus.HTTP_OK;


/**
 * 返回给前端的统一工具类
 *
 * @author 狐狸半面添
 * @create 2023-01-16 19:19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;

    }
    private Result(){}

    public static Result ok() {
        return new Result(HTTP_OK.getCode(), HTTP_OK.getValue());
    }


    public static Result ok(Object data) {
        return new Result(HTTP_OK.getCode(), HTTP_OK.getValue(), data);
    }

    public static Result ok(String msg, Object data) {
        return new Result(HTTP_OK.getCode(), msg, data);
    }

    public static Result error() {
        return new Result(HTTP_INTERNAL_ERROR.getCode(), HTTP_INTERNAL_ERROR.getValue());
    }

    public static Result error(String msg) {
        return new Result(HTTP_INTERNAL_ERROR.getCode(), msg);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }
    public static Result error(String msg, Object data) {
        return new Result(HTTP_INTERNAL_ERROR.getCode(), msg, data);
    }

    public static Result error(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
