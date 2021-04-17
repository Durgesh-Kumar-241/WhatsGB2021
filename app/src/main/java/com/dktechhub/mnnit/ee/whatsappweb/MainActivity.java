package com.dktechhub.mnnit.ee.whatsappweb;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressBar pbar;
    WebView wb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {
            wb = findViewById(R.id.webView);
            pbar = findViewById(R.id.progressBar);
            wb.setWebChromeClient(new WebChromeClient());
            wb.setWebViewClient(new webClient());
            wb.getSettings().setLoadWithOverviewMode(true);
            wb.getSettings().setJavaScriptEnabled(true);
            wb.getSettings().setBuiltInZoomControls(true);
            wb.getSettings().setSavePassword(true);
            wb.getSettings().supportMultipleWindows();
            wb.getSettings().setDomStorageEnabled(true);
            wb.getSettings().setAllowFileAccess(true);
            wb.setSoundEffectsEnabled(true);


            wb.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.76");
            HashMap<String, String> headers = new HashMap<>();
            headers.put("authority", "web.whatsapp.com");
            headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            headers.put("accept-encoding", "gzip, deflate, br");
            headers.put("accept-language", "en-US,en;q=0.9,hi;q=0.8");
            headers.put("cookie", "wa_lang_pref=en; wa_csrf=Dro3IjzhVOzGj-XaTGtozR");
            headers.put("sec-fetch-dest", "document");
            headers.put("sec-fetch-mode", "navigate");
            headers.put("sec-fetch-site", "none");
            headers.put("sec-fetch-user", "?1");
            headers.put("upgrade-insecure-requests", "1");
            headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.76");
            wb.loadUrl("https://web.whatsapp.com", headers);
        }
        else
            wb.restoreState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(newProgress);
        }


    }
    public class webClient extends WebViewClient{
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

    @Override
    public void onBackPressed() {

        if(wb.canGoBack())
        {
            wb.goBack();
        }
        else super.onBackPressed();
    }
}