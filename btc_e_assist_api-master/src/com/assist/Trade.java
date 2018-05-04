package com.assist;

public class Trade extends PrivateSingleResponse {
    public Trade(PrivateNetwork nt) {
        network = nt;
    }

    public synchronized void setAmount(String str) {
        paramsMap.put("amount", str);
    }

    public synchronized void setPair(String str) {
        paramsMap.put("pair", str.replace('-', '_').toLowerCase());
    }

    public synchronized void setType(String str) {
        paramsMap.put("type", str.toLowerCase());
    }

    public synchronized void setRate(String str) {
        paramsMap.put("rate", str);
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("Trade", getParams(), connectTimeoutMillis,
                readTimeoutMillis));
        return success;
    }

    public synchronized String getReceived() {
        return formatDouble(rootNode.path("return").path("received").asDouble());
    }

    public synchronized String getRemains() {
        return formatDouble(rootNode.path("return").path("remains").asDouble());
    }

    public synchronized String getOrder_id() {
        return rootNode.path("return").path("order_id").toString();
    }
}
