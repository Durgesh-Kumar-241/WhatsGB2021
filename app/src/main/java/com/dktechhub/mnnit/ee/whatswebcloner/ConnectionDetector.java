package com.dktechhub.mnnit.ee.whatswebcloner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    public static boolean isInternetAvailable(Activity context) {
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        else {
            if (info.isConnected()) {
                return true;
            } else {
                return true;
            }
        }
    }
    public static void checkInternet(Activity activity)
    {
        if(!ConnectionDetector.isInternetAvailable(activity))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setMessage(activity.getString(R.string.connecttointernerfirst));
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.onBackPressed();activity.onBackPressed();
                }
            });
            alertDialog.show();

        }
    }
}
