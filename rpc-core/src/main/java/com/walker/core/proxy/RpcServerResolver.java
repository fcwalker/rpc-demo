package com.walker.core.proxy;

public interface RpcServerResolver {
    Object resolve(String serviceClassName);
}
