package com.walker.core.proxy.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.walker.core.protocol.RpcProtoReq;
import com.walker.core.protocol.RpcProtoResp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author fcwalker 服务提供者根据调用方法的协议
 */
public class RpcServerInvoker {
    private RpcServerResolver resolver;

    public RpcServerInvoker(RpcServerResolver resolver) {
        this.resolver = resolver;
    }

    public RpcProtoResp invoke(final RpcProtoReq protoReq) {
        RpcProtoResp protoResp = new RpcProtoResp();
        String serviceClass = protoReq.getClassName();
        String methodName = protoReq.getMethodName();
        Object[] params = protoReq.getParams();
        Object target = this.resolver.resolve(serviceClass);
        System.out.println(target.getClass().getName());
        Method targetMethod = resolveMethodFromClass(target.getClass(), methodName, params);
        try {
            Object result = targetMethod.invoke(target, params);
            protoResp.setStatus(true);
            // 对返回对象进行序列化
            protoResp.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            protoResp.setStatus(false);
            protoResp.setException(e);
        }
        return protoResp;
    }

    private Method resolveMethodFromClass(final Class<?> kclass, final String methodName,
            final Object[] params) {
        return Arrays.stream(kclass.getMethods())
                .filter(m -> compareMethod(m, methodName, params))
                .findFirst().get();
    }

    boolean compareMethod(final Method sourceMethod, final String targetMethodName,
            final Object[] params) {
        if (!sourceMethod.getName().equals(targetMethodName)) {
            return false;
        }
        if (null != params && params.length > 0) {
            return compareMethodWithParams(sourceMethod, params);
        }
        return true;
    }

    boolean compareMethodWithParams(final Method sourceMethod, final Object[] params) {
        int paramsCount = sourceMethod.getParameterCount();
        if (paramsCount != params.length) {
            return false;
        }
        return Arrays.asList(sourceMethod.getParameterTypes()).stream()
                .allMatch(typeCls -> compareMethodParamType(typeCls, params));
    }

    boolean compareMethodParamType(final Class<?> typeCls, final Object[] params) {
        return Arrays.asList(params).stream()
                .anyMatch(param -> param.getClass().getName().equals(typeCls.getName()));
    }


}
