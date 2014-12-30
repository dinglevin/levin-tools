package org.levin.tools.protobuf.rpc;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Service;

public interface ServiceContainer {
    public void registerService(Service service);
    public void registerBlockingService(BlockingService blockingService);
    
    public BlockingService findBlockingService(String service);
}
