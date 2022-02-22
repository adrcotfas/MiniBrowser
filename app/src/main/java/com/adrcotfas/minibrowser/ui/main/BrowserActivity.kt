package com.adrcotfas.minibrowser.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.adrcotfas.minibrowser.R
import com.adrcotfas.minibrowser.databinding.ActivityMainBinding
import com.adrcotfas.minibrowser.ui.UrlViewModel
import com.adrcotfas.minibrowser.ui.history.HistoryDialog
import com.adrcotfas.minibrowser.ui.history.HistoryDialog.Companion.newInstance
import com.adrcotfas.minibrowser.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() {
    private lateinit var viewModel: UrlViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        binding.viewmodel = viewModel
        setupEditText()
        setupWebView()
        if (savedInstanceState == null) {
            webView.loadUrl("https://youtube.com")
        }
        setupImageLoadToggleButton()
        setupHistoryButton()
        hideSystemBars()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val settings = webView.settings
        val loadImagesFlag = viewModel.loadImages
        settings.apply {
            userAgentString =
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0"
            blockNetworkImage = loadImagesFlag
            loadsImagesAutomatically = !loadImagesFlag
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        webView.webChromeClient = MiniWebChromeClient(object : MiniWebChromeClient.Listener {
            override fun onUrlChanged(url: String?) {
                if (url.isNullOrEmpty()) {
                    return
                }
                binding.editText.setText(url)
                try {
                    val u = URL(webView.url)
                    val host = Utils.stripHostPrefix(u.host)
                    if (host.isNotEmpty()) {
                        Executors.newSingleThreadExecutor().execute { viewModel.insert(host) }
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }
            }

            override fun onProgressChanged(progress: Int) {
                binding.progress.progress = progress
                if (progress == 100) {
                    binding.progress.visibility = View.INVISIBLE
                }
            }
        }, binding.videoLayout)
    }

    private fun setupEditText() {
        binding.editText.setOnKeyListener { _: View?, keyCode: Int, _: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val userInput = binding.editText.text.toString()

                // for simplicity's sake, redirect input not containing "." to a search engine
                // else add the required prefix
                val url =
                    if (!userInput.contains(".")) Utils.getSearchUrl(userInput) else Utils.addUrlPrefix(
                        userInput
                    )
                if (url.isNotEmpty()) {
                    binding.progress.visibility = View.VISIBLE
                    webView.loadUrl(url)
                    hideKeyboard()
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    /**
     * Toggle image requests blocking and persist the last value
     */
    private fun setupImageLoadToggleButton() {
        binding.buttonToggleImages.parent.setOnClickListener { v: View? ->
            val flag = !webView.settings.blockNetworkImage
            Log.d(TAG, "toggle image loading: $flag")
            webView.settings.blockNetworkImage = flag
            webView.settings.loadsImagesAutomatically = !flag
            viewModel.loadImages = flag
            updateLoadImagesButton(!flag)
            webView.reload()
        }
    }

    private fun updateLoadImagesButton(flag: Boolean) {
        binding.buttonToggleImages.buttonEdit.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (flag) R.drawable.ic_image else R.drawable.ic_image_off, null
            )
        )
    }

    private fun setupHistoryButton() {
        binding.buttonShowHistory.parent.setOnClickListener {
            newInstance(
                object : HistoryDialog.Listener {
                    override fun onClicked(url: String) {
                        webView.loadUrl(Utils.addUrlPrefix(url))
                    }
                })
                .show(supportFragmentManager, null)
        }
    }

    /**
     * Handle back navigation in the WebView
     */
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }


    companion object {
        private const val TAG = "BrowserActivity"
    }
}