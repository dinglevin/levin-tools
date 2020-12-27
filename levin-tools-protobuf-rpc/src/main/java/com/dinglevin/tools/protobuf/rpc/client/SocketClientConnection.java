package com.dinglevin.tools.protobuf.rpc.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcRequest;
import com.dinglevin.tools.protobuf.rpc.RpcProtos.RpcResponse;

public class SocketClientConnection implements ClientConnection {
    private final Socket socket;
    private final BufferedInputStream input;
    private final BufferedOutputStream output;
    
    public SocketClientConnection(String host, int port) throws IOException {
        this(new Socket(host, port));
    }
    
    public SocketClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        
        try {
            this.input = new BufferedInputStream(socket.getInputStream());
            this.output = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
            throw ex;
        }
    }

    @Override
    public void sendRpcRequest(RpcRequest request) throws IOException {
        request.writeDelimitedTo(output);
        output.flush();
    }

    @Override
    public RpcResponse receiveRpcResponse() throws IOException {
        RpcResponse.Builder builder = RpcResponse.newBuilder();
        if (!builder.mergeDelimitedFrom(input)) {
            throw new IllegalStateException("Failed to read RpcResponse");
        }
        return builder.build();
    }
    
    public Socket getSocket() {
        return socket;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public boolean isClose() {
        return socket.isClosed();
    }
}
