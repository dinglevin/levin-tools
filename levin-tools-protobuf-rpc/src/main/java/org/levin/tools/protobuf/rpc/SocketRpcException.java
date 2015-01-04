package org.levin.tools.protobuf.rpc;

import java.net.Socket;

import org.levin.tools.protobuf.rpc.RpcProtos.RpcResponse.Status;

public class SocketRpcException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final Status errorStatus;
    private final Socket socket;
    
    public SocketRpcException(Status errorStatus, String msg) {
        this(errorStatus, null, msg);
    }

    public SocketRpcException(Status errorStatus, Socket socket, String msg) {
        super(buildMsg(socket, msg, null));
        
        this.socket = socket;
        this.errorStatus = errorStatus;
    }
    
    public SocketRpcException(Status errorStatus, String msg, Throwable cause) {
        this(errorStatus, null, msg, cause);
    }
    
    public SocketRpcException(Status errorStatus, Socket socket, String msg, Throwable cause) {
        super(buildMsg(socket, msg, cause), cause);
        
        this.socket = socket;
        this.errorStatus = errorStatus;
    }
    
    public Status getErrorStatus() {
        return errorStatus;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public String toString() {
        return errorStatus + ": " + super.toString();
    }
    
    private static String buildMsg(Socket socket, String msg, Throwable cause) {
        return socket == null ? msg : 
            "Failed to " + msg + " socket: " + socket + (cause == null ? "" : " - " + cause.getMessage());
    }
}
