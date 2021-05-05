package com.dktechhub.mnnit.ee.whatsappweb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

public class InstagramActivity extends AppCompatActivity {
    ProgressBar pbar;
    WebView wb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.fragment_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        wb = findViewById(R.id.webView);
        pbar = findViewById(R.id.progressBar);

        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(new webClient());
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        // wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setSavePassword(true);
        wb.getSettings().supportMultipleWindows();
        wb.getSettings().setDomStorageEnabled(true);
        wb.getSettings().setAllowFileAccess(true);
        wb.getSettings().setSupportZoom(true);
        wb.setSoundEffectsEnabled(true);

        //wb.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.76");

        //headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.76");
        wb.loadUrl("https://www.instagram.com");

    }


    long time=0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-time<1000)
        {
            super.onBackPressed();
        }else {
            time=System.currentTimeMillis();
            if(wb.canGoBack())
                wb.goBack();

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }

    }

    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(newProgress);
        }


    }
    public class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView webView,String url)
        {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbar.setVisibility(View.GONE);
        }
    }

    public void applyTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);
        if(dark_theme)
        {
            //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else setTheme(R.style.Theme_AppCompat_Light);
    }
}