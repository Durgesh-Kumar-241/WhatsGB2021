package com.dktechhub.mnnit.ee.whatsweb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationListener extends NotificationListenerService {
    private static final String CHANNEL_ID = "GB Chat";
    SimpleDateFormat simpleDateFormat ;
    DBHelper dbHelper;
    Intent intent;
    NotificationManager notificationManager;

    public NotificationListener() {
        simpleDateFormat = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        intent= new Intent();
        intent.setAction("com.dktechhub.mnnit.ee.whatsweb.newMessageObserver");

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
            Bitmap n=null;
            try {
                n = Build.VERSION.SDK_INT >= 23 ? drawableToBitmap(sbn.getNotification().getLargeIcon().loadDrawable(this.getApplicationContext())) : sbn.getNotification().largeIcon;
                //saveBitmap(n,)
            }catch (Exception e)
            {

            }

            String title = bundle.getString("android.title");
            String mes = bundle.getString("android.text");
            mes=mes.replaceAll("\n","<br>");
            if (title == null || mes == null || title.equals("WhatsApp") || title.equals("You") || title.contains("new message") || mes.contains("new message"))
                return;
            boolean isGorupCoversation = bundle.getBoolean("android.isGroupConversation");
            String time = simpleDateFormat.format(notification.when);
            if (!isGorupCoversation) {

                //if(mob.length()==0)
                String mob = getMobNo(title);
                NotificationTitle notificationTitle = new NotificationTitle(null, title, saveBitmap(n,title+".png"), 0, time, mob, mes);
                int id = dbHelper.insertNotificationData(notificationTitle);
                dbHelper.insertMessage(new WMessage(mes, time, id, null, true, null));

            } else {
                String from = title.substring(title.indexOf(':') + 1);
                title = title.substring(0, title.indexOf(':'));
                if (title.contains(" messages)") && title.contains("("))
                    title = title.substring(0, title.indexOf('(')).trim();
                NotificationTitle notificationTitle = new NotificationTitle(null, title, saveBitmap(n,title+".png"), 0, time, null, mes);
                int id = dbHelper.insertNotificationData(notificationTitle);
                dbHelper.insertMessage(new WMessage(from + "<br>" + mes, time, id, null, true, null));
            }

           // saveBitmap(n,title);
            sendBroadcast(intent);
            showNotification();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    private String saveBitmap(Bitmap bitmap, String str) {
        if(bitmap==null)
            return null;
        File destDir = new File(Environment.getExternalStorageDirectory()+File.separator+"GB What s App"+File.separator+".caches");
        if(destDir.exists()||destDir.mkdirs())
        {
            File dst = new File(destDir,str);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(dst);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
                return str;
            } catch (IOException ignored) {

            }
        }
        return null;
    }





/*
    @TargetApi(19)
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(new Intent(this, NewfileReciever.class));
        } else {
            startService(new Intent(this, NewfileReciever.class));
        }
        try {
            this.i++;
            String packageName = statusBarNotification.getPackageName();
            Bundle bundle = statusBarNotification.getNotification().extras;
            this.m = bundle;
            this.j = bundle.getCharSequence("android.text");
            this.f11526d = this.m.getString("android.title");
            this.n = Build.VERSION.SDK_INT >= 23 ? drawableToBitmap(statusBarNotification.getNotification().getLargeIcon().loadDrawable(this.getApplicationContext())) : statusBarNotification.getNotification().largeIcon;
            long j2 = statusBarNotification.getNotification().when;
            if (this.j != null) {
                String charSequence = this.j.toString();
                this.f11527e = charSequence;
                try {
                    this.i = Integer.parseInt(charSequence.split(" ")[0]);
                } catch (NumberFormatException unused) {
                }
            }
            boolean contains = packageName.contains("com.whatsapp");
            String str = BuildConfig.FLAVOR;
            if (!contains || !this.l.getString("pkgName", str).contains("com.whatsapp")) {
                this.f11526d = this.f11526d.trim();
                this.f11527e = this.f11527e.trim();
                if (!packageName.contains("com.whatsapp")) {
                    if (this.l.getString("pkgName", str).contains(packageName)) {
                        if (!this.f11527e.isEmpty() && this.f11526d != null && !this.f11526d.equals(str) && !this.f11526d.contains("Missed video call") && !this.f11526d.contains("Missed video call") && this.n != null) {
                            b(this.n, this.f11526d);
                        }
                        if (this.f11526d != null && !this.f11526d.equals(str)) {
                            Cursor rawQuery = this.h.getReadableDatabase().rawQuery("SELECT Title FROM Other_Apps_Title WHERE Title= ?", new String[]{this.f11526d});
                            int count = rawQuery.getCount();
                            rawQuery.close();
                            if (!(count <= 0 ? Boolean.FALSE : Boolean.TRUE).booleanValue() && !this.f11526d.contains("WhatsApp") && !this.f11526d.contains("Usb debugging connected") && !this.f11526d.contains("Connected as a media device") && !this.f11526d.contains("using battery") && !this.f11526d.contains("chat heads") && !this.f11526d.contains("Messenger is displaying over other apps")) {
                                a aVar = this.h;
                                String str2 = this.f11526d;
                                String format = this.k.format(new Date());
                                SQLiteDatabase writableDatabase = aVar.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("Title", str2);
                                contentValues.put("Date", format);
                                int i2 = (writableDatabase.insert("Other_Apps_Title", null, contentValues) > -1 ? 1 : (writableDatabase.insert("Other_Apps_Title", null, contentValues) == -1 ? 0 : -1));
                            }
                        }
                    } else {
                        a aVar2 = this.h;
                        String format2 = this.k.format(new Date());
                        String str3 = this.f11526d;
                        SQLiteDatabase writableDatabase2 = aVar2.getWritableDatabase();
                        writableDatabase2.execSQL(" UPDATE Other_Apps_Title SET Date='" + format2 + "' WHERE Title = '" + str3 + "'");
                        writableDatabase2.close();
                    }
                    if (!packageName.contains("com.whatsapp") && this.f11526d != null && !this.f11526d.equals(str) && !this.f11526d.contains("WhatsApp")) {
                        String str4 = this.f11527e;
                        Cursor rawQuery2 = this.h.getReadableDatabase().rawQuery("SELECT Text FROM Other_Apps_Messages WHERE Title= ?", new String[]{this.f11526d});
                        if (rawQuery2.moveToLast()) {
                            str = rawQuery2.getString(rawQuery2.getColumnIndex("Text"));
                        }
                        rawQuery2.close();
                        if (!str4.equals(str) && this.f11527e != null) {
                            this.h.p(this.f11526d, this.f11527e);
                        }
                    }
                }
                Log.e("BC_", "SENT");
                sendBroadcast(new Intent("the.hexcoders.whatsdelete.USER_ACTION_OTHER"));
                return;
            }
            a(statusBarNotification);
            Log.e("BC_", "SENT");
            sendBroadcast(new Intent("the.hexcoders.whatsdelete.USER_ACTION"));
        } catch (Exception unused2) {
        }
    }




*/




































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
                .setContentText("Tap to chat without appearing online")
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

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap createBitmap = (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) ? Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) : Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

}