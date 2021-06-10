package com.dktechhub.mnnit.ee.whatswebcloner;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivityNew extends AppCompatActivity {
    //PermissionDetector permissionDetector;
    private final String[] allPermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    com.google.android.material.card.MaterialCardView whatsweb,statussaver,savedstatus, repeater, offline, direct, empty;
    //private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton fab;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        checkPermissions();
        whatsweb =findViewById(R.id.whasweb);
        statussaver=findViewById(R.id.statussaver);
        savedstatus=findViewById(R.id.savedstatus);
        repeater =findViewById(R.id.textr);
        offline =findViewById(R.id.offline);
        direct =findViewById(R.id.direct);
        empty =findViewById(R.id.empty);
        fab=findViewById(R.id.fab);
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=com.dktechhub.mnnit.ee.whatswebcloner");
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(i);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dktechhub.mnnit.ee.whatsweb")));
                }            }
        });
        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityNew.this,DirectChatActivity.class));
            }
        });
        whatsweb.setOnClickListener(v -> {
            //whatsappweb.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fullscreengo));
            String encriptedText = encriptedText();
            StringBuilder sb = new StringBuilder();
            sb.append("https://" + encriptedText + "/🌐/");
            sb.append(Locale.getDefault().getLanguage());
            //this.webView.loadUrl(sb.toString());
            openBrowser(sb.toString(),true,v);
        });

       savedstatus.setOnClickListener(v -> {
           ActivityOptions options =
                   ActivityOptions.makeSceneTransitionAnimation(
                           this, v, "shared_element_end_root");
           Intent intent=new Intent(MainActivityNew.this,SavedStatusActivity.class);
           startActivity(intent,options.toBundle());
       });
       statussaver.setOnClickListener(v -> {
           ActivityOptions options =
                   ActivityOptions.makeSceneTransitionAnimation(
                           this, v, "shared_element_end_root");
           Intent intent=new Intent(MainActivityNew.this,StatusSaverActivity.class);
           startActivity(intent,options.toBundle());
       });

       repeater.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivityNew.this,TextRepeater.class));
           }
       });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v,R.menu.menufab);
            }
        });
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
        String str = "";
        for (int i = 0; i < 16; i++) {
            str = str + ((char) ("uc`,uf_rq_nn,amk".charAt(i) + 2));
        }
        return str;
    }


    @SuppressWarnings("RestrictTo")
    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());
        // There is no public API to make icons show on menus.
        // IF you need the icons to show this works however it's discouraged to rely on library only
        // APIs since they might disappear in future versions.
        if (popup.getMenu() instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) popup.getMenu();
            //noinspection RestrictedApi
            menuBuilder.setOptionalIconsVisible(true);
            //noinspection RestrictedApi
            for (MenuItem item : menuBuilder.getVisibleItems()) {
                int iconMarginPx =
                        (int)
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

                if (item.getIcon() != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.setIcon(new InsetDrawable(item.getIcon(), iconMarginPx, 0, iconMarginPx, 0));
                    } else {
                        item.setIcon(
                                new InsetDrawable(item.getIcon(), iconMarginPx, 0, iconMarginPx, 0) {
                                    @Override
                                    public int getIntrinsicWidth() {
                                        return getIntrinsicHeight() + iconMarginPx + iconMarginPx;
                                    }
                                });
                    }
                }
            }
        }
        popup.setOnMenuItemClickListener(
                menuItem -> {
                    onMenuItemClicked(menuItem);
                    return true;
                });
        popup.show();
    }

    public void onMenuItemClicked(MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.rateus) {
            Uri uri = Uri.parse("market://details?id=com.dktechhub.mnnit.ee.whatswebcloner");
            try {
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dktechhub.mnnit.ee.whatsweb")));
            }
        }
    }


}