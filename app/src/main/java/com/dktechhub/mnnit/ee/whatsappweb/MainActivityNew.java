package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

public class MainActivityNew extends AppCompatActivity {
    //PermissionDetector permissionDetector;
    private final String[] allPermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    com.google.android.material.card.MaterialCardView whatsappweb,statussaver,savedstatus,directChat,settings,facebook,instagram,twitter;
    //private FirebaseAnalytics mFirebaseAnalytics;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        checkPermissions();
        whatsappweb=findViewById(R.id.whatsappweb);
        statussaver=findViewById(R.id.statussaver);
        savedstatus=findViewById(R.id.savedstatus);
        directChat=findViewById(R.id.directchat);
        settings=findViewById(R.id.browser);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        twitter=findViewById(R.id.twitter);
        facebook.setOnClickListener(v -> openBrowser("https://m.facebook.com",false));
        instagram.setOnClickListener(v -> openBrowser("https://www.instagram.com",false));
        whatsappweb.setOnClickListener(v -> {
            String encriptedText = encriptedText();
            StringBuilder sb = new StringBuilder();
            sb.append("https://" + encriptedText + "/🌐/");
            sb.append(Locale.getDefault().getLanguage());
            //this.webView.loadUrl(sb.toString());
            openBrowser(sb.toString(),true);

        });
       savedstatus.setOnClickListener(v -> {
           Intent intent=new Intent(MainActivityNew.this,SavedStatusActivity.class);
           startActivity(intent);
       });
       statussaver.setOnClickListener(v -> {
           Intent intent=new Intent(MainActivityNew.this,StatusSaverActivity.class);
           startActivity(intent);
       });
       directChat.setOnClickListener(v -> {
           Intent intent=new Intent(MainActivityNew.this,DirectChatActivity.class);
           startActivity(intent);
       });
       settings.setOnClickListener(v -> openBrowser("https://web.telegram.org/",false));
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("https://twitter.com",false);
            }
        });

    }

    public void openBrowser(String url,boolean requestdesktopSite)
    {
        Intent intent=new Intent(MainActivityNew.this,MainFragment.class);
        intent.putExtra("url",url);
        intent.putExtra("requestDesktopSite",requestdesktopSite);
        startActivity(intent);
    }


    public void checkPermissions()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return ;

        for (String permission : allPermissions) {
            if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(allPermissions,100);
            }
        }

    }

    private String encriptedText() {
        String str = "";
        for (int i = 0; i < 16; i++) {
            str = str + ((char) ("uc`,uf_rq_nn,amk".charAt(i) + 2));
        }
        return str;
    }

}