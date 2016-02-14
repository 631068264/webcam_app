package com.example.wuyuxi.webcam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.wuyuxi.webcam.R;
import com.example.wuyuxi.webcam.core.BaseActivity;
import com.example.wuyuxi.webcam.events.ClearCacheEvent;
import com.example.wuyuxi.webcam.events.UrlEvent;
import com.example.wuyuxi.webcam.fragment.TestDlgFragment;

import de.greenrobot.event.EventBus;

/**
 * @Annotation // webView页面
 */
public class MainActivity extends BaseActivity {
    private WebView mWebView;
    JavaScriptInterface mJavascriptInterface;
    String appUrl = "http://192.168.1.106/webcam/app";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        loadWebView(appUrl);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void loadWebView(String url) {
        appUrl = url;
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);


        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        mJavascriptInterface = new JavaScriptInterface();
        mWebView.addJavascriptInterface(mJavascriptInterface, "ANDROIDAPI");

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.loadUrl(url);
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void playDirectVideo(String url) {
            DirectVideoActivity.launch(MainActivity.this, url);
        }

        @JavascriptInterface
        public void playVideo(String url) {
            VideoActivity.launch(MainActivity.this, url);
        }

        @JavascriptInterface
        public void onBackButtonPress(boolean canBack) {
            if (!canBack) {
                MainActivity.this.finish();
            }
        }

        @JavascriptInterface
        public void clearCache() {
            EventBus.getDefault().post(new ClearCacheEvent());
        }

    }


    class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
        }

        public void onReceivedError(WebView v, int errorCode, String description, String failingUrl) {
            showDialog(TestDlgFragment.newInstance(appUrl));
            Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
        }
    }


    @Override //重写后推键
    public void onBackPressed() {
        mWebView.loadUrl("javascript: goBack()");
    }

    public void onEventMainThread(ClearCacheEvent event) {
        //一定UI线程进行 --另一种 mWebView.post(new Runable);
        mWebView.clearCache(true);
        mWebView.loadUrl(appUrl);
        Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
    }

    public void onEvent(UrlEvent event) {
        loadWebView(event.getUrl());
    }


}


