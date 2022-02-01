package com.dktechhub.gbchat22.statussaver22;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import androidx.preference.PreferenceManager;

public class MyApplication extends Application {
    /*
    FirebaseFirestore db;
    AdmobConf admobConf=new AdmobConf();
    public int adSerial =3;
    DocumentReference admob_conf;
    InterstitialAd interstitialAd;

     */
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme();


    }
    /*
    public void loadAdmobConf()
    {
        admob_conf.get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists())
                {
                    admobConf = documentSnapshot.toObject(AdmobConf.class);
                    //loadInterstitial();
                    Log.d("firebase admob",admobConf.getMaxImpressionsDaily()+" "+admobConf.getRequests()+" "+admobConf.getMaxRequestsDaily());
                }
            }
        });
    }

    public void updateAdmobRequest()
    {
        loadAdmobConf();
        admobConf.setRequests(admobConf.getRequests()+1);
        admob_conf.set(admobConf).addOnSuccessListener(unused -> Log.d("firebase admob","request updated"));
    }


     */
    public void setTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme_mode", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(theme));

    }

/*
    public void loadInterstitial(SDKInterface sdkInterface)
    {   adSerial++;
        Log.d("firebase admob","adserial "+adSerial);
        if(interstitialAd==null&&admobConf.requestAllowed()&&adSerial%4==0)
        {
            AdRequest adRequest =new  AdRequest.Builder().build();
            InterstitialAd.load(this,getString(R.string.inter), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                           MyApplication.this.interstitialAd = interstitialAd;
                           Log.d("firebase admob","ad loaded");
                           updateAdmobRequest();
                           sdkInterface.onInitCompleted();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("admob",loadAdError.getMessage()+loadAdError.getResponseInfo()+loadAdError.toString());
                            interstitialAd = null;
                            updateAdmobRequest();
                            sdkInterface.onInitCompleted();
                        }
                    });
        }else {
            sdkInterface.onInitCompleted();
        }
    }

    public void showInterstitialIfReady(Activity activity)
    {
        if(this.interstitialAd!=null)
        {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    interstitialAd = null;
                }
            });
            interstitialAd.show(activity);

        }
    }

    public void initializeSdk(SDKInterface sdkInterface)
    {
        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this, initializationStatus -> sdkInterface.onInitCompleted());
        db = FirebaseFirestore.getInstance();
        admob_conf = db.collection("main_admob").document("admob_conf");
        loadAdmobConf();
        //loadInterstitial();
    }

    public interface SDKInterface{
        void onInitCompleted();
    }

 */
}
