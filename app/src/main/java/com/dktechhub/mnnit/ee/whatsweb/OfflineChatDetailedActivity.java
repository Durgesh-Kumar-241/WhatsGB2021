package com.dktechhub.mnnit.ee.whatsweb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;

public class OfflineChatDetailedActivity extends AppCompatActivity {
    com.google.android.material.floatingactionbutton.FloatingActionButton sendButton;
    ImageButton imagePicker;
    com.vanniktech.emoji.EmojiEditText emojiEditText;
    ImageView imogiSwitch;
    //Access access;
    String number;
    DBHelper dbHelper;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_offline_chat_detailed);
        try {


            name = getIntent().getStringExtra("name");

             number = getIntent().getStringExtra("number");
             if(number.startsWith("+"))
                number = number.replace("+"," ");
             number = number.replaceAll("\\s","");
            String id = getIntent().getStringExtra("id");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                //actionBar.hide();
                actionBar.setTitle(name);
                actionBar.setSubtitle(number);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        }catch (Exception e)
        {
            this.finish();
        }
        if(number==null||number.length()==0)
            this.finish();


        imagePicker = findViewById(R.id.image_button_send_image);
        sendButton=findViewById(R.id.Float_send);
        emojiEditText=findViewById(R.id.emojiEditText);
        imogiSwitch=findViewById(R.id.imageView_emoji);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        showAlert();
        dbHelper=new DBHelper(this, 1);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //setupService();
    }



    public void send()
    {   if(emojiEditText.getText()==null||emojiEditText.getText().toString().length()==0)
    {
        Toast.makeText(this, "Enter a message to send", Toast.LENGTH_SHORT).show();
        return;
    }
        try{


            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra("jid", number + "@s.whatsapp.net");
            intent.putExtra("android.intent.extra.TEXT",emojiEditText.getText().toString()+"‏‏");
            startActivity(intent);
            emojiEditText.setText("");
        }catch (Exception e)
        {
            Toast.makeText(this, "Install Whatsapp first", Toast.LENGTH_SHORT).show();
        }
    }


    public void showAlert()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean show = sharedPreferences.getBoolean("alert_offline_chat_redirect",true);
        if(!show)
        {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Each time you send a message, you will be directed to whatsapp ,without changing the last seen time");
        builder.setTitle("Notice");
        
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.setNegativeButton("Do not show again", (dialog, which) -> {
            sharedPreferences.edit().putBoolean("alert_offline_chat_redirect",false).apply();
            dialog.dismiss();
        });

        builder.create().show();

    }
}