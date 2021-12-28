package com.dktechhub.gbchat22.whatsweb;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BootReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
        try {
            toggleNotificationService(context);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void toggleNotificationService(Context context)
    {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context,NotificationListener.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context,NotificationListener.class),PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        //context.b
    }
}