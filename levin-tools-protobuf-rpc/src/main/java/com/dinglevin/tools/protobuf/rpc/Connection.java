package com.dinglevin.tools.protobuf.rpc;

import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcResponse;

public interface Connection {
    public void close();
    public boolean isClosed();
    
    public RpcRequest receiveRequest();
    public void sendResponse(RpcResponse response);
    public void sendErrorResponse(Throwable ex, String service, String method);
}
