package org.levin.tools.protobuf.rpc.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.levin.tools.protobuf.rpc.Endpoint;
import org.levin.tools.protobuf.rpc.ServiceContainer;

import com.google.protobuf.Descriptors;

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

    }
}
