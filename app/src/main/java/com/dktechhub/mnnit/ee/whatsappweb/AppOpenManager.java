package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.Activity;
import android.app.Application;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AppOpenManager implements  Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private final long loadTimeAppOpen;
    private long loadTimeInterstitial;
    private AppOpenAd appOpenAd = null;
    private InterstitialAd interstitialAd=null;

    private final MyApplication myApplication;

    private Activity currentActivity;
    /** Constructor */
    public AppOpenManager(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        loadTimeAppOpen = (new Date()).getTime();
        loadTimeInterstitial=(new Date()).getTime();
        fetchAppOpeenAd();
        fetchInterstitialAd();


    }

    /** Request an ad */
        public void fetchAppOpeenAd() {
            // Have unused ad, no need to fetch another.
            if (isAppOpenAdAvailable()) {
                return;
            }

            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            // Handle the error.
            AppOpenAd.AppOpenAdLoadCallback appOpenAdLoadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                @Override
                public void onAdLoaded(@NotNull AppOpenAd ad) {
                    AppOpenManager.this.appOpenAd = ad;
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                @Override
                public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                    // Handle the error.
                }

            };
            AdRequest request = getAdRequest();
            AppOpenAd.load(
                    myApplication, AD_UNIT_ID, request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, appOpenAdLoadCallback);
        }


    public void fetchInterstitialAd() {
        // Have unused ad, no need to fetch another.
        if (isInterstitialAdAvailable()) {
            return;
        }
        InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                AppOpenManager.this.interstitialAd = interstitialAd;
                loadTimeInterstitial=(new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        };

        AdRequest request = getAdRequest();
        InterstitialAd.load(myApplication,"ca-app-pub-3940256099942544/1033173712",request, interstitialAdLoadCallback);
    }


    public void showInterstitialIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isInterstitialAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.interstitialAd = null;
                            isShowingAd = false;
                            fetchInterstitialAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            interstitialAd.show(currentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchInterstitialAd();
        }
    }







    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /** Utility method that checks if ad exists and can be shown. */


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if(activity.getClass().getName().equals(MainActivityNew.class.getName()))
            showAppOpenAdIfAvailable();
        else
            showInterstitialIfAvailable();
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        //currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    currentActivity=null;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    currentActivity=null;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }






    private static boolean isShowingAd = false;

    /** Shows the ad if one isn't already showing. */

    public void showAppOpenAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAppOpenAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAppOpeenAd();
                            checkForUpdates(currentActivity);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAppOpeenAd();
        }
    }
    /*
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void loadAppOpenAdd()
    {   if(currentActivity.getClass().getName().equals(SplashActivity.class.getName()))
        showAppOpenAdIfAvailable();
    }

     */

    /** Utility method to check if ad was loaded more than n hours ago. */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours,long loadTime) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAppOpenAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4,loadTimeAppOpen);
    }

    public boolean isInterstitialAdAvailable() {
        return interstitialAd != null && wasLoadTimeLessThanNHoursAgo(4,loadTimeInterstitial);
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
