package com.assist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

abstract class CommonClass {
    public static final int CONNECT_TIMEOUT_MILLIS = 5000;
    public static final int READ_TIMEOUT_MILLIS = 10000;
    private NumberFormat formatter;
    private DecimalFormatSymbols sep;
    protected JsonNode rootNode;
    protected ObjectMapper mapper;
    protected int connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS;
    protected int readTimeoutMillis = READ_TIMEOUT_MILLIS;
    static final String FORMAT_STR = "#.########";
    static final int FORMAT_STR_POINT_POSITION = 2;
    protected long localTimestamp = 0;

    protected void makeDefaultRootNode() {
        try {
            rootNode = mapper.readTree("{\"success\":0}");
        } catch (Exception e) {
        }
    }

    protected CommonClass() {
        mapper = new ObjectMapper();
        sep = new DecimalFormatSymbols();
        sep.setDecimalSeparator('.');
        formatter = new DecimalFormat(FORMAT_STR, sep);
        makeDefaultRootNode();
    }

    /**
     * JSON data received from server
     */
    public String toString() {
        return rootNode.toString();
    }

    /**
     * Time stamp when data was received on this system
     *
     * @return Unix time in milliseconds
     */
    public synchronized long getLocalTimestamp() {
        return localTimestamp;
    }

    /**
     * Set connection and read timeouts for this object.
     *
     * @param connectMillis connection timeout in milliseconds
     * @param readMillis    read timeout in milliseconds
     */
    public synchronized void setTimeouts(int connectMillis, int readMillis) {
        connectTimeoutMillis = connectMillis;
        readTimeoutMillis = readMillis;
    }

    /**
     * Reset connection and read timeouts for this object.
     */
    public synchronized void resetTimeouts() {
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS;
        readTimeoutMillis = READ_TIMEOUT_MILLIS;
    }

    /**
     * Set count of decimal places for all numbers methods in this object.
     *
     * @param placesCount count of places after point
     */
    public synchronized void setDecimalPlaces(int placesCount) {
        if (placesCount >= 0 && placesCount < 8) {
            if (placesCount != 0) {
                formatter = new DecimalFormat(FORMAT_STR.substring(0,
                        FORMAT_STR_POINT_POSITION + placesCount), sep);
            } else {
                formatter = new DecimalFormat("#", sep);
            }
        }
    }

    public synchronized void setDefaultDecimalPlaces() {
        formatter = new DecimalFormat(FORMAT_STR, sep);
    }

    /**
     * Convert double from exponential to decimal format
     */
    protected String formatDouble(double src) {
        return formatter.format(src);
    }

    protected static String convertPairName(String pairName) {
        return pairName.toUpperCase().replace('_', '-');
    }

}
