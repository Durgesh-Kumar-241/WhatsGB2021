package com.dktechhub.mnnit.ee.whatsweb;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.dktechhub.mnnit.ee.whatsweb.ui.main.SectionsPagerAdapter;

public class stsact extends AppCompatActivity {

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        myApplication=(MyApplication)this.getApplication();
        myApplication.showInterstitial(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new FragmentStaus(false),"Recent");
        sectionsPagerAdapter.addFragment(new FragmentStaus(true),"Saved");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }
}