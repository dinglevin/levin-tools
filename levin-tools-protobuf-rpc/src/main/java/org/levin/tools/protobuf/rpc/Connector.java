package org.levin.tools.protobuf.rpc;

import java.io.IOException;
import java.net.Socket;

public interface Connector {
    public int getServerPort();
    public void setServerPort(int serverPort);
    
    public int getBacklog();
    public void setBacklog(int backlog);
    
    public int getAcceptors();
    public void setAcceptors(int acceptors);
    
    public RpcServer getRpcServer();
    
    public Endpoint createEndpoint(Socket socket, ServiceContainer serviceContainer) throws IOException;
    
    public void start() throws IOException;
    public void stop() throws IOException, InterruptedException;
}
