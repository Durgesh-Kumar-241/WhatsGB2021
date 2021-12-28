package com.dktechhub.gbchat22.whatsweb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class newMessageObserver extends BroadcastReceiver {
    observerInterface observerInterface;
    newMessageObserver()
    {}


    newMessageObserver(observerInterface observerInterface)
    {
        this.observerInterface=observerInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       this.observerInterface.onUpdate();
    }
    public interface observerInterface{
        void onUpdate();
    }
}