package com.dktechhub.gbchat22.whatsweb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.gbchat22.whatsweb.Utils.DBHelper;
import com.dktechhub.gbchat22.whatsweb.Utils.NotificationTitle;
import com.dktechhub.gbchat22.whatsweb.Utils.NotificationTitleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OFCLO extends AppCompatActivity implements NotificationTitleAdapter.OnItemClickListener {
    Access access;
    DBHelper dbHelper;
    NotificationTitleAdapter adapter;
    RecyclerView recyclerView;
    //MyApplication myApplication;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chat_list);
       // myApplication= (MyApplication) getApplication();
        ((MyApplication)getApplication()).showInterstitialIfReady(this);

        dbHelper = new DBHelper(this);

        getPermissions();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(OFCLO.this,ChatPickerActivity.class)));
        recyclerView = findViewById(R.id.overview_recent_chats);
        adapter = new NotificationTitleAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        empty = findViewById(R.id.empty2);
        refreshUI();
        startObserver();

    }
    public void getPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] { Manifest.permission.READ_CONTACTS},100);
            }
        }else
        {
            prepare();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Contacts permission must be enabled to work this app properly", Toast.LENGTH_SHORT).show();
            finish();
        }else
        {
            prepare();
        }
            

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTaskId();
        //showInters();
    }

    public void prepare()
    {   //Log.d("Setup","Called accessibility: "+isAccessibilityOn(this)+" noti"+ isNotificationEnbld());
        if(!isAccessibilityOn(this))
            setupService();
        if(!isNotificationEnbld())
            startNotificationService();
    }

    public void setupService()
    {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Accessibility service must be enabled to work this app properly");
            builder.setNegativeButton("Go Back", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setPositiveButton("Let's do it", (dialog, which) -> {
                Intent intent = new Intent (Settings.ACTION_ACCESSIBILITY_SETTINGS);
                dialog.dismiss();
                startActivity (intent);

            });

            builder.create().show();
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
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Allow notification access to receive your messages without opening Whatsapp");
        builder.setNegativeButton("Go Back", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.setPositiveButton("Let's do it", (dialog, which) -> {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            dialog.dismiss();
            startActivity(intent);

        });

        builder.create().show();




    }

    private  boolean isNotificationEnbld()
    {
          return Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners") != null && (Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners") != null ? Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners") : "").contains(getPackageName());
    }
    public void setTaskId()
    {
        access=Access.instance;
        if(access!=null&&access.selfTaskId==-1)
            access.setTaskId(getTaskId());
    }

    @Override
    public void onItemClicked(NotificationTitle notificationTitle) {
        Intent i = new Intent(this, OFCDA.class);
        i.putExtra("name",notificationTitle.title);
        i.putExtra("number",notificationTitle.number);
        i.putExtra("id",notificationTitle.id);
        startActivity(i);
        //this.finish();
    }


    public void refreshUI()
    {
        ArrayList<NotificationTitle> rec = dbHelper.loadNotificationData();
        if(rec.size()==0)
        {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setmList(rec);
        }
    }



    newMessageObserver observer;
    public void startObserver()
    {
        observer = new newMessageObserver(this::refreshUI);
        registerReceiver(observer,new IntentFilter("com.dktechhub.gbchat22.whatsweb.newMessageObserver"));
    }
    public void stopObserver()
    {
        if(observer!=null)
            unregisterReceiver(observer);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopObserver();

    }





}