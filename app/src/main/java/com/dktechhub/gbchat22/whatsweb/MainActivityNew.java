package com.dktechhub.gbchat22.whatsweb;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivityNew extends AppCompatActivity {
    //PermissionDetector permissionDetector;
    TextView statussaver, direct,offlineChat,more;
            LinearLayout rate_us;


    MyApplication myApplication;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        myApplication= (MyApplication) getApplication();
        ((MyApplication)getApplication()).showInterstitialIfReady(this);

        offlineChat=findViewById(R.id.offlinechat);
        direct =findViewById(R.id.dchat);
        statussaver=findViewById(R.id.statussaver);
        more = findViewById(R.id.more);
        rate_us=findViewById(R.id.rate_us);
        rate_us.setOnClickListener(v -> rate(null));
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


    }



    @Override
    protected void onResume() {
        super.onResume();
        //showInters();
    }






    public void showMoreOptions()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.layout_more);

        TextView empty = bottomSheetDialog.findViewById(R.id.empty_text);
        TextView rep = bottomSheetDialog.findViewById(R.id.text_rep);

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
                bottomSheetDialog.dismiss();
            });
        }

        if(rep!=null)
        {
            rep.setOnClickListener(v -> {

                startActivity(new Intent(MainActivityNew.this, trpr.class));
                bottomSheetDialog.dismiss();
            });
        }
        bottomSheetDialog.show();
    }


    public void rate(View view)
    {
        Uri uri = Uri.parse("market://details?id="+"com.dktechhub.gbchat22.whatsweb");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(i);
        }catch (Exception e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+"com.dktechhub.gbchat22.whatsweb")));//Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

