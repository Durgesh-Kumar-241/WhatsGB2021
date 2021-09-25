package com.dktechhub.mnnit.ee.whatsweb;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

public class MainActivityNew extends AppCompatActivity {
    //PermissionDetector permissionDetector;
    private final String[] allPermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    TextView statussaver, repeater,  direct, empty,offlineChat;
    TextView whatsweb;
    AdView adView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        loadAd();
        checkPermissions();


        whatsweb =findViewById(R.id.whatsweb);
        statussaver=findViewById(R.id.statussaver);

        repeater =findViewById(R.id.textrepeater);

        direct =findViewById(R.id.directchat);
        empty =findViewById(R.id.emptytext);
        offlineChat=findViewById(R.id.directchat2);

        offlineChat.setOnClickListener(v -> startActivity(new Intent(MainActivityNew.this,OfflineChatList.class)));

        direct.setOnClickListener(v -> startActivity(new Intent(MainActivityNew.this,DirectChatActivity.class)));
        whatsweb.setOnClickListener(v -> {

            //whatsappweb.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fullscreengo));
            String encriptedText = encriptedText();
            //this.webView.loadUrl(sb.toString());
            String sb = "https://" + encriptedText + "/🌐/" +
                    Locale.getDefault().getLanguage();
            openBrowser(sb,true,v);
        });


       statussaver.setOnClickListener(v -> {

           ActivityOptions options =
                   ActivityOptions.makeSceneTransitionAnimation(
                           this, v, "shared_element_end_root");
           Intent intent=new Intent(MainActivityNew.this,StatusActivity.class);
           startActivity(intent,options.toBundle());
       });

       repeater.setOnClickListener(v -> startActivity(new Intent(MainActivityNew.this,TextRepeater.class)));
        empty.setOnClickListener(v -> {

            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra("android.intent.extra.TEXT", "‏‏");
                intent.putExtra("jid", "@s.whatsapp.net");
                startActivity(intent);
            } catch (Exception unused) {
                Toast.makeText(getBaseContext(), getString(R.string.errortryagainlater), Toast.LENGTH_LONG).show();
            }            });



    }

    public void openBrowser(String url,boolean requestdesktopSite,View v)
    {ActivityOptions options =
            ActivityOptions.makeSceneTransitionAnimation(
                    this, v, "shared_element_end_root");
        Intent intent=new Intent(MainActivityNew.this,MainFragment.class);
        intent.putExtra("url",url);
        intent.putExtra("requestDesktopSite",requestdesktopSite);
        startActivity(intent,options.toBundle());
    }


    public void checkPermissions()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return ;

        for (String permission : allPermissions) {
            if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(allPermissions,100);
            }
        }

    }

    private String encriptedText() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            str.append((char) ("uc`,uf_rq_nn,amk".charAt(i) + 2));
        }
        return str.toString();
    }





    public void loadAd()
    {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.banner));
        LinearLayout linearLayout = findViewById(R.id.banner_container);
        linearLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView!=null)
            adView.destroy();
    }
}