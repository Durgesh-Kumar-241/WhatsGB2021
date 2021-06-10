package com.dktechhub.mnnit.ee.whatswebcloner;

import android.app.Application;

public class MyApplication extends Application {
    DatabaseManager databaseManager;
    @Override
    public void onCreate() {
        super.onCreate();
        //MobileAds.initialize(
          //      this,
           //     initializationStatus -> {});

        //AppOpenManager appOpenManager = new AppOpenManager(this);

    }
    /*
    public DatabaseManager getDatabaseManager()
    {
        if(databaseManager==null)
            databaseManager=new DatabaseManager(this);
        return databaseManager;
    }

     */
}
