package com.adrcotfas.minibrowser.ui.main

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout

class MiniWebChromeClient internal constructor(
    private val listener: Listener,
    private val frameLayout: FrameLayout,
) : WebChromeClient() {
    internal interface Listener {
        fun onUrlChanged(url: String?)
        fun onProgressChanged(progress: Int)
    }

    private var customView: View? = null

    override fun onProgressChanged(view: WebView, progress: Int) {
        listener.onProgressChanged(progress)
        if (progress == 100) {
            listener.onUrlChanged(view.url)
        }
    }

    override fun onHideCustomView() {
        customView?.let { frameLayout.removeView(it) }
        customView = null
    }

    override fun onShowCustomView(
        paramView: View,
        paramCustomViewCallback: CustomViewCallback
    ) {

        onHideCustomView()
        customView = paramView
        frameLayout.addView(
            customView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        paramView.requestFocus()
    }
}