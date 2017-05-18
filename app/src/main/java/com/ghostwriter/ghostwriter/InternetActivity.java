package com.ghostwriter.ghostwriter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class InternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);


        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.loadUrl("http://developers.daum.net/");
    }
}
