package com.dktechhub.mnnit.ee.whatsweb;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.w3c.dom.Text;

public class MainActivityNew extends AppCompatActivity {
    //PermissionDetector permissionDetector;
    private final String[] allPermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    TextView statussaver, direct,offlineChat,more;


    MyApplication myApplication;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        myApplication= (MyApplication) getApplication();
        myApplication.loadAd();
        checkPermissions();
        offlineChat=findViewById(R.id.offlinechat);
        direct =findViewById(R.id.dchat);
        statussaver=findViewById(R.id.statussaver);
        more = findViewById(R.id.more);
        more.setOnClickListener(v -> showMoreOptions());
        offlineChat.setOnClickListener(v -> {
            startActivity(new Intent(MainActivityNew.this, OFCLO.class));

        });

        direct.setOnClickListener(v -> {
            startActivity(new Intent(MainActivityNew.this, DCat.class));

        });



       statussaver.setOnClickListener(v -> {
           ActivityOptions options =
                   ActivityOptions.makeSceneTransitionAnimation(
                           this, v, "shared_element_end_root");
           Intent intent=new Intent(MainActivityNew.this, stsact.class);
           startActivity(intent,options.toBundle());
       });

       rate();

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

    @Override
    protected void onResume() {
        super.onResume();
        showInters();
    }



    public void showInters()
    {      try {
        myApplication.showInterstitial(this);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    }


    public void showMoreOptions()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.layout_more);

        builder.setCancelable(true);


        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        TextView empty = alertDialog.findViewById(R.id.empty_text);
        TextView rep = alertDialog.findViewById(R.id.text_rep);

        if (empty != null) {
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
                }
            });
        }

        if(rep!=null)
        {
            rep.setOnClickListener(v -> startActivity(new Intent(MainActivityNew.this, trpr.class)));
        }
    }


    public void rate()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rated = sharedPreferences.getBoolean("rated",false);
        int nused = sharedPreferences.getInt("nused",1);
        if(!rated&&nused>2&&nused%2==0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Hope you are enjoying the App. How about rating?");
            builder.setCancelable(true);
            builder.setPositiveButton("Rate now", (dialog, which) -> {
                Uri uri = Uri.parse("market://details?id="+"com.dktechhub.mnnit.ee.whatsweb");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(i);
                }catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+"com.dktechhub.mnnit.ee.whatsweb")));
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                sharedPreferences.edit().putBoolean("rated",true).apply();

                dialog.dismiss();
            });
            builder.setNegativeButton("Not now", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Never", (dialog, which) -> {
                sharedPreferences.edit().putBoolean("rated",true).apply();
                dialog.dismiss();
            });

            builder.create().show();

        }
    }

}

