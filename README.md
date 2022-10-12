# Simple-RPC
用Java手撸一个简单的RPC框架
本项目与文档转载于：https://github.com/tomstillcoding/Simple-RPC

# 使用方式
用IDEA打开后配置Maven，右键点击pom.xml，重新加载项目（Reload Project）
然后直接运行服务器程序和客户端程序

带你手撸简单RPC框架

# 目录

今天的视频分三部分：

1、介绍RPC是什么

2、用Java手撸一个最简单的RPC框架

今天聊到的所有东西都会开源，你可以照着文档，以及看看源代码，就能明白RPC的核心内容，也可以在这个简单RPC框架代码上补充各种组件，最后写到自己的简历上，最重要的目的是，撸一套RPC框架，能让你掌握RPC的核心功能实现。



# 手撸Java RPC框架

因此，我们要用Java手撸一个最简单的RPC，按照上面的说法，应该要实现的是：

框架：

1. 接口文件，定义了方法名、参数、返回值，供客户端和服务端使用
2. RPC框架，让客户端和服务端可以使用框架，达到**本地调用，远端执行**的目的

测试：

1. 写一个TestServer可执行程序，注册方法的实现后，使用rpcServer进行监听并处理rpc调用
2. 写一个TestClient可执行程序，使用rpcClient进行发起rpc调用，并打印rpc调用结果



<img src="./Screen Shot 2022-05-29 at 14.24.39.png" alt="Screen Shot 2022-05-29 at 14.24.21" style="zoom:50%;" />

### 第一步：写IDL文件

IDL.Hello的内容直接编写完毕。

按照grpc的方式，编写接口HelloService，以及里面的消息体HelloRequest和HelloResponse，客户端和服务器都使用这同一套接口

### 第二步：编写RPC协议

RpcRequest和RpcResponse都是RPC协议，RPC协议包括header和body两部分，header我们用String表示，body我们用序列化后的byte[]流表示，这里的字节流的序列化的方式可以是Java的序列化方式，可以换成JSON序列化方式，也可以用Thrift、PB序列化方式，为了简单，我们这次直接用Java的序列化方式。

然后body中被序列化的内容，因为是codec层的工作，放在了codec包中，RPCReuqest要调用一个方法，需要知道接口名、方法名、参数、参数类型，因此把这些东西放进RpcRequestBody中即可，后面把它序列化后房价RpcRequest的body字节流中；同理，RPCResponse 的body中，只需要一个被序列化后的Java Object即可。

### 第三步：分析

我们的rpcClient要执行一个函数hello，传入参数HelloRequest，然后返回HelloResponse。

整个流程就是：客户端获得rpcService对象，使用rpcService对象执行hello方法，那么rpcService底层实现就发送一条RpcRequest协议（对比HTTP协议）把：要执行的接口名+方法名+参数类型+具体参数序列化后，放进RpcRequest协议的body字节流中，然后给RpcRequest加上header，发给服务端，服务端解析出Rpc协议的body（对比HTTP协议解析body）中的接口名、方法名等，直接调用本地的接口的实现，然后将返回值包装成一条RpcResponse消息，发送给客户端即可，rpcService底层将该response消息解析，从body中拿到（也是对比HTTP解析body）返回值，然后返回给客户端。

### 第四步：客户端实现（动态代理）

客户端方面，客户端本地只有IDL.Hello中的内容，没有方法的具体实现，也就是说要调用一个没有实现的接口，显然，我们使用Java反射的动态代理特性，实例化一个接口，将调用接口方法“代理”给InvocationHandler中的invoke来执行，在Invoke中获取到接口名、方法名等包装成Rpc协议，发送给服务端，然后等待服务端返回。

### 第五步：服务端实现（反射调用）

服务端方面，本地需要实现接口的方法，然后在启动监听网络之前注册所有的接口，当消息到来的时候，根据RpcRequestBody中的接口名拿到接口对象，然后用反射的方式调用即可，将调用结果包装成RpcResponse，发送给客户端。

### 第六步：测试

编写测试用例

一共两个接口，HelloService和PingService，HelloService有两个方法：hello和hi，PingService有一个方法：ping

### 总结

事实上一个完整的RPC框架不仅包含上面的内容，还要提供很多功能，比如说

服务发现：客户端怎么找到能够调用rpc的服务器的ip和端口？

服务治理：整个rpc的运行怎么可靠，比如说客户端请求太多，一台服务器不顶用，IO打满了，再加一台服务器？

服务注册：服务器怎么才能把自己能够handle的接口告诉客户端，不然自己都不能处理，客户端调用接口，调了也是失败

编解码：比如上面body中我们是直接用的Java序列化，那要是跨平台怎么办，客户端用Java写，服务端用Go写，Go又不能处理Java序列化，对吧，而且编解码性能、编码后的字节数量也是很重要的一些东西

RPC协议：协议的字段有很多的，协议版本、传输方式、序列化方式、连接个数等等



所以，今天讲的这个东西不过是一个RPC的最简化Demo，帮大家理解rpc的原理，按照多层模型去设计与实现，后面往这个架子上面搭东西，或者去学习真正的RPC框架是比较有帮助的.
