package com.yrk.core.client;


import com.yrk.core.codec.RpcRequestBody;
import com.yrk.core.rpc_protocol.RpcRequest;
import com.yrk.core.rpc_protocol.RpcResponse;
import com.yrk.core.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1、将调用所需信息编码成bytes[]，即有了调用编码【codec层】
        //1.1也就是说将 代理对象的接口名  方法名 参数类型 等作出对象的封装
        //method.getDeclaringClass().getName() 获取类名
        RpcRequestBody requestBody = RpcRequestBody.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramTypes(method.getParameterTypes())
                .parameters(args)
                .build();
        // 2、创建RPC协议，将Header、Body的内容设置好（Body中存放调用编码）【protocol层】
        //2.1模拟请求头
        String head="1";
        //2.2模拟请求体
        byte[] bytes = Utils.ObjectToByte(requestBody);
        RpcRequest rpcRequest=RpcRequest.builder()
                .header(head)
                .body(bytes)
                .build();
        // 3、发送RpcRequest，获得RpcResponse
        RpcClientTransfer clientTransfer=new RpcClientTransfer();
        RpcResponse rpcResponse = clientTransfer.sendRequest(rpcRequest);
        if (!"1".equals(rpcResponse.getHeader())) {
            //报错提示等等 直接返回
        }
        Object object =  Utils.bytesToObject(rpcResponse.getBody());
        // 4、解析RpcResponse，也就是在解析rpc协议【protocol层】

        return object;
    }

}
