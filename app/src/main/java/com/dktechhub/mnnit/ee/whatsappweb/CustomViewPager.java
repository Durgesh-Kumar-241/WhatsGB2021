package com.dktechhub.mnnit.ee.whatsappweb;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }
    public CustomViewPager(@NonNull Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);
    }
}
