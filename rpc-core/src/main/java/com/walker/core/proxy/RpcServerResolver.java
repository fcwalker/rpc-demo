package com.walker.core.proxy;

/**
 * 调用方法的解释器
 * 由服务提供方进行实现（服务提供方可根据项目开发环境自行实现）
 */
public interface RpcServerResolver {
    Object resolve(String serviceClassName);
}
