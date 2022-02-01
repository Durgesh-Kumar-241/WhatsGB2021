package com.dktechhub.gbchat22.whatsweb;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.dktechhub.gbchat22.whatsweb.Utils.AdmobConf;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class MyApplication extends MultiDexApplication {
    FirebaseFirestore db;
    AdmobConf admobConf=new AdmobConf();
    public int adSerial =2;
    DocumentReference admob_conf;
    InterstitialAd interstitialAd;
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());
        setTheme();


    }

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
        admob_conf.set(admobConf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("firebase admob","request updated");
            }
        });
    }

    public void setTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme_mode", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(theme));

    }


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
}
