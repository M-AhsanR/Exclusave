package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.exclusave.BaseClass;
import com.exclusave.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        BaseClass.showCustomLoader(WebViewActivity.this);
        initilizeViews();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String weburl = "";
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            weburl = bundle.getString("WEBURL");
        }else {
            Toast.makeText(WebViewActivity.this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show();
            BaseClass.dialog.dismiss();
            finish();
        }

        if (!weburl.isEmpty()) {
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(weburl);
            BaseClass.dialog.dismiss();
        } else {
            Toast.makeText(WebViewActivity.this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show();
            BaseClass.dialog.dismiss();
            finish();
        }
    }

    private void initilizeViews() {
        webView = findViewById(R.id.webView);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
