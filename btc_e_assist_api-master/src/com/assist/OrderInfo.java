package com.assist;

public class OrderInfo extends PrivateMultipleResponse {
    public OrderInfo(PrivateNetwork nt) {
        network = nt;
    }

    public synchronized void setOrder_id(String str) {
        paramsMap.put("order_id", str.toUpperCase());
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("OrderInfo", getParams(),
                connectTimeoutMillis, readTimeoutMillis));
        return success;
    }

    public synchronized String getCurrentPair() {
        return convertPairName(rootNode.path("return").path(current_position)
                .path("pair").asText());
    }

    public synchronized String getCurrentType() {
        return rootNode.path("return").path(current_position).path("type")
                .asText();
    }

    public synchronized String getCurrentStart_amount() {
        return formatDouble(rootNode.path("return").path(current_position)
                .path("start_amount").asDouble());
    }

    public synchronized String getCurrentRate() {
        return formatDouble(rootNode.path("return").path(current_position)
                .path("rate").asDouble());
    }

    public synchronized String getCurrentTimestamp_created() {
        return rootNode.path("return").path(current_position)
                .path("timestamp_created").toString();
    }

    /**
     * 0 - active, 1 - executed order, 2 - canceled, 3 - canceled, but was
     * partially executed.
     *
     * @return current status
     */
    public synchronized int getCurrentStatus() {
        return rootNode.path("return").path(current_position).path("status")
                .asInt();
    }
}