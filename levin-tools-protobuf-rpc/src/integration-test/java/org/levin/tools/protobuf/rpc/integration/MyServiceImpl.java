package org.levin.tools.protobuf.rpc.integration;

import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.SearchRequest;
import org.levin.tools.protobuf.rpc.integration.MyServiceProtos.SearchResponse;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class MyServiceImpl implements MyServiceProtos.MyService.BlockingInterface {

    @Override
    public SearchResponse request(RpcController controller,
            SearchRequest request) throws ServiceException {
        return SearchResponse.newBuilder().setPageNumber(request.getPageNumber())
                .setQueryString(request.getQueryString())
                .setResultPerPage(request.getResultPerPage())
                .build();
    }
    
}
