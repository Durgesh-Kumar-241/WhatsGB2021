package com.dktechhub.mnnit.ee.whatsweb;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class OfflineChatList extends AppCompatActivity implements NotificationTitleAdapter.OnItemClickListener {
    Access access;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chat_list);
        dbHelper = new DBHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getPermissions();
        startNotificationService();
        setupService();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OfflineChatList.this,ChatPickerActivity.class));

            }
        });
        RecyclerView recyclerView = findViewById(R.id.overview_recent_chats);
        NotificationTitleAdapter adapter = new NotificationTitleAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //NotificationTitle notificationTitle = new NotificationTitle(123,"Durgesh",null,1234,"12/02/2003","100","Hello Dear");

        //dbHelper.insertNotificationData(notificationTitle);
        adapter.setmList(dbHelper.loadNotificationData());


    }
    public void getPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] { Manifest.permission.READ_CONTACTS},100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Contacts permission must be enabled to work this app properly", Toast.LENGTH_SHORT).show();
            finish();
        }
            

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTaskId();
        setupService();
    }

    public void setupService()
    {
        if (!isAccessibilityOn (this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Accessibility service must be enable to work this app properly");
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent (Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity (intent);
                    dialog.dismiss();
                }
            });

            builder.create().show();

        }else setTaskId();
    }
    private boolean isAccessibilityOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + Access.class.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void startNotificationService()
    {   if(!isNotificationEnbld()) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Allow notification access to receive your messages without opening Whatsapp");
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.create().show();



    }
    }

    private  boolean isNotificationEnbld()
    {
        ContentResolver cr = getContentResolver();
        String enableds = Settings.Secure.getString(cr,"enabled_notification_listeners");
        String pak = getPackageName();
        return  enableds!=null && enableds.contains(pak);
    }
    public void setTaskId()
    {
        access=Access.instance;
        if(access!=null)
            access.setTaskId(getTaskId());
    }

    @Override
    public void onItemClicked(NotificationTitle notificationTitle) {
        Intent i = new Intent(this,OfflineChatDetailedActivity.class);
        i.putExtra("name",notificationTitle.title);
        i.putExtra("number",notificationTitle.number);
        i.putExtra("id",notificationTitle.id);
        startActivity(i);
        this.finish();
    }
}