package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationListener extends NotificationListenerService {
    public NotificationListener() {
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d("Durgesh",sbn.getPackageName()+"\t"+ sbn.getNotification().toString());

        if(!(sbn.getPackageName().equals("com.whatsapp")))
            return;
        Notification notification= sbn.getNotification();
        Bundle bundle=notification.extras;
        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);

        Log.d("Durgesh",from+"\t"+message);


    }
}