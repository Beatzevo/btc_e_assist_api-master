package com.assist;

public class TradeHistory extends PrivateMultipleResponse {
    public TradeHistory(PrivateNetwork nt) {
        network = nt;
    }

    /**
     * default 0
     *
     * @param str
     */
    public synchronized void setFrom(String str) {
        paramsMap.put("from", str);
    }

    /**
     * default 1000
     *
     * @param str
     */
    public synchronized void setCount(String str) {
        paramsMap.put("count", str);
    }

    public synchronized void setFrom_id(String str) {
        paramsMap.put("from_id", str);
    }

    public synchronized void setEnd_id(String str) {
        paramsMap.put("end_id", str);
    }

    /**
     * DESC or ASC sort type, default DESC
     *
     * @param str
     * @throws NoSuchMethodException
     */
    public synchronized void setOrder(String str) {
        paramsMap.put("order", str.toUpperCase());
    }

    public synchronized void setSince(String str) {
        paramsMap.put("since", str);
    }

    public synchronized void setEnd(String str) {
        paramsMap.put("end", str);
    }

    public synchronized void setPair(String str) {
        paramsMap.put("pair", str.replace('-', '_').toLowerCase());
    }

    public synchronized boolean runMethod() {
        setData(network.sendRequest("TradeHistory", getParams(),
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

    public synchronized String getCurrentOrder_id() {
        return rootNode.path("return").path(current_position).path("order_id")
                .toString();
    }

    public synchronized int getCurrentIs_Your_Order() {
        return rootNode.path("return").path(current_position)
                .path("is_your_order").asInt();
    }

    public synchronized String getCurrentTimestamp() {
        return rootNode.path("return").path(current_position).path("timestamp")
                .toString();
    }
}
