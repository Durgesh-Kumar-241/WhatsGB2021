package com.dktechhub.gbchat22.whatsweb;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.dktechhub.gbchat22.whatsweb.Utils.AdmobConf;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.List;

public class MyApplication extends MultiDexApplication {
    FirebaseFirestore db;
    AdmobConf admobConf=new AdmobConf();
    String id;
    CollectionReference admob_conf;
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new GoogleEmojiProvider());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme_mode", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        //AppCompatDelegate.setDefaultNightMode(theme);
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(theme));


        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference admob_conf = db.collection("main_admob");
        admob_conf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot: list)
                {
                    AdmobConf  admobConf = documentSnapshot.toObject(AdmobConf.class);
                    String id = documentSnapshot.getId();
                    Log.d("firebase admob",admobConf.getMaxImpressionsDaily()+" "+admobConf.getImpressions()+" "+admobConf.getRequests()+" "+admobConf.getMaxRequestsDaily());

                    admobConf.setImpressions(admobConf.getImpressions()+1);
                    admob_conf.document(id).set(admobConf).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("firebase admob","updated sucessfully");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("firebase admob",e.toString());
            }
        });
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
