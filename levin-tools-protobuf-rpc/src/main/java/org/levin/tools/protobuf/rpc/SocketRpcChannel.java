package org.levin.tools.protobuf.rpc;

import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

public class SocketRpcChannel implements RpcChannel {

    @Override
    public void callMethod(MethodDescriptor method, RpcController controller,
            Message request, Message responsePrototype,
            RpcCallback<Message> done) {
        
    }

}
