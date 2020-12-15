package com.walker.core.proxy;

import com.alibaba.fastjson.parser.ParserConfig;
import com.walker.core.protocol.RpcProtoReq;
import com.walker.core.protocol.RpcProtoResp;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author dell
 * @date 2020/12/15 16:52
 **/
public class RpcProxy {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url){
        return Proxy.newProxyInstance(RpcProxy.class.getClassLoader(), new Class[]{serviceClass}, )
    }

    public static class RpcInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;

        private final String url;

        public <T> RpcInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 传输数据格式（协议内容），供序列化使用
            RpcProtoReq rpcProtoReq = RpcProtoReq.builder().className(this.serviceClass.getName())
                    .methodName(method.getName())
                    .params(args).build();

            // 发起远程调用，并获取结果
            RpcProtoResp protoResp = call(rpcProtoReq, url);

            // 解析结果并返回
            return null;
        }

        // 远程调用
        public RpcProtoResp call(final RpcProtoReq protoReq, final String url) {
            RpcProtoResp protoResp;
            // 远程访问
            // 通过http方式
            protoResp = callByOkHttp(protoReq, url);
            return protoResp;
        }

        private RpcProtoResp callByOkHttp(RpcProtoReq protoReq, String url) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .
            client.newCall()
        }


    }
}
