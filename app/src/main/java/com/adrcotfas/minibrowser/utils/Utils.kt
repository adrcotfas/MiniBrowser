package com.adrcotfas.minibrowser.utils

object Utils {
    fun addUrlPrefix(url_: String): String {
        var url = url_
        if (!url.startsWith("https://")) {
            if (!url.startsWith("www.")) {
                url = "www.$url"
            }
            url = "https://$url"
        }
        return url
    }

    fun getSearchUrl(url: String): String {
        return "https://duckduckgo.com/?q=$url"
    }

    fun stripHostPrefix(url_: String): String {
        var url = url_
        val www = "www."
        if (url.startsWith(www)) {
            url = url.substring(www.length)
        }
        val mobile = "m."
        if (url.startsWith(mobile)) {
            url = url.substring(mobile.length)
        }
        return url
    }
}