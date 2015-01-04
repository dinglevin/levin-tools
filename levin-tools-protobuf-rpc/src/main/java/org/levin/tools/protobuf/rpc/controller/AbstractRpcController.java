package org.levin.tools.protobuf.rpc.controller;

import static com.google.common.base.Preconditions.*;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

abstract class AbstractRpcController implements RpcController {
    private String errorText = null;
    private boolean canncelled = false;

    @Override
    public void reset() {
        errorText = null;
        canncelled = false;
    }

    @Override
    public boolean failed() {
        return errorText != null;
    }

    @Override
    public String errorText() {
        return errorText;
    }

    @Override
    public void startCancel() {
        canncelled = true;
    }

    @Override
    public void setFailed(String reason) {
        this.errorText = checkNotNull(reason, "reason is null");
    }

    @Override
    public boolean isCanceled() {
        return canncelled;
    }

    @Override
    public void notifyOnCancel(RpcCallback<Object> callback) {
        throw new UnsupportedOperationException("notifyOnCancel");
    }

}
