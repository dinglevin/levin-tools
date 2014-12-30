package org.levin.tools.protobuf.rpc.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.levin.tools.protobuf.rpc.Endpoint;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse;
import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse.Status;
import org.levin.tools.protobuf.rpc.controller.DefaultRpcController;
import org.levin.tools.protobuf.rpc.ServiceContainer;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class SocketEndpoint implements Endpoint {
    private final Socket socket;
    private final ServiceContainer serviceContainer;
    
    private final InputStream input;
    private final OutputStream output;
    
    public SocketEndpoint(Socket socket, ServiceContainer serviceContainer) throws IOException {
        this.socket = socket;
        this.serviceContainer = serviceContainer;
        
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
    }

    @Override
    public void run() throws IOException {
        doRun();
    }
    
    protected void doRun() throws IOException {
        RpcController controller = new DefaultRpcController();
        RpcRequest request = RpcRequest.newBuilder().mergeFrom(input).build();
        String serviceName = request.getService();
        String methodName = request.getMethod();
        RpcResponse response = null;
        
        try {
            BlockingService service = serviceContainer.findBlockingService(serviceName);
            if (service == null) {
                throw new RuntimeException("service: " + serviceName + " not supported");
            }
            MethodDescriptor method = service.getDescriptorForType().findMethodByName(methodName);
            if (method == null) {
                throw new RuntimeException("method: " + methodName + 
                        " in service: " + serviceName + " not supported");
            }
            
            Message requestPrototype = service.getRequestPrototype(method);
            Message methodRequest =requestPrototype.getParserForType().parseFrom(request.getData());
            Message result = service.callBlockingMethod(method, controller, methodRequest);
            
            if (controller.failed()) {
                response = RpcResponse.newBuilder()
                        .setService(serviceName)
                        .setMethod(methodName)
                        .setStatus(Status.APP_ERROR)
                        .setErrorMsg(controller.errorText())
                        .build();
            } else {
                response = RpcResponse.newBuilder()
                        .setService(serviceName)
                        .setMethod(methodName)
                        .setStatus(Status.SUCCESS)
                        .setData(result.toByteString())
                        .build();
            }
        } catch (ServiceException e) {
            response = RpcResponse.newBuilder()
                                  .setService(serviceName)
                                  .setMethod(methodName)
                                  .setStatus(Status.APP_ERROR)
                                  .setErrorMsg(e.getMessage())
                                  .build();
        } catch (Exception e) {
            response = RpcResponse.newBuilder()
                                  .setService(serviceName)
                                  .setMethod(methodName)
                                  .setStatus(Status.SERVER_ERROR)
                                  .setErrorMsg(e.getMessage())
                                  .build();
        }
        
        response.writeTo(output);
    }
    
    public Socket getSocket() {
        return socket;
    }
}
