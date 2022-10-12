package server;

import com.yrk.core.server.RpcServer;
import impl.HelloRpcImpl;

/**
 * @BelongsProject: study_RPC_Demo
 * @Author: ruikun
 * @CreateTime: 2022-10-12  10:09
 * @Description: TODO
 * @Version: 1.0
 */
public class TestServer {

    public static void main(String[] args) {
        //首先需要将对象注册到rpc中心
        //1.创建rpc服务中心
        RpcServer rpcServer=new RpcServer();
        //2.将对象实例注册到服务中心
        HelloRpcImpl impl=new HelloRpcImpl();
        rpcServer.register(impl);
        //开启socket
        rpcServer.serve(9000);

    }
}
