package org.levin.tools.protobuf.rpc.client;

import java.io.IOException;

import org.levin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse;

public interface ClientConnection {
    public void sendRpcRequest(RpcRequest request) throws IOException;
    public RpcResponse receiveRpcResponse() throws IOException;
    
    public void close() throws IOException;
    public boolean isClose();
}
