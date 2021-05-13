package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainFragment extends AppCompatActivity {


    ProgressBar pbar;
    mWebView webView;







    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.fragment_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ConnectionDetector.checkInternet(this);
        checkPermissions();
        pbar = findViewById(R.id.progressBar);


        this.webView = new mWebView(findViewById(R.id.webView));
        webView.setmWebViewListner(new mWebView.mWebViewInterface() {
            @Override
            public ProgressBar getProgressBar() {
                return pbar;
            }

            @Override
            public Activity getActivity() {
                return MainFragment.this;
            }
        });
        webView.InitSettings(true,"*/*");
        String encriptedText = encriptedText();
        StringBuilder sb = new StringBuilder();
        sb.append("https://" + encriptedText + "/🌐/");
        sb.append(Locale.getDefault().getLanguage());
        this.webView.loadUrl(sb.toString());
       /*
        this.webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                checkPermissions();
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(mimetype);
                    //------------------------COOKIE!!------------------------
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    //------------------------COOKIE!!------------------------
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainFragment.this, "Download failed", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        // wb.loadUrl("https://web.whatsapp.com", headers);
    }


    private String encriptedText() {
        String str = "";
        for (int i = 0; i < 16; i++) {
            str = str + ((char) ("uc`,uf_rq_nn,amk".charAt(i) + 2));
        }
        return str;
    }

    long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time < 1000) {
            super.onBackPressed();
        } else {
            time = System.currentTimeMillis();
            if (webView.canGoBack())
                webView.goBack();

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        //super.onActivityResult();
        super.onActivityResult(requestCode, resultCode, data);
        this.webView.onActivityResult(requestCode, resultCode, data);

    }

    public void applyTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);
        if(dark_theme)
        {
            //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else setTheme(R.style.Theme_MaterialComponents_Light);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denined,Reading external storage is necessary for the app to work properly", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;
    public void checkPermissions()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},WRITE_EXTERNAL_STORAGE_CODE);
        }


    }

}