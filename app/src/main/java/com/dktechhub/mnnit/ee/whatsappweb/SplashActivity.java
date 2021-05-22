package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    Handler UiHandler=new Handler(Looper.getMainLooper());
                    UiHandler.post(() -> ini());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void ini()
    {
        MobileAds.initialize(this);
        checkForUpdates(this);
        startActivity(new Intent(this,MainActivityNew.class));
        this.finish();
    }



    public void checkForUpdates(Activity context)
    {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Log.d("Update","Checking for updates");
// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
            ) {
                /*&& appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE*/
                try {
                    Log.d("Update","Update available");
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.FLEXIBLE,
                            // The current activity making the update request.
                            context,
                            // Include a request code to later monitor this update request.
                            1234);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.d("Update","Update failed");
                }
            }else {
                Log.d("Update","not available"+appUpdateInfo.toString());
            }
        });
    }


}