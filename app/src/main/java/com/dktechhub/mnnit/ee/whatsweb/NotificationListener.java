package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationListener extends NotificationListenerService {
    SimpleDateFormat simpleDateFormat ;
    DBHelper dbHelper;
    public NotificationListener() {
        simpleDateFormat = new SimpleDateFormat("dd\\MM\\yyyy, hh:mm:ss a", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        //dbHelper=new DBHelper(getApplicationContext(),1);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!sbn.getPackageName().equals("com.whatsapp"))
            return;


        //Log.d("Durgesh",sbn.getPackageName()+"\t"+ sbn.getNotification().toString());


        Notification notification= sbn.getNotification();
        Bundle bundle=notification.extras;

        Log.d("Durgesh",bundle.toString());

        String title = bundle.getString("android.title");

        if(title.equals("") ||title.equals("WhatsApp")||title.equals("You"))
            return;
        boolean isGorupCoversation = bundle.getBoolean("android.isGroupConversation");

        String time =simpleDateFormat.format(notification.when);
        Log.d("Durgesh","DateTime: "+time);
        String mes = bundle.getString("android.text");
        //byte[] blob = notification.getLargeIcon();
        if(!isGorupCoversation)
        {

            Log.d("Durgesh","Private message from "+ title + " Message :"+mes);
        } else {

            title=title.substring(0,title.indexOf(':'));
            Log.d("Durgesh","Group message: "+title+" Messages:"+mes);
        }
        //Log.d("Durgesh","\n\n\n\ntitle "+title+"\t"+"group conversation:"+isGorupCoversation+"\n\n\n");
        if(dbHelper==null)
            dbHelper=new DBHelper(getApplicationContext());
        dbHelper.insertNotificationData(new NotificationTitle(123,title,null,12,time,"100",mes));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}