package com.assist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class help you find BTC-E mirror
 */
public class Mirrors {
    public static final String[] mirrors = {"https://btc-e.com", "https://btc-e.nz"};
    private static int currentMirrorIndex = -1;


    /**
     * @return working mirror or null
     */
    public static String getMirror() {
        if (currentMirrorIndex >= 0) {
            return mirrors[currentMirrorIndex];
        } else {
            return selectMirror();
        }
    }

    private static String selectMirror() {
        Pattern pattern = Pattern.compile("\"server_time\":");
        for (int i = 0; i < mirrors.length; i++) {
            try {
                String mirror = mirrors[i];
                mirror += "/api/3/info";
                String content = ProxyHook.loadContent(mirror);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    currentMirrorIndex = i;
                    return mirrors[currentMirrorIndex];
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
