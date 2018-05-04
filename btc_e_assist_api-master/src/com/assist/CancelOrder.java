package com.assist;

public class CancelOrder extends PrivateSingleResponse {
    public CancelOrder(PrivateNetwork nt) {
        network = nt;
    }

    public synchronized void setOrder_id(String str) {
        paramsMap.put("order_id", str);
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("CancelOrder", getParams(),
                connectTimeoutMillis, readTimeoutMillis));
        return success;
    }

    public synchronized String getOrder_id() {
        return rootNode.path("return").path("order_id").toString();
    }
}
