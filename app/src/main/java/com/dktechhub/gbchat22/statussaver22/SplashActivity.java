package com.dktechhub.gbchat22.statussaver22;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_splash);
        ((MyApplication)getApplication()).initializeSdk(this::loadInert);
    }

    public void loadInert()
    {
        ((MyApplication)getApplication()).loadInterstitial(this::goToMain);
    }

    public void goToMain()
    {
        new Handler().postDelayed(() -> {

            Intent i = new Intent(getApplicationContext(),MainActivityNew.class);
            startActivity(i);
            finish();
        },500);
    }

}