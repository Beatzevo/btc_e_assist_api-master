package com.assist;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final public class PrivateNetwork {
    private StringCrypter userEncryptKeys;
    private String apiKey = "";
    private boolean flagTryProxyHook = false;
    private Mac mac;
    private MessageDigest md;

    private static final String API_URL = "/tapi";
    private static final String TARGET_URL = "https://btc-e.com/tapi";
    private String clientName = "Assist TradeApi";

    public PrivateNetwork() {
        try {
            mac = Mac.getInstance("HMACSHA512");
            userEncryptKeys = new StringCrypter();
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ignored) {
        }

    }

    /**
     * Encode string to DESede with SHA-1 from 'key' (key->SHA-1->DESede)
     *
     * @param sourceStr original string
     * @param userKey   key for encoding
     * @return encoded string
     * @throws Exception
     */
    public synchronized String encodeStr(String sourceStr, String userKey)
            throws Exception {
        userEncryptKeys.setKey(hashStr(userKey));
        return userEncryptKeys.encrypt(sourceStr);
    }

    /**
     * Decode string from DESede
     *
     * @param encodeStr encoded string
     * @param userKey   key for decoding
     * @return decoded string
     * @throws Exception
     */
    public synchronized String decodeStr(String encodeStr, String userKey)
            throws Exception {
        userEncryptKeys.setKey(hashStr(userKey));
        return userEncryptKeys.decrypt(encodeStr);
    }

    public synchronized void setKeys(String aKey, String aSecret)
            throws Exception {
        if (aKey.length() > 0 && aSecret.length() > 0) {
            apiKey = aKey;
            SecretKeySpec key = new SecretKeySpec(aSecret.getBytes("UTF-8"),
                    "HmacSHA512");
            mac.init(key);
        } else {
            throw new Exception("Invalid key or secret");
        }
    }

    public synchronized void setKeys(String encodedKey, String encodedSecret,
                                     String decodeKey) throws Exception {
        userEncryptKeys.setKey(hashStr(decodeKey));
        apiKey = userEncryptKeys.decrypt(encodedKey);
        SecretKeySpec key = new SecretKeySpec(userEncryptKeys.decrypt(
                encodedSecret).getBytes("UTF-8"), "HmacSHA512");
        mac.init(key);
    }

    public synchronized boolean isKeysInstalled() {
        return (apiKey.length() > 0 && mac.getMacLength() > 0);
    }

    public synchronized void resetInstalledKeys() {
        apiKey = "";
        try {
            mac = Mac.getInstance("HMACSHA512");
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public synchronized void setClientName(String name) {
        clientName = name;
    }

    private String hashStr(String str) throws Exception {
        StringBuilder hashCode = new StringBuilder();
        byte[] digest = md.digest(str.getBytes());
        for (byte aDigest : digest) {
            hashCode.append(Integer.toHexString(0x0100 + (aDigest & 0x00FF))
                    .substring(1));
        }
        return hashCode.toString();
    }

    InputStream sendRequest(String name, String params, int connectMillis, int readMillis) {
        try {
            Long nonce = System.currentTimeMillis() / 100L - 14247195500L;
            String postData = "method=" + name + "&" + params + "nonce="
                    + nonce.toString();
            HttpURLConnection connection;
            if (flagTryProxyHook) {
                String proxyUrl = ProxyHook.getProxyUrl(TARGET_URL);
                connection = (HttpURLConnection) new URL(proxyUrl).openConnection();
            } else {
                connection = (HttpURLConnection) new URL(Mirrors.getMirror() + API_URL).openConnection();
            }
            connection.setConnectTimeout(connectMillis);
            connection.setReadTimeout(readMillis);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", clientName);
            connection.setRequestProperty("Key", apiKey);
            connection.setRequestProperty("Sign", new String(Hex.encodeHex(mac.doFinal(postData.getBytes("UTF-8")))));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.flush();
            os.close();
            return connection.getInputStream();
        } catch (Exception e) {
            flagTryProxyHook = !flagTryProxyHook;
            if (flagTryProxyHook) {
                return sendRequest(name, params, connectMillis, readMillis);
            } else {
                e.printStackTrace();
                return null;
            }
        }
    }
}
