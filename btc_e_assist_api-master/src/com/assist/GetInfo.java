package com.assist;

public class GetInfo extends PrivateSingleResponse {
    public GetInfo(PrivateNetwork nt) {
        network = nt;
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("getInfo", "", connectTimeoutMillis,
                readTimeoutMillis));
        return success;
    }

    public synchronized String getTransaction_count() {
        return rootNode.path("return").path("transaction_count").toString();
    }

    public synchronized String getOpen_orders() {
        return rootNode.path("return").path("open_orders").toString();
    }

    public synchronized String getServer_time() {
        return rootNode.path("return").path("server_time").toString();
    }

    public synchronized boolean isInfo() {
        return rootNode.path("return").path("rights").path("info").asBoolean();
    }

    public synchronized boolean isTrade() {
        return rootNode.path("return").path("rights").path("trade").asBoolean();
    }
}
