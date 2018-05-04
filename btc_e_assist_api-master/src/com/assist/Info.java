package com.assist;

import java.util.ArrayList;
import java.util.Iterator;

public class Info extends PublicBaseClass {
    public synchronized boolean runMethod() {
        sendRequest("info");
        try {
            it = rootNode.path("pairs").fieldNames();
        } catch (Exception e) {
            it = null;
        }
        return success;
    }

    /**
     * Method is not allowed for this class
     */
    public synchronized void addPair(String pair) {
    }

    /**
     * Method is not allowed for this class
     */
    public synchronized void setLimit(int count) {
    }

    /**
     * Method is not allowed for this class
     */
    public synchronized void setIgnoreInvalid(boolean isIgnoreInvalid) {
    }

    /**
     * Method is not allowed for this class
     */
    public synchronized void resetParams() {
    }

    public synchronized void switchResetPair() {
        it = rootNode.path("pairs").fieldNames();
    }

    public synchronized String getServerTime() {
        return rootNode.path("server_time").toString();
    }

    public synchronized int getCurrentDecimalPlaces() {
        return rootNode.path("pairs").path(current_position)
                .path("decimal_places").asInt();
    }

    public synchronized Double getCurrentMinPrice() {
        return rootNode.path("pairs").path(current_position).path("min_price")
                .asDouble();
    }

    public synchronized Double getCurrentMaxPrice() {
        return rootNode.path("pairs").path(current_position).path("max_price")
                .asDouble();
    }

    public synchronized Double getCurrentMinAmount() {
        return rootNode.path("pairs").path(current_position).path("min_amount")
                .asDouble();
    }

    public synchronized Boolean getCurrentHidden() {
        return rootNode.path("pairs").path(current_position).path("hidden")
                .asBoolean();
    }

    public synchronized Double getCurrentFee() {
        return rootNode.path("pairs").path(current_position).path("fee")
                .asDouble();
    }

    public synchronized ArrayList<String> getPairsList() {
        ArrayList<String> pairsList = new ArrayList<String>();
        Iterator<String> pairsIt = rootNode.path("pairs").fieldNames();
        while (pairsIt.hasNext()) {
            pairsList.add(convertPairName(pairsIt.next()));
        }
        return pairsList;
    }
}
