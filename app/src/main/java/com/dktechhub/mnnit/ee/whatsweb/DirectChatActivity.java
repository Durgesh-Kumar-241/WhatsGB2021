package com.dktechhub.mnnit.ee.whatsweb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.net.URLEncoder;

public class DirectChatActivity extends AppCompatActivity {
    TextView send,clear;
    EditText number,message;
    CountryCodePicker ccp;
    SharedPreferences sharedPreferences;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_direct_chat);
        //MobileAds.initialize(this);
        //myApplication=(MyApplication)this.getApplication();
        //myApplication.showInterstitial(this);
        //AdView mAdView = findViewById(R.id.adView);

        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        ccp=findViewById(R.id.spinner);
        number=findViewById(R.id.number);
        message=findViewById(R.id.message);
        send=findViewById(R.id.send);
        clear=findViewById(R.id.clear);
        ccp.registerPhoneNumberTextView(number);

        ccp.enableHint(false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            ccp.setCountryForNameCode(((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase());
        }catch (Exception ignored)
        {

        }
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
       //// AdView mAdView = findViewById(R.id.adView);
     //  AdRequest adRequest = new AdRequest.Builder().build();
       // mAdView.loadAd(adRequest);
    }

    public void send()
    {
        //int contCode=91;
        //Toast.makeText(this, ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
        if(!ccp.isValid()) {
            Toast.makeText(this, getString(R.string.phonenumbernotvalid), Toast.LENGTH_SHORT).show();
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

        //sharedPreferences.edit().putString("recent_country",ccp.getSelectedCountryNameCode()).apply();
        sharedPreferences.edit().putString("recent_number",number.getText().toString()).apply();
    }


}