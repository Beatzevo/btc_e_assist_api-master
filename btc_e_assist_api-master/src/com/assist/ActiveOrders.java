package com.assist;

public class ActiveOrders extends PrivateMultipleResponse {
    public ActiveOrders(PrivateNetwork nt) {
        network = nt;
    }

    public synchronized void setPair(String str) {
        paramsMap.put("pair", str.replace('-', '_').toLowerCase());
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("ActiveOrders", getParams(),
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

    public synchronized String getCurrentRate() {
        return formatDouble(rootNode.path("return").path(current_position)
                .path("rate").asDouble());
    }

    public synchronized String getCurrentTimestamp_created() {
        return rootNode.path("return").path(current_position)
                .path("timestamp_created").toString();
    }

    /**
     * Deprecated, is always equal to 0.
     *
     * @return current status
     */
    public synchronized int getCurrentStatus() {
        return rootNode.path("return").path(current_position).path("status")
                .asInt();
    }
}
