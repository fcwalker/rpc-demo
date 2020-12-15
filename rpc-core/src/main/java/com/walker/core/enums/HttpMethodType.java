package com.walker.core.enums;

/**
 * @author dell
 * @date 2020/12/15 22:32
 **/
public enum HttpMethodType {
    POST("POST"),
    GET("GET");
    private String methodType;

    HttpMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getMethodType() {
        return methodType;
    }
}
