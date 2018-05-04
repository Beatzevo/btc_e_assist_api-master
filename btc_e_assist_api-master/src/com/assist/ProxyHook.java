package com.assist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyHook {
    private static final String userAgent = "Assist TradeApi";


    public static String getProxyUrl(String originalUrl) {
        String zeroUrl = getZeroUrl(originalUrl);
        return getFirstUrl(zeroUrl);
    }

    private static String getZeroUrl(String originalUrl) {
        return "http://noblockme.ru/api/anonymize?url=" + originalUrl;
    }

    private static String getFirstUrl(String zeroUrl) {
        String contentString = loadContent(zeroUrl);
        if (contentString == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("http://.*\",").matcher(contentString);
        if (!matcher.find()) {
            return null;
        }
        String firstUrl = matcher.group();
        return firstUrl.replace("\",", "");
    }

    public static String loadContent(String link) {
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

}
