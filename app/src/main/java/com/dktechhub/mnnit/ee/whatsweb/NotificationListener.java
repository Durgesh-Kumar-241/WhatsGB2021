package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationListener extends NotificationListenerService {
    private static final String CHANNEL_ID = "GB Chat";
    SimpleDateFormat simpleDateFormat ;
    DBHelper dbHelper;
    Intent i;
    NotificationManager notificationManager;
    public NotificationListener() {
        simpleDateFormat = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        i = new Intent();
        i.setAction("com.dktechhub.mnnit.ee.whatsweb.newMessageObserver");

        //dbHelper=new DBHelper(getApplicationContext(),1);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
       // Toast.makeText(getApplicationContext(), "Notification posted", Toast.LENGTH_SHORT).show();

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
            showNotification();

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
        createNotificationChannel();

    }

    private String getMobNo(String title)
    {   String temp = title .replace(" ","");
        temp=temp.replace("+","");
        if(temp.matches("[0-9]+")&&temp.length()>0)
        {
            return temp;
        }else return dbHelper.getMob(title);
    }





    public void showNotification()
    {
        Intent intent2 = new Intent(this, OFCLO.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.gbiconnewsmall)
                .setContentText("Tap to chat without appear online")
                .setContentTitle("GB Whatsapp pro version 2021")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);



        Notification notification = builder.build();
        notificationManager.notify(1,notification);

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}