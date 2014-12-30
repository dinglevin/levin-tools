package org.levin.tools.protobuf.rpc;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class SocketBlockingRpcChannel implements BlockingRpcChannel {

    @Override
    public Message callBlockingMethod(MethodDescriptor method,
            RpcController controller, Message request, Message responsePrototype)
            throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
