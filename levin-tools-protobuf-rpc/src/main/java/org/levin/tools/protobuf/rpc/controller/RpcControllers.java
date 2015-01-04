package org.levin.tools.protobuf.rpc.controller;

import com.google.protobuf.RpcController;

public class RpcControllers {
    private RpcControllers() { }
    
    public static RpcController newServerRpcController() {
        return new ServerRpcController();
    }
    
    public static RpcController newClientRpcController() {
        return new ClientRpcController();
    }
}
