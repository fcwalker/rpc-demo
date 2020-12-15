package com.walker.core.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.walker.core.enums.HttpMethodType;
import com.walker.core.protocol.RpcProtoReq;
import com.walker.core.protocol.RpcProtoResp;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理实现远程调用
 * @author dell
 * @date 2020/12/15 16:52
 **/
public class RpcClientProxy {

    static {
        ParserConfig.getGlobalInstance().addAccept("com.walker");
    }

    public static <T> T create(final Class<T> serviceClass, final String url, final HttpMethodType methodType) {
        return (T) Proxy.newProxyInstance(RpcClientProxy.class.getClassLoader(), new Class[]{serviceClass}, new RpcInvocationHandler(serviceClass, url, methodType));
    }

    public static class RpcInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;

        private final String url;

        private final HttpMethodType methodType;

        public <T> RpcInvocationHandler(Class<T> serviceClass, String url, HttpMethodType methodType) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.methodType = methodType;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 传输数据格式（协议内容），供序列化使用
            RpcProtoReq rpcProtoReq = RpcProtoReq.builder().className(this.serviceClass.getName())
                    .methodName(method.getName())
                    .methodType(methodType.getMethodType())
                    .params(args).build();

            // 发起远程调用，并获取结果
            RpcProtoResp protoResp = call(rpcProtoReq, url, methodType);

            // 异常情况处理
            if (!protoResp.isStatus()) {
                Exception exception = protoResp.getException();
                System.err.println(exception.getMessage());
                throw exception;
            }
            // 解析结果并返回
            return JSON.parse(protoResp.getResult());
        }

        // 远程调用
        public RpcProtoResp call(final RpcProtoReq protoReq, final String url, HttpMethodType methodType) {
            RpcProtoResp protoResp = null;
            // 远程访问
            // 通过http方式
            try {
                protoResp = callByOkHttp(protoReq, url, methodType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return protoResp;
        }

        private RpcProtoResp callByOkHttp(RpcProtoReq protoReq, String url, HttpMethodType methodType) throws IOException {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            if (HttpMethodType.GET.equals(methodType)) {
                request.newBuilder().get().build();
            }
            String respJson = client.newCall(request).execute().body().string();
            return JSON.parseObject(respJson, RpcProtoResp.class);
        }


    }
}
