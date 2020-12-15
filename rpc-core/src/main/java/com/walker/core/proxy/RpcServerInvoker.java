package com.walker.core.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.walker.core.protocol.RpcProtoReq;
import com.walker.core.protocol.RpcProtoResp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcServerInvoker {
    private RpcServerResolver resolver;

    RpcServerInvoker(RpcServerResolver resolver) {
        this.resolver = resolver;
    }

    public RpcProtoResp invoke(RpcProtoReq protoReq) {
        RpcProtoResp protoResp = new RpcProtoResp();
        String serviceClass = protoReq.getClassName();
        String methodName = protoReq.getMethodName();
        Object[] params = protoReq.getParams();
        Object target = this.resolver.resolve(serviceClass);
        Method targetMethod = resolveMethodFromClass(target.getClass(), methodName, params);
        try {
            Object result = targetMethod.invoke(target, params);
            protoResp.setStatus(true);
            //对返回对象进行序列化
            protoResp.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            protoResp.setStatus(false);
            protoResp.setException(e);
        }
        return protoResp;
    }

    private Method resolveMethodFromClass(Class<?> kclass, String methodName, Object[] params) {
        return Arrays.stream(kclass.getMethods()).filter(m -> {
                    if (m.getName().equals(methodName)) {
                        if (null != params && m.getParameterCount() == params.length) {
                            Class<?>[] types = m.getParameterTypes();
                            if (Arrays.stream(types).allMatch(type ->
                                    Arrays.stream(params).anyMatch(param -> {
                                        if (type.equals(param.getClass())) {
                                            return true;
                                        }
                                        return false;
                                    })
                            )) {
                                return true;
                            }
                            return false;
                        }
                        return true;
                    }
                    return false;
                }
        ).findFirst().get();
    }
}
