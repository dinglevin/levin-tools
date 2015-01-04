package org.levin.tools.protobuf.rpc.container;

import java.util.Map;

import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse.Status;
import org.levin.tools.protobuf.rpc.ServiceContainer;
import org.levin.tools.protobuf.rpc.SocketRpcException;
import org.levin.tools.protobuf.rpc.controller.RpcControllers;

import com.google.common.collect.Maps;
import com.google.protobuf.BlockingService;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class DefaultServiceContainer implements ServiceContainer {
    private Map<String, BlockingService> serviceMap = Maps.newConcurrentMap();

    @Override
    public void registerService(BlockingService service) {
        serviceMap.put(service.getDescriptorForType().getFullName(), service);
    }

    @Override
    public BlockingService findService(String service) {
        return serviceMap.get(service);
    }

    @Override
    public RpcResponse doRpc(String serviceName, String methodName, ByteString data) {
        BlockingService service = findService(serviceName);
        if (service == null) {
            throw new SocketRpcException(Status.SERVICE_NOT_FOUND, 
                    "service: " + serviceName + " not found");
        }
        MethodDescriptor method = service.getDescriptorForType().findMethodByName(methodName);
        if (method == null) {
            throw new SocketRpcException(Status.METHOD_NOT_FOUND, 
                    "method: " + methodName + " in service: " + serviceName + " not found");
        }
        
        Message methodRequest = null;
        try {
            Message requestPrototype = service.getRequestPrototype(method);
            methodRequest = requestPrototype.getParserForType().parseFrom(data);
        } catch (Exception ex) {
            throw new SocketRpcException(Status.BAD_REQUEST_DATA, 
                    "Failed to parse request data of " + serviceName + "." + methodName);
        }
        
        RpcController controller = RpcControllers.newServerRpcController();
        Message result = null;
        try {
            result = service.callBlockingMethod(method, controller, methodRequest);
        } catch (ServiceException e) {
            throw new SocketRpcException(Status.RPC_FAILED, "RPC call[" + 
                    serviceName + "." + methodName + "] failed: " + e.getMessage(), e);
        }
        
        if (controller.failed()) {
            throw new SocketRpcException(Status.RPC_FAILED, 
                    "RPC call[" + serviceName + "." + methodName + "] failed: " + controller.errorText());
        }
        
        return RpcResponse.newBuilder()
                          .setService(serviceName)
                          .setMethod(methodName)
                          .setStatus(Status.SUCCESS)
                          .setData(result.toByteString())
                          .build();
    }
}
