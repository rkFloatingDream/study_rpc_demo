package com.yrk.core.server;


import com.yrk.core.codec.RpcRequestBody;
import com.yrk.core.rpc_protocol.RpcRequest;
import com.yrk.core.rpc_protocol.RpcResponse;
import com.yrk.core.utils.Utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class RpcServerWorker implements Runnable{

    private Socket socket;
    private HashMap<String, Object> registeredService;

    public RpcServerWorker(Socket socket, HashMap<String, Object> registeredService) {
        //得到当前socket的对象引用 以及所有注册在rpc服务端的实例对象
        this.socket = socket;
        this.registeredService = registeredService;
    }

    @Override
    public void run() {
        try {

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            //1.拿取客户端放进流中的请求对象
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            RpcRequestBody body=(RpcRequestBody)Utils.bytesToObject(rpcRequest.getBody());
            //获取注册在rpc的实例对象
            Object server = registeredService.get(body.getInterfaceName());
            //根据方法名以及方法参数类型获取到方法实例
            Method method = server.getClass().getMethod(body.getMethodName(), body.getParamTypes());
            //根据代理调用实际方法
            Object returnObject = method.invoke(server, body.getParameters());

            //将方法的返回转成字节数组
            byte[] responseBody = Utils.ObjectToByte(returnObject);
            //协议体
            String hear="1";

            //封装返回
            RpcResponse rpcResponse = RpcResponse.builder().header(hear).body(responseBody).build();

            //将数据重新丢回socket的流对象中
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
