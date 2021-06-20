package com.adrcotfas.minibrowser.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adrcotfas.minibrowser.R;
import com.adrcotfas.minibrowser.databinding.ActivityMainBinding;
import com.adrcotfas.minibrowser.ui.UrlViewModel;
import com.adrcotfas.minibrowser.ui.history.HistoryDialog;
import com.adrcotfas.minibrowser.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

import static com.adrcotfas.minibrowser.utils.Utils.addUrlPrefix;
import static com.adrcotfas.minibrowser.utils.Utils.getSearchUrl;
import static com.adrcotfas.minibrowser.utils.Utils.stripHostPrefix;

@AndroidEntryPoint
public class BrowserActivity extends AppCompatActivity {

    private static final String TAG = "BrowserActivity";

    private UrlViewModel viewModel;

    private ActivityMainBinding binding;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(UrlViewModel.class);
        binding.setViewmodel(viewModel);
        setupEditText();
        setupWebView();
        setupImageLoadToggleButton();
        setupHistoryButton();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView = binding.webView;
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings settings = webView.getSettings();
        boolean loadImagesFlag = viewModel.getLoadImages();
        settings.setBlockNetworkImage(loadImagesFlag);
        settings.setLoadsImagesAutomatically(!loadImagesFlag);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new MiniWebChromeClient(new MiniWebChromeClient.Listener() {
            @Override
            public void onUrlChanged(String url) {
                    binding.editText.setText(url);
                    try {
                        URL u = new URL(webView.getUrl());
                        String host = stripHostPrefix(u.getHost());
                        if (!host.isEmpty()) {
                            Executors.newSingleThreadExecutor().execute(() -> viewModel.insert(host));
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
            }
            @Override
            public void onProgressChanged(int progress) {
                binding.progress.setProgress(progress);
                if (progress == 100) {
                    binding.progress.setVisibility(View.INVISIBLE);
                }
            }
        }));
    }

    private void setupEditText() {
        binding.editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                String userInput = binding.editText.getText().toString();

                // for simplicity's sake, redirect input not containing "." to a search engine
                // else add the required prefix
                String url = (!userInput.contains(".")) ? getSearchUrl(userInput): addUrlPrefix(userInput);

                if (!url.isEmpty()) {
                    binding.progress.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                    hideKeyboard();
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Toggle image requests blocking and persist the last value
     */
    private void setupImageLoadToggleButton() {
        binding.buttonToggleImages.parent.setOnClickListener(v -> {
            boolean flag = !webView.getSettings().getBlockNetworkImage();
            Log.d(TAG, "toggle image loading: " + flag);
            webView.getSettings().setBlockNetworkImage(flag);
            webView.getSettings().setLoadsImagesAutomatically(!flag);
            viewModel.setLoadImages(flag);
            updateLoadImagesButton(!flag);
            webView.reload();
        });
    }

    private void updateLoadImagesButton(boolean flag) {
        binding.buttonToggleImages.buttonEdit.setImageDrawable(
                ResourcesCompat.getDrawable(getResources(),
                        flag ? R.drawable.ic_image : R.drawable.ic_image_off, null));
    }

    private void setupHistoryButton() {
        binding.buttonShowHistory.parent.setOnClickListener(
                v -> HistoryDialog.newInstance(url -> webView.loadUrl(Utils.addUrlPrefix(url)))
                        .show(getSupportFragmentManager(), null));
    }

    /**
     * Handle back navigation in the WebView
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
