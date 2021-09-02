package com.dktechhub.mnnit.ee.whatsweb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTextAdapter;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WMessage;

import java.util.ArrayList;

public class OfflineChatDetailedActivity extends AppCompatActivity {
    com.google.android.material.floatingactionbutton.FloatingActionButton sendButton;
    ImageButton imagePicker;
    com.vanniktech.emoji.EmojiEditText emojiEditText;
    ImageView imogiSwitch;
    //Access access;
    String number;
    DBHelper dbHelper;
    String name;
    RecyclerView recyclerView;
    boolean isPrivate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_offline_chat_detailed);
        recyclerView=findViewById(R.id.recycler_view);
        try {


            name = getIntent().getStringExtra("name");

             number = getIntent().getStringExtra("number");
            String id = getIntent().getStringExtra("id");
            ActionBar actionBar = getSupportActionBar();
            if(name!=null)
                actionBar.setTitle(name);
            if(number!=null&&number.length()>0)
            {actionBar.setSubtitle(number);isPrivate=true;}

        }catch (Exception e)
        {
            this.finish();
        }



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

        if(!isPrivate)
        {
            emojiEditText.setText("You must receive atleast one message from this group to send any messages");
            emojiEditText.setEnabled(false);
            sendButton.setEnabled(false);
        }

        dbHelper=new DBHelper(this);

        ArrayList<WMessage> arrayList = new ArrayList<>();
        WMessage wMessage = new WMessage("Title","Tetx","12/02/2003",123,"/sdcard/fef",true,"/sdcard/helowdw");
        arrayList.add(wMessage);
        arrayList.add(wMessage);
        arrayList.add(wMessage);
        WMessage wMessage2 = new WMessage("Title","Tetx","12/02/2003",123,"/sdcard/fef",false,"/sdcard/helowdw");
        arrayList.add(wMessage2);
        arrayList.add(wMessage2);
        NotificationTextAdapter adapter = new NotificationTextAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper.insertMessage(wMessage);

        dbHelper.insertMessage(wMessage2);
        arrayList.addAll(dbHelper.getMessageByTitleId(123));
        adapter.setmList(arrayList);

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