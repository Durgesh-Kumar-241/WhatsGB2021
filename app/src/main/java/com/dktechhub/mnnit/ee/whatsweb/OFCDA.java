package com.dktechhub.mnnit.ee.whatsweb;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.mnnit.ee.whatsweb.Utils.DBHelper;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTextAdapter;
import com.dktechhub.mnnit.ee.whatsweb.Utils.NotificationTitle;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WMessage;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.vanniktech.emoji.EmojiPopup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class OFCDA extends AppCompatActivity {
    AdView adView;
    com.google.android.material.floatingactionbutton.FloatingActionButton sendButton;
    ImageButton imagePicker;
    com.vanniktech.emoji.EmojiEditText emojiEditText;
    ImageView imogiSwitch;
    //Access access;
    String number;
    DBHelper dbHelper;
    String name;
    RecyclerView recyclerView;
    int id=-1;
    boolean isPrivate = false;
    SimpleDateFormat simpleDateFormat ;
    NotificationTextAdapter adapter = new NotificationTextAdapter();
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_offline_chat_detailed);
        loadAd();

        //adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container



        simpleDateFormat = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        recyclerView=findViewById(R.id.recycler_view);
        try {


            name = getIntent().getStringExtra("name");

             number = getIntent().getStringExtra("number");
            int id = getIntent().getIntExtra("id",-1);
            if(id!=-1)
                this.id=id;
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null)
            {
                if(name!=null)
                    actionBar.setTitle(name);
                if(number!=null&&number.length()>0)
                {actionBar.setSubtitle(number);isPrivate=true;}
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


        }catch (Exception e)
        {
            this.finish();
        }



        imagePicker = findViewById(R.id.image_button_send_image);
        sendButton=findViewById(R.id.Float_send);
        emojiEditText=findViewById(R.id.emojiEditText);
        imogiSwitch=findViewById(R.id.imageView_emoji);
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.rootView)).build(emojiEditText);
        imogiSwitch.setOnClickListener(v -> emojiPopup.toggle());
        sendButton.setOnClickListener(v -> send());

        showAlert();

        if(!isPrivate)
        {
            emojiEditText.setText(R.string.group_msg_send_warning);
            emojiEditText.setEnabled(false);
            sendButton.setEnabled(false);
        }

        dbHelper=new DBHelper(this);



        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        if(this.id==-1)
        {
            NotificationTitle notificationTitle =  new NotificationTitle(null,name,null,12,simpleDateFormat.format(new Date()),number,"");
            id =dbHelper.getNotificationTitleId(notificationTitle);
        }

        if(this.id!=-1)
        {
             adapter.setmList(dbHelper.getMessageByTitleId(id)); }



        i = new Intent();
        i.setAction("com.dktechhub.mnnit.ee.whatsweb.newMessageObserver");
        startObserver();

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
            String time =simpleDateFormat.format(new Date());
            NotificationTitle notificationTitle =  new NotificationTitle(null,name,null,12,time,number,emojiEditText.getText().toString());

            int id =dbHelper.insertNotificationData(notificationTitle);
            WMessage wMessage =new WMessage(emojiEditText.getText().toString(),time,id,null,false,null);
            dbHelper.insertMessage(wMessage);
            //adapter.addMessage(wMessage);
            emojiEditText.setText("");
            sendBroadcast(i);
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

    public void updateUI()
    {
        adapter.setmList(dbHelper.getMessageByTitleId(id));
        //recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    newMessageObserver observer;
    public void startObserver()
    {
        observer = new newMessageObserver(this::updateUI);
        registerReceiver(observer,new IntentFilter("com.dktechhub.mnnit.ee.whatsweb.newMessageObserver"));
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
        if (adView != null) {
            adView.destroy();
        }
    }


    public void loadAd()
    {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.ban_ofcld));
        LinearLayout linearLayout = findViewById(R.id.banner_container);
        TextView tv = findViewById(R.id.ad_cont_test);
        linearLayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                tv.setVisibility(View.GONE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }



}