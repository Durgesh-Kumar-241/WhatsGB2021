package com.dktechhub.gbchat22.whatsweb;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class MyApplication extends Application {
    /*
    InterstitialAd interstitialAd;
    int nused=0;
    AdView adView;
    boolean alrerady = false;
    boolean loadedBanner=false;
    SharedPreferences sharedPreferences;
    boolean init=false;
    */

    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());
        /*
        init();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nused = sharedPreferences.getInt("nused",1);
        sharedPreferences.edit().putInt("nused",nused+1).apply();

         */
    }
    /*
    public void showInterstitial(Activity activity) {
        if (interstitialAd != null) {
            try {
                interstitialAd.show(activity);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }else loadAd();

    }
    public void loadAd() {
        Log.d("2004d","Requested inter");
        if(nused<2||this.interstitialAd!=null||alrerady)
            return;
        Log.d("2004d","Request confirmed for interstitial");
        alrerady=true;


    }


    LinearLayout current;
    public void loadBanner(LinearLayout linearLayout)
    {
            if(nused<3)
                return;

            current=linearLayout;
            if(adView==null)
            {
                adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
                AdListener adListener = new AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        loadedBanner=true;
                        if(current!=null)
                        {
                            current.removeAllViews();
                            current.addView(adView);
                        }
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                };
                adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

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




    }

    public void init()
    {
        AudienceNetworkAds.buildInitSettings(this).withInitListener(initResult -> {
            if(onInitListener!=null)
                onInitListener.onInitialized();

            init=true;
        }).initialize();
    }


    public void setOnInitListner(OnInitListener onInitListener)
    {
        this.onInitListener=onInitListener;
    }

    public OnInitListener onInitListener;
    public interface OnInitListener{
        public void onInitialized();
    }

     */
}
