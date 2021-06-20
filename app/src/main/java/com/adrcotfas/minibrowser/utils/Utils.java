package com.adrcotfas.minibrowser.utils;

public class Utils {

    public static String addUrlPrefix(String url) {
        if (!url.startsWith("https://")) {
            if (!url.startsWith("www.")) {
                url = "www." + url;
            }
            url = "https://" + url;
        }
        return url;
    }

    public static String getSearchUrl(String url) {
        return "https://duckduckgo.com/?q=" + url;
    }

    public static String stripHostPrefix(String url) {
        String www = "www.";
        if (url.startsWith(www)) {
            url = url.substring(www.length());
        }
        String mobile = "m.";
        if (url.startsWith(mobile)) {
            url = url.substring(mobile.length());
        }
        return url;
    }
}
