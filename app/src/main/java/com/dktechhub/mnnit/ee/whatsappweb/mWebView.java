package com.dktechhub.mnnit.ee.whatsappweb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class mWebView  {
    private ProgressBar progressBar;
    private mWebViewInterface mWebViewInterface;
    private Activity activity;
    private WebView webView;
    private String mime;
    private ValueCallback<Uri[]> mFilePathCallback;
    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    public mWebView(WebView webView) {
        this.webView=webView;
    }
    public void setmWebViewListner(mWebViewInterface mWebViewInterface1)
    {
        this.mWebViewInterface=mWebViewInterface1;
        this.progressBar=mWebViewInterface.getProgressBar();
        this.activity=mWebViewInterface.getActivity();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    if (data != null) {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                            Log.d("CustomChooserActivity", dataString);
                        }
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        Log.d("CustomChooserActivity", mCameraPhotoPath);
                    }
                    break;
            }
        }
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void InitSettings(Boolean requestDesktopSite,String mime)
    {   this.mime=mime;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new webClient());
        this.webView.getSettings().setSaveFormData(true);
        this.webView.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= 16) {
            this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        if(requestDesktopSite)
        {

            this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            this.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Win64; x64; rv:46.0) Gecko/20100101 Firefox/60.0");
        }
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setSupportMultipleWindows(true);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setNeedInitialFocus(false);
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setDatabaseEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setGeolocationEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        this.webView.setWebChromeClient(new WebChromeClient());
    }

    private Dialog dialog;
    private boolean resetCallback = true;
    private String mCameraPhotoPath;
    private void showChooserDialog() {
        if (dialog == null) {
            dialog = new Dialog(this.activity);
            dialog.setTitle("Choose a action");
            dialog.setContentView(R.layout.dialog_chooser_layout);


            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (resetCallback && mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                        mFilePathCallback = null;
                    }
                    resetCallback = true;
                }
            });

            dialog.findViewById(R.id.text_album).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetCallback = false;
                    dialog.dismiss();
                    Intent albumIntent = new Intent(Intent.ACTION_PICK);
                    albumIntent.setType(mime);
                    activity.startActivityForResult(albumIntent, REQUEST_CODE_ALBUM);
                }
            });
            dialog.findViewById(R.id.text_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetCallback = false;
                    dialog.dismiss();

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,BuildConfig.APPLICATION_ID+".provider", photoFile));

                            activity.startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
                        }
                    }
                }
            });
        }
        dialog.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public void loadUrl(String toString) {
        this.webView.loadUrl(toString);
    }

    public boolean canGoBack() {
        return this.webView.canGoBack();
    }

    public void goBack() {
        this.webView.goBack();
    }


    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;

            showChooserDialog();

            return true;
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
            progressBar.setVisibility(View.GONE);
        }
    }

    public interface mWebViewInterface{
        ProgressBar getProgressBar();
        Activity getActivity();
    }
}
