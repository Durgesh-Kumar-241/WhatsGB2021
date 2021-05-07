package com.dktechhub.mnnit.ee.whatsappweb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

public class InstagramActivity extends AppCompatActivity {
    ProgressBar pbar;
    mWebView wb;
    PermissionDetector permissionDetector;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.fragment_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        permissionDetector=new PermissionDetector(this);
        ConnectionDetector.checkInternet(this);
        permissionDetector.checkPermissions();
        wb = new mWebView(findViewById(R.id.webView));
        pbar = findViewById(R.id.progressBar);
        wb.setmWebViewListner(new mWebView.mWebViewInterface() {
            @Override
            public ProgressBar getProgressBar() {
                return pbar;
            }

            @Override
            public Activity getActivity() {
                return InstagramActivity.this;
            }
        });

wb.InitSettings(false,"*/*");


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        //super.onActivityResult();
        super.onActivityResult(requestCode, resultCode, data);
        this.wb.onActivityResult(requestCode, resultCode, data);

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionDetector.onRequestPermissionsResult( requestCode, permissions,  grantResults);
    }

    /*
    public void checkInternet()
    {
        if(!ConnectionDetector.isInternetAvailable(this))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Connect to internet first");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InstagramActivity.this.onBackPressed();
                }
            });

        }
    }

     */
}