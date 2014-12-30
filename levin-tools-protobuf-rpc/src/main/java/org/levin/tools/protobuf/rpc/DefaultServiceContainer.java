package org.levin.tools.protobuf.rpc;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Service;

public class DefaultServiceContainer implements ServiceContainer {
    private Map<String, BlockingService> blockingServices = Maps.newConcurrentMap();

    @Override
    public void registerService(Service service) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerBlockingService(BlockingService blockingService) {
        blockingServices.put(blockingService.getDescriptorForType().getFullName(), blockingService);
    }

    @Override
    public BlockingService findBlockingService(String service) {
        return blockingServices.get(service);
    }
}
