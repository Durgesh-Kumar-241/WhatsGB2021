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
        simpleDateFormat = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        //dbHelper=new DBHelper(getApplicationContext(),1);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!sbn.getPackageName().equals("com.whatsapp"))
            return;
        Notification notification= sbn.getNotification();
        if(notification==null)
            return;

        Bundle bundle=notification.extras;
        if(bundle==null)
            return;
        if(dbHelper==null)
            dbHelper=DBHelper.getInstance(this);

        String title = bundle.getString("android.title");
        String mes = bundle.getString("android.text");
        if(title.equals("") ||title.equals("WhatsApp")||title.equals("You")||title.contains("new message")||mes.contains("new message"))
            return;
        boolean isGorupCoversation = bundle.getBoolean("android.isGroupConversation");

        String time =simpleDateFormat.format(notification.when);


        if(!isGorupCoversation)
        {

            //if(mob.length()==0)
            String mob=getMobNo(title);
            NotificationTitle notificationTitle =  new NotificationTitle(null,title,null,12,time,mob,mes);
            int id =dbHelper.insertNotificationData(notificationTitle);
            dbHelper.insertMessage(new WMessage(mes,time,id,null,true,null));

        } else {
            String from = title.substring(title.indexOf(':')+1);
            title=title.substring(0,title.indexOf(':'));
            if(title.contains(" messages)")&&title.contains("("))
                title=title.substring(0,title.indexOf('(')).trim();
            NotificationTitle notificationTitle =  new NotificationTitle(null,title,null,12,time,null,mes);
            int id =dbHelper.insertNotificationData(notificationTitle);
            dbHelper.insertMessage(new WMessage(from+" \n"+mes,time,id,null,true,null));
        }
    }




    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    private String getMobNo(String title)
    {   String temp = title .replace(" ","");
        temp=temp.replace("+","");
        if(temp.matches("[0-9]+")&&temp.length()>0)
        {
            return temp;
        }else return dbHelper.getMob(title);
    }
}