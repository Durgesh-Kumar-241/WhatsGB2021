package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public class MyApplication extends Application {

    InterstitialAd interstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void showInterstitial(Activity activity) {

        if (interstitialAd != null) {
            try {
                interstitialAd.show(activity);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    public void loadAd() {
        if(this.interstitialAd!=null)
            return;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        MyApplication.this.interstitialAd = interstitialAd;

                        Toast.makeText(getApplicationContext(), "May be you will see an Ad now", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {

                                        MyApplication.this.interstitialAd = null;
                                        loadAd();
                                        //Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {

                                        MyApplication.this.interstitialAd = null;

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {

                                        loadAd();

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        interstitialAd = null;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadAd();
                            }
                        },10000);
                        //loadAd();
                    }
                });


    }



}
