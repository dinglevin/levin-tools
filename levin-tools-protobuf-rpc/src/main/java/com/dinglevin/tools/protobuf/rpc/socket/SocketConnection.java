package com.dinglevin.tools.protobuf.rpc.socket;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.dinglevin.tools.protobuf.rpc.Connection;
import com.dinglevin.tools.protobuf.rpc.SocketRpcException;
import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcResponse;
import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcResponse.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class SocketConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);
    
    private final Socket socket;
    private final BufferedInputStream input;
    private final BufferedOutputStream output;
    
    public SocketConnection(Socket socket) {
        this.socket = checkNotNull(socket, "socket is null");
        
        try {
            this.input = new BufferedInputStream(socket.getInputStream());
            this.output = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore this exception here
            }
            
            throw new SocketRpcException(Status.RPC_ERROR, socket,
                    "get input/out stream from", ex);
        }
    }
    
    @Override
    public void close() {
        if (socket.isClosed()) {
            throw new SocketRpcException(Status.RPC_ERROR, "socket already closed");
        }
        
        try {
            socket.close();
        } catch (IOException e) {
            throw new SocketRpcException(Status.RPC_ERROR, socket, "close", e);
        }
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public RpcRequest receiveRequest() {
        try {
            RpcRequest.Builder builder = RpcRequest.newBuilder();
            if (!builder.mergeDelimitedFrom(input)) {
                throw new SocketRpcException(Status.BAD_REQUEST_PROTO, "RpcRequest not integerated");
            }
            if (!builder.isInitialized()) {
                throw new SocketRpcException(Status.BAD_REQUEST_PROTO,
                        "RpcRequest hasn't been initialized yet");
            }
            return builder.build();
        } catch (IOException e) {
            throw new SocketRpcException(Status.RPC_ERROR, socket,
                    "parse input data from", e);
        }
    }

    @Override
    public void sendResponse(RpcResponse response) {
        try {
            response.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            logger.error("Failed to write RpcResponse[" + response.getService() + 
                    "." + response.getMethod() + "] to socket: " + socket, e);
        }
    }
    
    @Override
    public void sendErrorResponse(Throwable ex, String service, String method) {
        RpcResponse.Builder builder = RpcResponse.newBuilder();
        builder.setService(Strings.isNullOrEmpty(service) ? "UNKNOWN" : service);
        builder.setMethod(Strings.isNullOrEmpty(method) ? "UNKNOWN" : method);
        if (ex instanceof SocketRpcException) {
            builder.setStatus(((SocketRpcException) ex).getErrorStatus());
        } else {
            builder.setStatus(RpcResponse.Status.RPC_ERROR);
        }
        builder.setErrorMsg(ex.getMessage());
        
        try {
            sendResponse(builder.build());
        } catch (Throwable e) {
            logger.error("Failed to write RpcResponse[" + service + 
                    "." + method + "] to socket: " + socket, e);
        }
    }

    @Override
    public String toString() {
        return socket.toString();
    }
}
