package com.yrk.core.codec;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
// 调用编码
public class RpcRequestBody implements Serializable {
    //接口名称
    private String interfaceName;
    //方法名称
    private String methodName;
    //方法参数
    private Object[] parameters;
    //参数类型
    private Class<?>[] paramTypes;
}
