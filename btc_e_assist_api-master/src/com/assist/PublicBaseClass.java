package com.assist;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

abstract class PublicBaseClass extends CommonClass {
    protected Iterator<String> it;
    protected String current_position;
    private StringBuilder paramsBuf;
    private StringBuilder address;
    protected boolean success;
    private boolean isLimit;
    private boolean isIgnoreInvalid;
    private StringBuilder limitString;
    private String clientName = "Assist TradeApi";

    private static final String limit = "limit=";
    private static final String ignoreInvalid = "ignore_invalid=1";
    private static final String API_URL = "/api/3/";

    PublicBaseClass() {
        paramsBuf = new StringBuilder("/");
        address = new StringBuilder();
        limitString = new StringBuilder();
        isLimit = false;
        isIgnoreInvalid = true;
    }

    /**
     * Send request.
     *
     * @return TRUE if positive answer is received. FALSE if has ANY trouble. If
     * getErrorMessage().length()==0 it's network troubles.
     */
    public synchronized boolean runMethod() {
        return false;
    }

    public synchronized void addPair(String pair) {
        paramsBuf.append(pair.replace('-', '_').toLowerCase()).append("-");
    }

    public synchronized void setLimit(int count) {
        limitString.delete(0, limitString.length());
        limitString.append(limit);
        limitString.append(String.valueOf(count));
        isLimit = true;
    }

    /**
     * This method set "ignore_invalid" parameter. Default state is
     * "ignore_invalid=1"
     */
    public synchronized void setIgnoreInvalid(boolean isIgnoreInvalid) {
        this.isIgnoreInvalid = isIgnoreInvalid;
    }

    /**
     * Reset all parameters (addPair, Limit, ignoreInvalid);
     */
    public synchronized void resetParams() {
        paramsBuf.delete(0, paramsBuf.length());
        paramsBuf.append("/");
        isLimit = false;
        isIgnoreInvalid = true;
    }

    /**
     * You can set data for object manually, if need
     *
     * @param json in JSON format
     * @return true if this data success=1
     */
    public synchronized boolean setData(String json) {
        try {
            rootNode = mapper.readTree(json);
            it = rootNode.fieldNames();
            if (it != null) {
                if (!rootNode.path("success").toString().equals("0")) {
                    localTimestamp = System.currentTimeMillis();
                    success = true;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        makeDefaultRootNode();
        success = false;
        it = null;
        localTimestamp = 0;
        return false;
    }

    protected boolean sendRequest(String name) {
        try {
            address.delete(0, address.length());
            address.append(Mirrors.getMirror());
            address.append(API_URL);
            address.append(name);
            address.append(paramsBuf);
            if (isLimit && isIgnoreInvalid) {
                address.append("?");
                address.append(limitString);
                address.append("&");
                address.append(ignoreInvalid);
            } else if (isLimit) {
                address.append("?");
                address.append(limitString);
            } else if (isIgnoreInvalid) {
                address.append("?");
                address.append(ignoreInvalid);
            }
            URL url = new URL(address.toString());
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", clientName);
            connection.setConnectTimeout(connectTimeoutMillis);
            connection.setReadTimeout(readTimeoutMillis);
            rootNode = null;
            it = null;
            localTimestamp = 0;
            success = false;
            try {
                rootNode = mapper.readTree(connection.getInputStream());
            } catch (IOException e) {
                String proxyUrl = ProxyHook.getProxyUrl(address.toString());
                rootNode = mapper.readTree(ProxyHook.loadContent(proxyUrl));
            }
            if (!rootNode.path("success").toString().equals("0")) {
                it = rootNode.fieldNames();
                if (it != null) {
                    localTimestamp = System.currentTimeMillis();
                    success = true;
                    return true;
                }
            } else if (getErrorMessage().length() != 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        makeDefaultRootNode();
        return false;
    }

    /**
     * Error message from server if success=0 and it's not connection trouble.
     *
     * @return error message
     */
    public synchronized String getErrorMessage() {
        return rootNode.path("error").asText();
    }

    public synchronized boolean isSuccess() {
        return success;
    }

    public synchronized boolean hasNextPair() {
        if (it != null) {
            return it.hasNext();
        }
        return false;
    }

    public synchronized void switchNextPair() {
        if (it != null) {
            current_position = it.next();
        }
    }

    public synchronized void switchResetPair() {
        it = rootNode.fieldNames();
    }

    public synchronized void setCurrentPair(String pair) {
        current_position = pair.replace('-', '_').toLowerCase();
    }

    public synchronized String getCurrentPairName() {
        return convertPairName(current_position);
    }

    public synchronized void setClientName(String name) {
        clientName = name;
    }
}
