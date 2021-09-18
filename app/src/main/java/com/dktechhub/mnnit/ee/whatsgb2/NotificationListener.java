package com.dktechhub.mnnit.ee.whatsgb2;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.dktechhub.mnnit.ee.whatsgb2.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsgb2.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsgb2.Utils.WMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationListener extends NotificationListenerService {
    SimpleDateFormat simpleDateFormat ;
    DBHelper dbHelper;
    Intent i;
    public NotificationListener() {
        simpleDateFormat = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        i = new Intent();
        i.setAction("com.dktechhub.mnnit.ee.whatsweb.newMessageObserver");

        //dbHelper=new DBHelper(getApplicationContext(),1);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {


            super.onNotificationPosted(sbn);
            if (sbn.getPackageName() == null || !sbn.getPackageName().equals("com.whatsapp"))
                return;
            Notification notification = sbn.getNotification();
            if (notification == null)
                return;

            Bundle bundle = notification.extras;
            if (bundle == null)
                return;
            if (dbHelper == null)
                dbHelper = DBHelper.getInstance(this);

            String title = bundle.getString("android.title");
            String mes = bundle.getString("android.text");
            if (title == null || mes == null || title.equals("WhatsApp") || title.equals("You") || title.contains("new message") || mes.contains("new message"))
                return;
            boolean isGorupCoversation = bundle.getBoolean("android.isGroupConversation");

            String time = simpleDateFormat.format(notification.when);


            if (!isGorupCoversation) {

                //if(mob.length()==0)
                String mob = getMobNo(title);
                NotificationTitle notificationTitle = new NotificationTitle(null, title, null, 0, time, mob, mes);
                int id = dbHelper.insertNotificationData(notificationTitle);
                dbHelper.insertMessage(new WMessage(mes, time, id, null, true, null));

            } else {
                String from = title.substring(title.indexOf(':') + 1);
                title = title.substring(0, title.indexOf(':'));
                if (title.contains(" messages)") && title.contains("("))
                    title = title.substring(0, title.indexOf('(')).trim();
                NotificationTitle notificationTitle = new NotificationTitle(null, title, null, 0, time, null, mes);
                int id = dbHelper.insertNotificationData(notificationTitle);
                dbHelper.insertMessage(new WMessage(from + " \n" + mes, time, id, null, true, null));
            }
            sendBroadcast(i);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        tryRecconectService();
    }

    private String getMobNo(String title)
    {   String temp = title .replace(" ","");
        temp=temp.replace("+","");
        if(temp.matches("[0-9]+")&&temp.length()>0)
        {
            return temp;
        }else return dbHelper.getMob(title);
    }


    public void tryRecconectService()
    {
     toggleNotificationService();
     if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
     {
         ComponentName cp = new ComponentName(getApplicationContext(),NotificationListener.class);
         requestRebind(cp);
     }
    }

    private void toggleNotificationService()
    {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this,NotificationListener.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this,NotificationListener.class),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

    }
}