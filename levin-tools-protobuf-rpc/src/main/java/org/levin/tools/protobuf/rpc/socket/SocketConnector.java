package org.levin.tools.protobuf.rpc.socket;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.levin.tools.protobuf.rpc.Connector;
import org.levin.tools.protobuf.rpc.Endpoint;
import org.levin.tools.protobuf.rpc.RpcServer;
import org.levin.tools.protobuf.rpc.ServiceContainer;
import org.levin.tools.protobuf.rpc.SocketRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketConnector implements Connector {
    private static final Logger logger = LoggerFactory.getLogger(SocketConnector.class);
    
    private static final int DEFAULT_BACKLOG = 50;
    private static final int MAX_BACKLOG = 1000;
    private static final int MIN_SERVER_PORT = 0;
    private static final int MAX_SERVER_PORT = 65536;
    private static final int MIN_ACCEPTORS = 0;
    private static final int DEFAULT_ACCEPTORS = 5;
    private static final int MAX_ACCEPTORS = 100;
    
    public static enum Status {
        Initialized,
        Starting,
        Started,
        Running,
        Stopping,
        Stopped
    }
    
    private final RpcServer rpcServer;
    
    private int serverPort = 0;
    private int backlog = MIN_ACCEPTORS;
    private InetAddress bindAddress = null;
    private int acceptors = DEFAULT_ACCEPTORS;
    
    private ServerSocket serverSocket;
    
    private volatile Status status;
    private Thread[] acceptorThreads;
    
    public SocketConnector(RpcServer rpcServer) {
        checkNotNull(rpcServer, "rpcServer is null");
        
        this.rpcServer = rpcServer;
        this.status = Status.Initialized;
    }
    
    public int getServerPort() {
        return serverPort;
    }
    public void setServerPort(int serverPort) {
        checkWithinRange(MIN_SERVER_PORT, MAX_SERVER_PORT, serverPort, "serverPort");
        this.serverPort = serverPort;
    }
    
    public int getBacklog() {
        return backlog;
    }
    public void setBacklog(int backlog) {
        checkWithinRange(DEFAULT_BACKLOG, MAX_BACKLOG, backlog, "backlog");
        this.backlog = backlog;
    }
    
    public int getAcceptors() {
        return this.acceptors;
    }
    public void setAcceptors(int acceptors) {
        checkWithinRange(MIN_ACCEPTORS, MAX_ACCEPTORS, backlog, "backlog");
        this.acceptors = acceptors;
    }
    
    @Override
    public RpcServer getRpcServer() {
        return rpcServer;
    }
    
    public synchronized void start() throws IOException {
        if (status != Status.Initialized) {
            throw new SocketRpcException("SocketServer already started");
        }
        
        status = Status.Starting;
        
        serverSocket = new ServerSocket();
        configServerSocket(serverSocket);
        serverSocket.bind(new InetSocketAddress(bindAddress, serverPort), backlog);
        
        acceptorThreads = new Thread[acceptors];
        for (int i = 0; i < acceptors; i++) {
            acceptorThreads[i] = new Thread(new Acceptor(i), "Acceptor-" + i);
            acceptorThreads[i].start();
        }
        
        status = Status.Started;
    }
    
    public synchronized void stop() throws IOException, InterruptedException {
        status = Status.Stopping;
        
        for (int i = 0; i < acceptors; i++) {
            acceptorThreads[i].interrupt();
        }
        
        for (int i = 0; i < acceptors; i++) {
            acceptorThreads[i].join(TimeUnit.MINUTES.toMillis(1));
        }
        
        status = Status.Stopped;
    }
    
    public Endpoint createEndpoint(Socket socket, ServiceContainer serviceContainer) throws IOException {
        return new SocketEndpoint(socket, serviceContainer);
    }
    
    protected void configServerSocket(ServerSocket serverSocket) throws IOException {
        serverSocket.setReceiveBufferSize(8192);
        serverSocket.setReuseAddress(true);
        serverSocket.setSoTimeout((int) TimeUnit.MINUTES.toMillis(60 * 24));
    }
    
    private class Acceptor implements Runnable {
        private final int index;
        
        public Acceptor(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            setRunning();
            
            while (status == Status.Running) {
                try {
                    Socket socket = serverSocket.accept();
                    Endpoint endpoint = createEndpoint(socket, rpcServer.getServiceContainer());
                    endpoint.run();
                } catch (IOException e) {
                    logger.error("Error happened on acceptor-" + index + ", exit!", e);
                    break;
                }
            }
        }
        
        private void setRunning() {
            synchronized (SocketConnector.this) {
                if (status == Status.Started) {
                    status = Status.Running;
                }
            }
        }
    }
    
    private static void checkWithinRange(int min, int max, int value, String arg) {
        checkArgument(value > min && value < max, arg + "(" + value + ") not within (" + min + ", " + max + ")");
    }
}
