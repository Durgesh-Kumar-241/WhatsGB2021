package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class MainActivityNew extends AppCompatActivity {
     Fragment fragment1 ;
    Fragment fragment2 ;
    Fragment fragment3 ;
   LinearLayout whatsappweb,statussaver,savedstatus,directChat,settings,facebook,instagram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_main_new);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        whatsappweb=findViewById(R.id.whatsappweb);
        statussaver=findViewById(R.id.statussaver);
        savedstatus=findViewById(R.id.savedstatus);
        directChat=findViewById(R.id.directchat);
        settings=findViewById(R.id.settings);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityNew.this,FacebookActivity.class);
                startActivity(intent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityNew.this,InstagramActivity.class);
                startActivity(intent);
            }
        });
        whatsappweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityNew.this,MainFragment.class);
                startActivity(intent);
            }
        });
       savedstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityNew.this,SavedStatusActivity.class);
                startActivity(intent);
            }
        });
       statussaver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivityNew.this,StatusSaverActivity.class);
               startActivity(intent);
           }
       });
       directChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivityNew.this,DirectChatActivity.class);
               startActivity(intent);
           }
       });
       settings.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivityNew.this,SettingsActivity.class);
               startActivity(intent);
           }
       });
        checkPermissions();








    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denined,Reading external storage is necessary for the app to work properly", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;
    public void checkPermissions()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
        }


    }
    public void applyTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);
        if(dark_theme)
        {
            //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else setTheme(R.style.Theme_AppCompat_Light);
    }
}