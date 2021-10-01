package com.dktechhub.mnnit.ee.whatsweb;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;


public class SplashActivity extends AppCompatActivity {

    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_splash);
        EmojiManager.install(new GoogleEmojiProvider());
        myApplication=(MyApplication)getApplication();

      /*AudienceNetworkAds
                .buildInitSettings(getApplicationContext())
                .withInitListener(initResult -> {
                    Intent i = new Intent(getApplicationContext(),MainActivityNew.class);
                    startActivity(i);
                    finish();
                })
                .initialize();
        */

        MobileAds.initialize(this, initializationStatus -> {
            myApplication.loadAd();
            Intent i = new Intent(getApplicationContext(),MainActivityNew.class);
            startActivity(i);
            finish();
        });


    }


}