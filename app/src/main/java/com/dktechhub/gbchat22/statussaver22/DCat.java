package com.dktechhub.gbchat22.statussaver22;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.net.URLEncoder;

public class DCat extends AppCompatActivity {
    TextView send,clear;
    EditText number,message;
    CountryCodePicker ccp;
    SharedPreferences sharedPreferences;

   //AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_direct_chat);
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
        send.setOnClickListener(v -> send());

        clear.setOnClickListener(v -> {
            number.setText("");
            message.setText("");
        });

    }

    public void send()
    {
           if(!ccp.isValid()) {
            Toast.makeText(this, getString(R.string.phonenumbernotvalid), Toast.LENGTH_SHORT).show();
            return;
        }
        String smessage = message.getText().toString();
         try{
             String url = "https://api.whatsapp.com/send/?phone="+ccp.getFullNumber()+"&text="+ URLEncoder.encode(smessage,"utf-8");

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