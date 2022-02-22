package com.adrcotfas.minibrowser.ui.main

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView

class MiniWebChromeClient internal constructor(private val listener: Listener) : WebChromeClient() {
    internal interface Listener {
        fun onUrlChanged(url: String?)
        fun onProgressChanged(progress: Int)
    }

    override fun onProgressChanged(view: WebView, progress: Int) {
        listener.onProgressChanged(progress)
        if (progress == 100) {
            listener.onUrlChanged(view.url)
        }
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
    }
}