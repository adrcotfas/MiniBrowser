package com.adrcotfas.minibrowser.ui.main;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MiniWebChromeClient extends WebChromeClient {

    interface Listener {
        void onUrlChanged(String url);
        void onProgressChanged(int progress);
    }

    private final Listener listener;

    MiniWebChromeClient(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        listener.onProgressChanged(progress);
        if (progress == 100) {
            listener.onUrlChanged(view.getUrl());
        }
    }
}
