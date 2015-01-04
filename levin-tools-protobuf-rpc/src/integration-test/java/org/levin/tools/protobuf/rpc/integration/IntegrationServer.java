package org.levin.tools.protobuf.rpc.integration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.levin.tools.protobuf.rpc.Connector;
import org.levin.tools.protobuf.rpc.RpcServer;
import org.levin.tools.protobuf.rpc.ServiceContainer;
import org.levin.tools.protobuf.rpc.container.DefaultServiceContainer;
import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.MyService;
import org.levin.tools.protobuf.rpc.socket.SocketConnector;

public class IntegrationServer {
    public static void main(String[] args) throws Exception {
        Connector connector = createConnector();
        ServiceContainer serviceContainer = createServiceContainer();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        RpcServer rpcServer = new RpcServer(connector, serviceContainer, executorService);
        
        rpcServer.start();
    }
    
    private static Connector createConnector() {
        SocketConnector connector = new SocketConnector();
        connector.setAcceptors(2);
        connector.setBacklog(100);
        connector.setServerPort(9000);
        return connector;
    }
    
    private static ServiceContainer createServiceContainer() {
        ServiceContainer serviceContainer = new DefaultServiceContainer();
        serviceContainer.registerService(MyService.newReflectiveBlockingService(new MyServiceImpl()));
        return serviceContainer;
    }
}
