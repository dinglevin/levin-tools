package com.dinglevin.tools.protobuf.rpc;

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
    public void setRpcServer(RpcServer rpcServer);
    
    public Connection createConnection(Socket socket);
    
    public void start() throws IOException;
    public void stop() throws IOException, InterruptedException;
    public boolean isRunning();
}
