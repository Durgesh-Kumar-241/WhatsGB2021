package com.dktechhub.gbchat22.whatsweb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.dktechhub.gbchat22.whatsweb.Utils.AdmobConf;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SplashActivity extends AppCompatActivity {


    private boolean storage=false,ini=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ((MyApplication)getApplication()).adSerial++;
        ((MyApplication)getApplication()).loadInterstitial();
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_splash);

        Button con = findViewById(R.id.cont);

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {

            update();
        }else {
            con.setVisibility(View.VISIBLE);

        }


        con.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                update();
            } else {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        });







    }


    public void goToMain()
    {
        new Handler().postDelayed(() -> {

            Intent i = new Intent(getApplicationContext(),MainActivityNew.class);
            startActivity(i);
            finish();
        },500);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        { storage=true; update();}
        else Toast.makeText(getApplicationContext(), "Storage permission is necessary to work this app properly", Toast.LENGTH_SHORT).show();
    }


    public void update()
    {
       // if(ini&&storage)
            goToMain();
    }
}