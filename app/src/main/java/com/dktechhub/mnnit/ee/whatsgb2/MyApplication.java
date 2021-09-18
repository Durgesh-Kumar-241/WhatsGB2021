package com.dktechhub.mnnit.ee.whatsgb2;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class MyApplication extends Application {
    Activity activity;
    InterstitialAd interstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
        /*
        MobileAds.initialize(
                this, initializationStatus -> {});
        loadAd();
        //AppOpenManager appOpenManager = new AppOpenManager(this);
        */
        EmojiManager.install(new GoogleEmojiProvider());
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
