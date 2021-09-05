package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WMessage;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationListener extends NotificationListenerService {
    SimpleDateFormat simpleDateFormat ;
    DBHelper dbHelper;
    public NotificationListener() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        //dbHelper=new DBHelper(getApplicationContext(),1);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!sbn.getPackageName().equals("com.whatsapp"))
            return;


        //Log.d("Durgesh",sbn.getPackageName()+"\t"+ sbn.getNotification().toString());
        if(dbHelper==null)
            dbHelper=DBHelper.getInstance(this);

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
        String mob = "";
        //byte[] blob = notification.getLargeIcon();
        if(!isGorupCoversation)
        {
            mob= mob=dbHelper.getMob(title);
            if(mob.length()==0&&!title.matches(".*[a-zA-Z]+.*"))
            {   mob=title;
                if(mob.startsWith("+"))
                    mob = mob.replace("+"," ");
                mob = mob.replaceAll("\\s","");


                NotificationTitle notificationTitle =  new NotificationTitle(null,title,null,12,time,mob,mes);
                dbHelper.insertNotificationData(notificationTitle);
                dbHelper.insertMessage(new WMessage(mes,time,dbHelper.getNotificationTitleId(notificationTitle),null,true,null));

            }
            //if(mob.length()==0)
            Log.d("Durgesh","Private message from "+ title + " Message :"+mes+ " Numebr "+mob);

        } else {

            title=title.substring(0,title.indexOf(':'));
            if(title.contains(" messages)")&&title.contains("("))
                title=title.substring(0,title.indexOf('(')).trim();
            Log.d("Durgesh","Group message: "+title+" Messages:"+mes);

            NotificationTitle notificationTitle =  new NotificationTitle(null,title,null,12,time,null,mes);
            dbHelper.insertNotificationData(notificationTitle);
            dbHelper.insertMessage(new WMessage(mes,time,dbHelper.getNotificationTitleId(notificationTitle),null,true,null));

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}