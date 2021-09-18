package com.dktechhub.mnnit.ee.whatsgb2;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.Map;

public class MyApplication extends Application {
    Activity activity;
    InterstitialAd interstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());

        MobileAds.initialize(this, initializationStatus -> {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }

            // Start loading ads here...
        });

        //AppOpenManager appOpenManager = new AppOpenManager(this);

        //AudienceNetworkAds.initialize(this);



    }

    public void showInterstitial(Activity activity) {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        /*
        if (interstitialAd != null) {
            try {
                interstitialAd.show(activity);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        *
         */
    }
    public void loadAd() {
        /*
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MyApplication.this.interstitialAd = interstitialAd;
                        //showInterstitial();
                        //Log.i(TAG, "onAdLoaded");
                        Toast.makeText(getApplicationContext(), "May be you will see an Ad now", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MyApplication.this.interstitialAd = null;
                                        //Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MyApplication.this.interstitialAd = null;
                                        //Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });

         */
    }



}
