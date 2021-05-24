package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jetbrains.annotations.NotNull;

public class MyApplication extends Application {
    private static AppOpenManager appOpenManager;
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                initializationStatus -> {});

        appOpenManager=new AppOpenManager(this);
    }
}
