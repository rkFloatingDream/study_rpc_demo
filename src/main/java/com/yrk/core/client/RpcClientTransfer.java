package com.yrk.core.client;


import com.yrk.core.rpc_protocol.RpcRequest;
import com.yrk.core.rpc_protocol.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// 传入protocol层的RpcRequest，输出protocol层的RpcResponse
public class RpcClientTransfer {

    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket("localhost", 9000)) {
            // 发送【transfer层】
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            //将请求数据传输到输出流中与socket做交互与刷新流数据
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            //持续等待获取rpc服务端存入的流数据
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();

            return rpcResponse;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
