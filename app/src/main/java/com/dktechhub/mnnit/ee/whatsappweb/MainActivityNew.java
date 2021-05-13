package com.dktechhub.mnnit.ee.whatsappweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.PreferenceManager;

public class MainActivityNew extends AppCompatActivity {
    PermissionDetector permissionDetector;
    com.google.android.material.card.MaterialCardView whatsappweb,statussaver,savedstatus,directChat,settings,facebook,instagram;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_main_new);


        permissionDetector=new PermissionDetector(this);
        permissionDetector.checkPermissions();
        whatsappweb=findViewById(R.id.whatsappweb);
        statussaver=findViewById(R.id.statussaver);
        savedstatus=findViewById(R.id.savedstatus);
        directChat=findViewById(R.id.directchat);
        settings=findViewById(R.id.browser);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        facebook.setOnClickListener(v -> openBrowser("https://m.facebook.com"));
        instagram.setOnClickListener(v -> openBrowser("https://www.instagram.com"));
        whatsappweb.setOnClickListener(v -> {
            //openBrowser("https://web.whatsapp.com");
            Intent intent=new Intent(MainActivityNew.this,MainFragment.class);
            startActivity(intent);
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
       settings.setOnClickListener(v -> openBrowser("https://www.google.com"));



    }

    public void openBrowser(String url)
    {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();

        // below line is setting toolbar color
        // for our custom chrome tab.
        //customIntent.setToolbarColor(ContextCompat.getColor(MainActivityNew.this, R.color.purple_200));
        customIntent.setUrlBarHidingEnabled(true);

        // we are calling below method after
        // setting our toolbar color.
        CustomTabsIntent customTabsIntent=customIntent.build();

        String packageName = "com.android.chrome";
        try{
            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName);

            // in that custom tab intent we are passing
            // our url which we have to browse.


            customTabsIntent.launchUrl(MainActivityNew.this,Uri.parse(url));
        } catch (Exception e){
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionDetector.onRequestPermissionsResult( requestCode, permissions,  grantResults);
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
}