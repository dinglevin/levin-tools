package org.levin.tools.protobuf.rpc.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.levin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse.Status;

import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class RpcChannelImpl implements RpcChannel, BlockingRpcChannel {
    private final ClientConnection connection;
    private final ExecutorService executorService;
    
    public RpcChannelImpl(ClientConnection connection, ExecutorService executorService) {
        this.connection = connection;
        this.executorService = executorService;
    }

    @Override
    public Message callBlockingMethod(MethodDescriptor method,
            RpcController controller, Message request, Message responsePrototype)
            throws ServiceException {
        RpcRequest rpcRequest = RpcRequest.newBuilder()
                .setService(method.getService().getFullName())
                .setMethod(method.getName())
                .setData(request.toByteString())
                .build();
        
        try {
            connection.sendRpcRequest(rpcRequest);
        } catch (IOException e) {
            controller.setFailed("Failed to send RpcRequest: " + e.getMessage());
            throw new ServiceException("Failed to send RpcRequest", e);
        }
        
        RpcResponse rpcResponse = null;
        try {
            rpcResponse = connection.receiveRpcResponse();
        } catch (IOException e) {
            controller.setFailed("Failed to receive RpcResponse: " + e.getMessage());
            throw new ServiceException("Failed to receive RpcResponse", e);
        }
        
        if (rpcResponse.getStatus() != Status.SUCCESS) {
            controller.setFailed("Status: " + rpcResponse.getStatus() + ", Msg: " + rpcResponse.getErrorMsg());
            throw new ServiceException("Status: " + rpcResponse.getStatus() + ", Msg: " + rpcResponse.getErrorMsg());
        }
        
        if (rpcResponse.getData() == null) {
            controller.setFailed("response data is null");
            throw new ServiceException("response data is null");
        }
        
        try {
            return responsePrototype.newBuilderForType().mergeFrom(rpcResponse.getData()).build();
        } catch (InvalidProtocolBufferException e) {
            controller.setFailed("Failed to parse response");
            throw new ServiceException("Failed to parse response");
        }
    }

    @Override
    public void callMethod(final MethodDescriptor method, 
            final RpcController controller,
            final Message request, 
            final Message responsePrototype,
            final RpcCallback<Message> done) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                RpcRequest rpcRequest = RpcRequest.newBuilder()
                        .setService(method.getService().getFullName())
                        .setMethod(method.getName())
                        .setData(request.toByteString())
                        .build();
                
                try {
                    connection.sendRpcRequest(rpcRequest);
                } catch (IOException e) {
                    controller.setFailed("Failed to send RpcRequest: " + e.getMessage());
                    // ensure callback will be called once
                    if (done != null) {
                        done.run(null);
                    }
                    return;
                }
                
                RpcResponse rpcResponse = null;
                try {
                    rpcResponse = connection.receiveRpcResponse();
                } catch (IOException e) {
                    controller.setFailed("Failed to receive RpcResponse: " + e.getMessage());
                    // ensure callback will be called once
                    if (done != null) {
                        done.run(null);
                    }
                    return;
                }
                
                if (rpcResponse.getStatus() != Status.SUCCESS) {
                    controller.setFailed("Status: " + rpcResponse.getStatus() + ", Msg: " + rpcResponse.getErrorMsg());
                    // ensure callback will be called once
                    if (done != null) {
                        done.run(null);
                    }
                    return;
                }
                
                if (rpcResponse.getData() == null) {
                    controller.setFailed("response data is null");
                    // ensure callback will be called once
                    if (done != null) {
                        done.run(null);
                    }
                    return;
                }
                
                try {
                    done.run(responsePrototype.newBuilderForType().mergeFrom(rpcResponse.getData()).build());
                } catch (InvalidProtocolBufferException e) {
                    controller.setFailed("Failed to parse response");
                    // ensure callback will be called once
                    if (done != null) {
                        done.run(null);
                    }
                }
            }
        });
    }
}
