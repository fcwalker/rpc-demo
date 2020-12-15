package com.walker.core.enums;

/**
 * @author dell
 * @date 2020/12/15 22:32
 **/
public enum HttpMethod {
    POST("POST"),
    GET("GET");
    private String methodType;

    HttpMethod(String methodType) {
        this.methodType = methodType;
    }

}
