package com.assist;

public class Ticker extends PublicBaseClass {
    /**
     * Method is not allowed for this class
     */
    public synchronized void setLimit(int count) {
    }

    public synchronized boolean runMethod() {
        return sendRequest("ticker");
    }

    public synchronized String getCurrentHigh() {
        return formatDouble(rootNode.path(current_position).path("high")
                .asDouble());
    }

    public synchronized String getCurrentLow() {
        return formatDouble(rootNode.path(current_position).path("low")
                .asDouble());
    }

    public synchronized String getCurrentAvg() {
        return formatDouble(rootNode.path(current_position).path("avg")
                .asDouble());
    }

    public synchronized String getCurrentVol() {
        return formatDouble(rootNode.path(current_position).path("vol")
                .asDouble());
    }

    public synchronized String getCurrentVolCur() {
        return formatDouble(rootNode.path(current_position).path("vol_cur")
                .asDouble());
    }

    public synchronized String getCurrentLast() {
        return formatDouble(rootNode.path(current_position).path("last")
                .asDouble());

    }

    public synchronized String getCurrentBuy() {
        return formatDouble(rootNode.path(current_position).path("buy")
                .asDouble());

    }

    public synchronized String getCurrentSell() {
        return formatDouble(rootNode.path(current_position).path("sell")
                .asDouble());
    }

    public synchronized String getCurrentUpdated() {
        return rootNode.path(current_position).path("updated").toString();
    }
}
