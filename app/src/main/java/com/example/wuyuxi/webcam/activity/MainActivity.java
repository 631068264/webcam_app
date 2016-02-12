package com.example.wuyuxi.webcam.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.wuyuxi.webcam.R;

public class MainActivity extends Activity {
    private WebView webView;
    JavaScriptInterface mJavascriptInterface;
    public static final String appUrl = "http://192.168.1.106/webcam/app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webview);

        loadWebView(appUrl);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void loadWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);


        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        mJavascriptInterface = new JavaScriptInterface();
        webView.addJavascriptInterface(mJavascriptInterface, "ANDROIDAPI");

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void playVideo(String url) {
            VideoActivity.launch(MainActivity.this, url);
        }
    }


    class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
           
            Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
        }
    }


}


