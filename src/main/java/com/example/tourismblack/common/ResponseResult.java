package com.example.tourismblack.common;

public class ResponseResult<T> {
    private int code;
    private T data;
    
    private ResponseResult(int code, T data) {
        this.code = code;
        this.data = data;
    }
    
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, data);
    }
    
    public static <T> ResponseResult<T> error(int code, T data) {
        return new ResponseResult<>(code, data);
    }
    
    public int getCode() {
        return code;
    }
    
    public T getData() {
        return data;
    }
}