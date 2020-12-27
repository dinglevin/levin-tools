package com.dinglevin.tools.protobuf.rpc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    
    private final Connector connector;
    private final ServiceContainer serviceContainer;
    private final ExecutorService executorService;
    
    public RpcServer(Connector connector, ServiceContainer serviceContainer, 
            ExecutorService executorService) {
        connector.setRpcServer(this);
        
        this.connector = connector;
        this.serviceContainer = serviceContainer;
        this.executorService = executorService;
    }
    
    public void start() {
        try {
            connector.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start RpcServer", e);
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Failed to start RpcServer in 1s", e);
        }
        
        if (connector.isRunning()) {
            logger.info("RpcServer started");
        } else {
            throw new IllegalStateException("Failed to start RpcServer in 1s");
        }
        
        while(connector.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Wait interrupted", e);
            }
        }
    }
    
    public void submitConnectionHandler(Runnable handler) {
        executorService.submit(handler);
    }

    public RpcResponse doRpc(String serviceName, String methodName, ByteString data) {
        return serviceContainer.doRpc(serviceName, methodName, data);
    }
    
    public Connector getConnector() {
        return connector;
    }
    
    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }
    
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
