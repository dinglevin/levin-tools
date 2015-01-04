package org.levin.tools.protobuf.rpc.controller;

class ServerRpcController extends AbstractRpcController {
    public ServerRpcController() { }
    
    @Override
    public void reset() {
        throw new UnsupportedOperationException("reset not supported on server side");
    }
}
