package org.levin.tools.protobuf.rpc;

import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse;

import com.google.protobuf.BlockingService;
import com.google.protobuf.ByteString;

public interface ServiceContainer {
    public void registerService(BlockingService service);
    public BlockingService findService(String service);
    public RpcResponse doRpc(String serviceName, String methodName, ByteString data);
}
