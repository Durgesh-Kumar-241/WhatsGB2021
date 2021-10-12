package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import org.jetbrains.annotations.NotNull;

public class MyApplication extends Application {

    InterstitialAd interstitialAd;
    int nused=0;
    AdView adView;
    boolean alrerady = false;
    boolean loadedBanner=false;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nused = sharedPreferences.getInt("nused",1);
        sharedPreferences.edit().putInt("nused",nused+1).apply();


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

        if(nused<2||this.interstitialAd!=null||alrerady)
            return;
       // Log.d("Request","Request confirmed");
        alrerady=true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        MyApplication.this.interstitialAd = interstitialAd;
                        //Log.d("Request","loaded");
                        alrerady=false;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        MyApplication.this.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                        MyApplication.this.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        interstitialAd = null;
                        alrerady=false;
                    }
                });


    }
    LinearLayout current;
    public void loadBanner(LinearLayout linearLayout)
    {
            if(nused<3)
                return;

            current=linearLayout;
            if(adView==null)
            {
                adView = new AdView(this);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(getString(R.string.ban_ofcld));
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        loadedBanner=true;
                        //Toast.makeText(getApplicationContext(), "Banner loaded", Toast.LENGTH_SHORT).show();
                        //Log.d("Banner","Banner loaded");
                        if(current!=null)
                        {
                            current.removeAllViews();
                            current.addView(adView);
                        }
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        loadedBanner=false;
                    }
                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        //Toast.makeText(getApplicationContext(), "Load impressed", Toast.LENGTH_SHORT).show();
                        //Log.d("Banner","Banner displayed");
                    }
                });
            }

            if(loadedBanner){
                if(adView.getParent()!=null)
                {
                    LinearLayout t = (LinearLayout) adView.getParent();
                    t.removeView(adView);
                }
                current.removeAllViews();
                current.addView(adView);
            }

            //Log.d("Banner","Banner requested");


    }





}
