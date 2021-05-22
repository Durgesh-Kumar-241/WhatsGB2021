package com.dktechhub.mnnit.ee.whatsappweb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.net.URLEncoder;

public class DirectChatActivity extends AppCompatActivity {
    TextView send,clear;
    EditText number,message;
    CountryCodePicker ccp;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_direct_chat);
        //MobileAds.initialize(this);
        ccp=findViewById(R.id.spinner);
        number=findViewById(R.id.number);
        message=findViewById(R.id.message);
        send=findViewById(R.id.send);
        clear=findViewById(R.id.clear);
        ccp.registerPhoneNumberTextView(number);

        ccp.enableHint(false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ccp.setDefaultCountryUsingNameCodeAndApply(sharedPreferences.getString("recent_country",ccp.getSelectedCountryName()));
        //Log.d("Number",sharedPreferences.getString("recent_country",ccp.getSelectedCountryName())+"loaded");
        number.setText(sharedPreferences.getString("recent_number",""));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number.setText("");
                message.setText("");
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void send()
    {
        //int contCode=91;
        //Toast.makeText(this, ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
        if(!ccp.isValid()) {
            Toast.makeText(this, "Phone number is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        String smessage = message.getText().toString();
        String url = "https://api.whatsapp.com/send/?phone="+ccp.getFullNumber()+"&text="+ URLEncoder.encode(smessage);
        try{
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setPackage("com.whatsapp");
            startActivity(i);
        }catch (Exception e)
        {
            Toast.makeText(this, "Whatsapp is not installed on your device", Toast.LENGTH_SHORT).show();
        }

        sharedPreferences.edit().putString("recent_country",ccp.getSelectedCountryNameCode()).apply();
        sharedPreferences.edit().putString("recent_number",number.getText().toString()).apply();
    }


}