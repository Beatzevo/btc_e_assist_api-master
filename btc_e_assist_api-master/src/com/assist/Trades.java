package com.assist;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class Trades extends PublicBaseClass {
    private Iterator<JsonNode> itTrades;
    private JsonNode current_trade;

    public synchronized boolean runMethod() {
        return sendRequest("trades");
    }

    public synchronized void switchNextPair() {
        current_position = it.next();
        itTrades = rootNode.path(current_position).iterator();
    }

    public synchronized void setCurrentPair(String pair) {
        super.setCurrentPair(pair);
        itTrades = rootNode.path(current_position).iterator();
    }

    public synchronized boolean hasNextTrade() {
        if (itTrades != null)
            return itTrades.hasNext();
        return false;
    }

    public synchronized void switchNextTrade() {
        current_trade = itTrades.next();
    }

    public synchronized void switchResetTrade() {
        itTrades = rootNode.path(current_position).iterator();
    }

    public synchronized String getCurrentType() {
        return current_trade.path("type").asText();
    }

    public synchronized String getCurrentPrice() {
        return formatDouble(current_trade.path("price").asDouble());
    }

    public synchronized String getCurrentAmount() {
        return formatDouble(current_trade.path("amount").asDouble());
    }

    public synchronized String getCurrentTid() {
        return current_trade.path("tid").toString();
    }

    public synchronized String getCurrentTimestamp() {
        return current_trade.path("timestamp").toString();
    }
}
