package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

public class PermissionDetector {
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;
    private static final int CAMERA_CODE = 532;
    private static final int RECORD_AUDIO = 55;
    private static final int ALL = 173;
    final Activity activity;
    private String[] allPermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    public PermissionDetector(Activity activity)
    {
        this.activity=activity;
    }

    public boolean hasPermissions(Context context, String[] permissions) {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return true;
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (this.activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions()
    {   if(!hasPermissions(this.activity,allPermissions)) {
        activity.requestPermissions(allPermissions,ALL);
    }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case ALL:
                if (grantResults.length > 0) {

                    if(hasPermissions(this.activity,allPermissions))
                    {
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(activity.findViewById(android.R.id.content),
                                "Please Grant Permissions to work this App properly",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        checkPermissions();
                                    }
                                }).show();
                    }
                }
                break;
        }
    }
}
