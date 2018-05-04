package com.assist;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class Depth extends PublicBaseClass {
    private Iterator<JsonNode> itAsks;
    private Iterator<JsonNode> itBids;
    private JsonNode current_array_asks;
    private JsonNode current_array_bids;

    public synchronized boolean runMethod() {
        return sendRequest("depth");
    }

    public synchronized void switchNextPair() {
        current_position = it.next();
        itAsks = rootNode.path(current_position).path("asks").iterator();
        itBids = rootNode.path(current_position).path("bids").iterator();
    }

    public synchronized void setCurrentPair(String pair) {
        super.setCurrentPair(pair);
        itAsks = rootNode.path(current_position).path("asks").iterator();
        itBids = rootNode.path(current_position).path("bids").iterator();
    }

    public synchronized boolean hasNextAsk() {
        if (itAsks != null)
            return itAsks.hasNext();
        return false;
    }

    public synchronized boolean hasNextBid() {
        if (itBids != null)
            return itBids.hasNext();
        return false;
    }

    public synchronized void switchNextAsk() {
        if (itAsks != null) {
            current_array_asks = itAsks.next();
        }
    }

    public synchronized void switchNextBid() {
        if (itBids != null) {
            current_array_bids = itBids.next();
        }
    }

    public synchronized void switchResetAsk() {
        itAsks = rootNode.path(current_position).path("asks").iterator();
    }

    public synchronized void switchResetBid() {
        itBids = rootNode.path(current_position).path("bids").iterator();
    }

    public synchronized String getCurrentAskPrice() {
        return formatDouble(current_array_asks.path(0).asDouble());
    }

    public synchronized String getCurrentAskAmount() {
        return formatDouble(current_array_asks.path(1).asDouble());
    }

    public synchronized String getCurrentBidPrice() {
        return formatDouble(current_array_bids.path(0).asDouble());
    }

    public synchronized String getCurrentBidAmount() {
        return formatDouble(current_array_bids.path(1).asDouble());
    }
}
