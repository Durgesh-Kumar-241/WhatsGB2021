package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.MissingResourceException;

public class MainFragment extends AppCompatActivity {


    //ProgressBar pbar;
    WebView webView;
    boolean requestDesktopSite;
    String url;
    ProgressBar progressBar;
    ActionBar actionBar;
    //protected static final String LANGUAGE_DEFAULT_ISO3 = "eng";
    public static final String PACKAGE_NAME_DOWNLOAD_MANAGER = "com.android.providers.downloads";
    protected static final int REQUEST_CODE_FILE_PICKER = 51426;
    protected static final String DATABASES_SUB_FOLDER = "/databases";
    protected static final String LANGUAGE_DEFAULT_ISO3 = "eng";
    protected static final String CHARSET_DEFAULT = "UTF-8";
    /** Alternative browsers that have their own rendering engine and *may* be installed on this device */
    protected static final String[] ALTERNATIVE_BROWSERS = new String[] { "org.mozilla.firefox", "com.android.chrome", "com.opera.browser", "org.mozilla.firefox_beta", "com.chrome.beta", "com.opera.browser.beta" };
    protected WeakReference<Activity> mActivity;
    protected WeakReference<Fragment> mFragment;
    //protected Listener mListener;
   // protected final List<String> mPermittedHostnames = new LinkedList<String>();
    /** File upload callback for platform versions prior to Android 5.0 */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /** File upload callback for Android 5.0+ */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
    protected long mLastError;
    protected String mLanguageIso3;
    protected int mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER;
    protected WebViewClient mCustomWebViewClient;
    protected WebChromeClient mCustomWebChromeClient;
    protected boolean mGeolocationEnabled;
    protected String mUploadableFileTypes = "*/*";
    //protected final Map<String, String> mHttpHeaders = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.hide();
        }

        ConnectionDetector.checkInternet(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},WRITE_EXTERNAL_STORAGE_CODE);
        }


       requestDesktopSite= getIntent().getBooleanExtra("requestDesktopSite",false);
        url=getIntent().getStringExtra("url");

        this.webView = findViewById(R.id.webView);

        progressBar=findViewById(R.id.progressBar);
        InitSettings();
        webView.loadUrl(url);



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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denined,Reading external storage is necessary for the app to work properly", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.fullscreen)
        {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }else if(item.getItemId()==R.id.refresh)
        {
            this.webView.reload();
        }
        return true;
    }


    public class webClient extends WebViewClient {
        private final boolean requestDesktopSite;
        public webClient(boolean requestDesktopSite)
        {
            this.requestDesktopSite=requestDesktopSite;
        }
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            webView.loadUrl(url);
            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLoadResource(WebView view, String url) {
            if(this.requestDesktopSite)
                view.evaluateJavascript("document.querySelector('meta[name=\"viewport\"]').setAttribute('content', 'width=1024px, initial-scale=' + (document.documentElement.clientWidth / 1024));", null);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

    }

    public class WebChromeClient extends android.webkit.WebChromeClient
    {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if(MainFragment.this.actionBar!=null)
            {
                actionBar.setTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }


        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // file upload callback (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // file upload callback (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // file upload callback (Android 5.0 (API level 21) -- current) (public method)
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;

                openFileInput(null, filePathCallback, allowMultiple);

                return true;
            }
            else {
                return false;
            }
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void InitSettings() {
        //this.requestDesktopSite = requestDesktopSite;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new webClient(requestDesktopSite));
        this.webView.getSettings().setSaveFormData(true);
        this.webView.getSettings().setLoadsImagesAutomatically(true);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        if (requestDesktopSite) {

            this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            this.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Win64; x64; rv:46.0) Gecko/20100101 Firefox/60.0");
            this.webView.setInitialScale(25);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
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
        webView.setDownloadListener(new DownloadListener() {
                                        @Override
                                        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                                            request.setDescription("Downloading file...");
                                            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url));
                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                            dm.enqueue(request);
                                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                                            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                        }
                                        final BroadcastReceiver onComplete = new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {
                                                Toast.makeText(getApplicationContext(), "Downloading Complete", Toast.LENGTH_SHORT).show();
                                            }
                                        };
                                    } );
    }















    //Upload


    protected static String getLanguageIso3() {
        try {
            return Locale.getDefault().getISO3Language().toLowerCase(Locale.US);
        }
        catch (MissingResourceException e) {
            return LANGUAGE_DEFAULT_ISO3;
        }
    }

    /**
     * Provides localizations for the 25 most widely spoken languages that have a ISO 639-2/T code
     *
     * @return the label for the file upload prompts as a string
     */
    protected String getFileUploadPromptLabel() {
        try {
            if (mLanguageIso3.equals("zho")) return decodeBase64("6YCJ5oup5LiA5Liq5paH5Lu2");
            else if (mLanguageIso3.equals("spa")) return decodeBase64("RWxpamEgdW4gYXJjaGl2bw==");
            else if (mLanguageIso3.equals("hin")) return decodeBase64("4KSP4KSVIOCkq+CkvOCkvuCkh+CksiDgpJrgpYHgpKjgpYfgpII=");
            else if (mLanguageIso3.equals("ben")) return decodeBase64("4KaP4KaV4Kaf4Ka/IOCmq+CmvuCmh+CmsiDgpqjgpr/gprDgp43gpqzgpr7gpprgpqg=");
            else if (mLanguageIso3.equals("ara")) return decodeBase64("2KfYrtiq2YrYp9ixINmF2YTZgSDZiNin2K3Yrw==");
            else if (mLanguageIso3.equals("por")) return decodeBase64("RXNjb2xoYSB1bSBhcnF1aXZv");
            else if (mLanguageIso3.equals("rus")) return decodeBase64("0JLRi9Cx0LXRgNC40YLQtSDQvtC00LjQvSDRhNCw0LnQuw==");
            else if (mLanguageIso3.equals("jpn")) return decodeBase64("MeODleOCoeOCpOODq+OCkumBuOaKnuOBl+OBpuOBj+OBoOOBleOBhA==");
            else if (mLanguageIso3.equals("pan")) return decodeBase64("4KiH4Kmx4KiVIOCoq+CovuCoh+CosiDgqJrgqYHgqKPgqYs=");
            else if (mLanguageIso3.equals("deu")) return decodeBase64("V8OkaGxlIGVpbmUgRGF0ZWk=");
            else if (mLanguageIso3.equals("jav")) return decodeBase64("UGlsaWggc2lqaSBiZXJrYXM=");
            else if (mLanguageIso3.equals("msa")) return decodeBase64("UGlsaWggc2F0dSBmYWls");
            else if (mLanguageIso3.equals("tel")) return decodeBase64("4LCS4LCVIOCwq+CxhuCxluCwsuCxjeCwqOCxgSDgsI7gsILgsJrgsYHgsJXgsYvgsILgsKHgsL8=");
            else if (mLanguageIso3.equals("vie")) return decodeBase64("Q2jhu41uIG3hu5l0IHThuq1wIHRpbg==");
            else if (mLanguageIso3.equals("kor")) return decodeBase64("7ZWY64KY7J2YIO2MjOydvOydhCDshKDtg50=");
            else if (mLanguageIso3.equals("fra")) return decodeBase64("Q2hvaXNpc3NleiB1biBmaWNoaWVy");
            else if (mLanguageIso3.equals("mar")) return decodeBase64("4KSr4KS+4KSH4KSyIOCkqOCkv+CkteCkoeCkvg==");
            else if (mLanguageIso3.equals("tam")) return decodeBase64("4K6S4K6w4K+BIOCuleCvh+CuvuCuquCvjeCuquCviCDgrqTgr4fgrrDgr43grrXgr4E=");
            else if (mLanguageIso3.equals("urd")) return decodeBase64("2KfbjNqpINmB2KfYptmEINmF24zauiDYs9uSINin2YbYqtiu2KfYqCDaqdix24zaug==");
            else if (mLanguageIso3.equals("fas")) return decodeBase64("2LHYpyDYp9mG2KrYrtin2Kgg2qnZhtuM2K8g24zaqSDZgdin24zZhA==");
            else if (mLanguageIso3.equals("tur")) return decodeBase64("QmlyIGRvc3lhIHNlw6dpbg==");
            else if (mLanguageIso3.equals("ita")) return decodeBase64("U2NlZ2xpIHVuIGZpbGU=");
            else if (mLanguageIso3.equals("tha")) return decodeBase64("4LmA4Lil4Li34Lit4LiB4LmE4Lif4Lil4LmM4Lir4LiZ4Li24LmI4LiH");
            else if (mLanguageIso3.equals("guj")) return decodeBase64("4KqP4KqVIOCqq+CqvuCqh+CqsuCqqOCrhyDgqqrgqrjgqoLgqqY=");
        }
        catch (Exception ignored) { }

        // return English translation by default
        return "Choose a file";
    }

    protected static String decodeBase64(final String base64) throws IllegalArgumentException, UnsupportedEncodingException {
        final byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return new String(bytes, CHARSET_DEFAULT);
    }

    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst;

        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        if (allowMultiple) {
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        i.setType(mUploadableFileTypes);
        //startActivityForResult(Intent.createChooser(i, getFileUploadPromptLabel()), mRequestCodeFilePicker);
        Intent shareIntent = Intent.createChooser(i, getFileUploadPromptLabel());
        startActivityForResult(shareIntent,mRequestCodeFilePicker);
    }

    /**
     * Returns whether file uploads can be used on the current device (generally all platform versions except for 4.4)
     *
     * @return whether file uploads can be used
     */
    public static boolean isFileUploadAvailable() {
        return isFileUploadAvailable(false);
    }

    /**
     * Returns whether file uploads can be used on the current device (generally all platform versions except for 4.4)
     *
     * On Android 4.4.3/4.4.4, file uploads may be possible but will come with a wrong MIME type
     *
     * @param needsCorrectMimeType whether a correct MIME type is required for file uploads or `application/octet-stream` is acceptable
     * @return whether file uploads can be used
     */
    public static boolean isFileUploadAvailable(final boolean needsCorrectMimeType) {
        return true;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == mRequestCodeFilePicker) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
                        mFileUploadCallbackFirst = null;
                    } else if (mFileUploadCallbackSecond != null) {
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[]{Uri.parse(intent.getDataString())};
                            } else {
                                if (intent.getClipData() != null) {
                                    final int numSelectedFiles = intent.getClipData().getItemCount();

                                    dataUris = new Uri[numSelectedFiles];

                                    for (int i = 0; i < numSelectedFiles; i++) {
                                        dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }

                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            } else {
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }
    }









    @SuppressLint("NewApi")
    public static boolean handleDownload(final Context context, final String fromUrl, final String toFilename) {

        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fromUrl));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, toFilename);

        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            try {
                dm.enqueue(request);
            }
            catch (SecurityException e) {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                dm.enqueue(request);
            }

            return true;
        }
        // if the download manager app has been disabled on the device
        catch (IllegalArgumentException e) {
            // show the settings screen where the user can enable the download manager app again
            openAppSettings(context);

            return false;
        }
    }

    @SuppressLint("NewApi")
    private static void openAppSettings(final Context context) {

        try {
            final Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + MainFragment.PACKAGE_NAME_DOWNLOAD_MANAGER));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

        }
        catch (Exception ignored) {
        }
    }
}