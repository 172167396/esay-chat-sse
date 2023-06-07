package com.easychat.sse.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    private static final String SUCCESS_MSG = "操作成功";
    private static final String ERROR_MSG = "操作失败";

    public static <T> R<T> success(T data) {
        return new R<>(200, data, SUCCESS_MSG);
    }

    public static <T> R<T> success() {
        return success(null, SUCCESS_MSG);
    }


    public static <T> R<T> success(T data, String msg) {
        return new R<>(200, data, msg);
    }

    public static <T> R<T> fail(Integer code, T data, String msg) {
        return new R<>(code, data, msg);
    }

    public static <T> R<T> fail() {
        return fail(null, ERROR_MSG);
    }

    public static <T> R<T> fail(T data, String msg) {
        return new R<>(500, data, msg);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(500, null, msg);
    }

    public static <T> R<T> fail(Integer code, String msg) {
        return new R<>(code, null, msg);
    }

    public R(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

}