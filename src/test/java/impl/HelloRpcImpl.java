package impl;

import com.yrk.IDL.rpc.server.HelloRpc;

public class HelloRpcImpl implements HelloRpc {

    @Override
    public String HelloRpcTest(String name) {
        System.out.println("接口调用");
        return name+"模拟外部Rpc接口调用";
    }
}
