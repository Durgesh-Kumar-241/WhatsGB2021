package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;

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
            if(title.contains(" messages)")&&title.contains("("))
                title=title.substring(0,title.indexOf('(')).trim();
            Log.d("Durgesh","Group message: "+title+" Messages:"+mes);
        }
        byte[] photo = null;
        try{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
               Bitmap bmp = (Bitmap) bundle.get(Notification.EXTRA_PICTURE);
               if(bmp==null)
                   bmp= (Bitmap) bundle.get("android.largeIcon");
               if(bmp!=null)
               {
                   ByteArrayOutputStream byteArrayOutputStream =
                           new ByteArrayOutputStream();
                   bmp.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                   photo  = byteArrayOutputStream.toByteArray();
               }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
            
        //Log.d("Durgesh","\n\n\n\ntitle "+title+"\t"+"group conversation:"+isGorupCoversation+"\n\n\n");
        if(dbHelper==null)
            dbHelper=new DBHelper(getApplicationContext());
        dbHelper.insertNotificationData(new NotificationTitle(123,title,photo,12,time,"100",mes));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}