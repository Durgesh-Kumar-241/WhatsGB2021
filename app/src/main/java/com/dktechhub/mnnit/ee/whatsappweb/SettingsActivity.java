package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment(() -> restart()))
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        OnPreferenceChangeListener onPreferenceChangeListener;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            /*SwitchPreference darktheme = findPreference("dark_theme");
            assert darktheme != null;
            darktheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    onPreferenceChangeListener.onPreferenceChanged();
                    return true;
                }
            });*/
        }
        public SettingsFragment(OnPreferenceChangeListener onPreferenceChangeListener)
        {
            this.onPreferenceChangeListener=onPreferenceChangeListener;
        }
    }

    public void restart()
    {
        AlertDialog.Builder alertDialogbuilder =new  AlertDialog.Builder(this);
        AlertDialog alertDialog=alertDialogbuilder.create();
        alertDialog.setTitle("Alert..");
        alertDialog.setMessage("Restart is required for updating the settings..Restart now?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent p = new Intent(SettingsActivity.this,SettingsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(SettingsActivity.this,12345,p,PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                //mgr.set(AlarmManager.RTC,System.currentTimeMillis()+20,pendingIntent);
                System.exit(0);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingsActivity.this, "Settings changed will take effect when the app will be started again..", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
    public void applyTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);
        if(dark_theme)
        {
            //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else setTheme(R.style.Theme_AppCompat_Light);
    }
    public interface OnPreferenceChangeListener{
        void onPreferenceChanged();
    }

}