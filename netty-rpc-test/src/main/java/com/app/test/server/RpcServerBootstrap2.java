package com.app.test.server;

import com.app.test.service.*;
import com.netty.rpc.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServerBootstrap2 {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerBootstrap2.class);

    public static void main(String[] args) {
        /**
         * 我有三个maven项目，分别是
         *  netty-rpc-server
         *  netty-rpc-client
         *  netty-rpc-common
         * netty-rpc-common实现了基本的信息交互数据格式，netty-rpc-server和netty-rpc-client都需要使用netty-rpc-common,netty-rpc-server是服务器包，netty-rpc-client是客户端包，需要将他们分别部署，而netty-rpc-common是公用的，我应该怎么部署
         *
         * 服务实现：
         *  服务器将netty-rpc-test中服务注册到zookeeper
         *  客户端通过动态代理来创建netty-rpc-test中的service
         *
         * 疑惑：
         *  如果服务器和客户端分别部署在不同的计算机上，需要远程协议进行数据的交互，service类该如何进行维护
         *      1.如果服务器和客户端都有一个service服务，每次进行修改，服务器和客户端都需要停止运行，代价太大
         *      2.如果通过protobuf来实现，感觉会更好，双方共享proto文件，任何一方改变了，只需要重新编译即可
         */
        String serverAddress = "127.0.0.1:1314";
        String registryAddress = "127.0.0.1:2181";
        RpcServer rpcServer = new RpcServer(serverAddress, registryAddress);
        HelloService helloService1 = new HelloServiceImpl();
        rpcServer.addService(HelloService.class.getName(), "1.0", helloService1);
        HelloService helloService2 = new HelloServiceImpl2();
        rpcServer.addService(HelloService.class.getName(), "2.0", helloService2);
        PersonService personService = new PersonServiceImpl();
        rpcServer.addService(PersonService.class.getName(), "", personService);
        try {
            rpcServer.start();
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.toString());
        }
    }
}
