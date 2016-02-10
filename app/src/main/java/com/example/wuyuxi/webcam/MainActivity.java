package com.example.wuyuxi.webcam;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    JavascriptInterface mJavascriptInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        mJavascriptInterface = new JavascriptInterface();
        webView.addJavascriptInterface(mJavascriptInterface, "ANDROIDAPI");

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl("http://192.168.1.107/webcam/app");
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void playVideo(String url) {
            VideoActivity.launch(MainActivity.this, url);
        }
    }


    class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}


