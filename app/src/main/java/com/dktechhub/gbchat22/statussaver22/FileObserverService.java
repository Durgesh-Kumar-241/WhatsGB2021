package com.dktechhub.gbchat22.statussaver22;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;

public class FileObserverService extends Service {
    public FileObserverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        startForeGround();
       File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/.Statuses");
        Log.d("FileObserver"," "  +f.canRead()+f.exists()+f.getAbsolutePath());
        FileObserver fileObserver = new FileObserver(f) {
            @Override
            public void onEvent(int i, @Nullable String s) {
                    Log.d("FileObserver","New file created"+s);
                    Toast.makeText(FileObserverService.this, "New file"+ s, Toast.LENGTH_SHORT).show();
            }
        };
        fileObserver.startWatching();
        return START_STICKY;
    }

    public void startForeGround()
    {

    }
}