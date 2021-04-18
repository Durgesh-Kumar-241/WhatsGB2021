package com.dktechhub.mnnit.ee.whatsappweb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsappweb.ui.dashboard.StatusFragment;
import com.dktechhub.mnnit.ee.whatsappweb.ui.home.HomeFragment;
import com.dktechhub.mnnit.ee.whatsappweb.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class MainActivityNew extends AppCompatActivity {
     Fragment fragment1 ;
    Fragment fragment2 ;
    Fragment fragment3 ;
    ViewPager viewPager;
    final FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        checkPermissions();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fragment1 = new HomeFragment();
        fragment2 = new StatusFragment();
        fragment3 = new NotificationsFragment();

       viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(5);
        ViewPagerAdapter madapter = new ViewPagerAdapter(getSupportFragmentManager());
        madapter.addFragment(fragment1,"Fragment1");
        madapter.addFragment(fragment2,"Fragment2");
        madapter.addFragment(fragment3,"Fragment3");
        viewPager.setAdapter(madapter);



        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
                if(id==R.id.navigation_home){
                    viewPager.setCurrentItem(0);
                    return true;}

                else if(id==R.id.navigation_dashboard){
                    viewPager.setCurrentItem(1);
                    return true;}

            if(id==R.id.Settings) {
                viewPager.setCurrentItem(2);
                return true;
            }

            return false;
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denined,Reading external storage is necessary for the app to work properly", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;
    public void checkPermissions()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
        }


    }
}