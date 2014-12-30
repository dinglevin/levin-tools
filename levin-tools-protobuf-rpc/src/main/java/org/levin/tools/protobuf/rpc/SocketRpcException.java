package org.levin.tools.protobuf.rpc;

public class SocketRpcException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketRpcException(String msg) {
        super(msg);
    }
    
    public SocketRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
