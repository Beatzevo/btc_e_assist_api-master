package com.assist;

public class TransHistory extends PrivateMultipleResponse {
    public TransHistory(PrivateNetwork nt) {
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

    public synchronized boolean runMethod() {
        setData(network.sendRequest("TransHistory", getParams(),
                connectTimeoutMillis, readTimeoutMillis));
        return success;
    }

    public synchronized int getCurrentType() {
        return rootNode.path("return").path(current_position).path("type")
                .asInt();
    }

    public synchronized String getCurrentCurrency() {
        return rootNode.path("return").path(current_position).path("currency")
                .asText();
    }

    public synchronized String getCurrentDesc() {
        return rootNode.path("return").path(current_position).path("desc")
                .asText().replaceFirst(":order:", "").replace(":", "");
    }

    public synchronized int getCurrentStatus() {
        return rootNode.path("return").path(current_position).path("status")
                .asInt();
    }

    public synchronized String getCurrentTimestamp() {
        return rootNode.path("return").path(current_position).path("timestamp")
                .toString();
    }

}
