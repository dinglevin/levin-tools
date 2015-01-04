package org.levin.tools.protobuf.rpc.integration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.levin.tools.protobuf.rpc.client.ClientConnection;
import org.levin.tools.protobuf.rpc.client.RpcChannelImpl;
import org.levin.tools.protobuf.rpc.client.SocketClientConnection;
import org.levin.tools.protobuf.rpc.controller.RpcControllers;
import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.MyService;
import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.SearchRequest;
import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.SearchResponse;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

public class IntegrationClient {
    public static void main(String[] args) throws Exception {
        ClientConnection connection = new SocketClientConnection("localhost", 9000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        
        SearchRequest request = SearchRequest.newBuilder()
                .setQueryString("testQuery")
                .setPageNumber(10)
                .setResultPerPage(50)
                .build();
        RpcController controller = RpcControllers.newClientRpcController();
        
        MyService.BlockingInterface blockingService = MyService.newBlockingStub(new RpcChannelImpl(connection, executorService));
        controller.reset();
        SearchResponse response = blockingService.request(controller, request);
        if(controller.failed()) {
            System.err.println("Request failed: " + request);
        } else {
            System.out.println("Received response: " + response);
        }
        connection.close();
        
        final CountDownLatch latch = new CountDownLatch(1);
        connection = new SocketClientConnection("localhost", 9000);
        MyService.Interface service = MyService.newStub(new RpcChannelImpl(connection, executorService));
        controller.reset();
        service.request(controller, request, new RpcCallback<SearchResponse>() {
            @Override
            public void run(SearchResponse response) {
                System.out.println("Received asyn response: " + response);
                latch.countDown();
            }
        });
        latch.await();
        connection.close();
        
        executorService.shutdown();
    }
}
