package client;

import com.yrk.IDL.rpc.server.HelloRpc;
import com.yrk.core.client.RpcClientProxy;

/**
 * @BelongsProject: study_RPC_Demo
 * @Author: ruikun
 * @CreateTime: 2022-10-12  10:13
 * @Description: TODO
 * @Version: 1.0
 */
public class TestClient {
    public static void main(String[] args) {
        //模拟远程调用方 将对象的创建交给动态代理
        //1.创建代理对象
        RpcClientProxy proxy=new RpcClientProxy();
        HelloRpc service = proxy.getService(HelloRpc.class);
        //2.通过代理对象去执行方法  进入invoke逻辑层分发远程 socket
        String yrk = service.HelloRpcTest("yrk");

        System.out.println(yrk);

    }
}
