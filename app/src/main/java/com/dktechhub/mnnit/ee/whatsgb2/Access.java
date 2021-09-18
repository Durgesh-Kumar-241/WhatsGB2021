package com.dktechhub.mnnit.ee.whatsgb2;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class Access extends AccessibilityService {
    static Access instance;
    ActivityManager activityManager;
    int selfTaskId=-1;
    public Access() {
        instance=this;
    }

   public  Access getInstance()
    {
        if(instance==null)
            instance=new Access();
        return instance;
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("event ","detected");
        if (getRootInActiveWindow () == null) {
            return;
        }

        try {

            AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());
            // Whatsapp Message EditText id
            List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
            if (messageNodeList == null || messageNodeList.isEmpty()) {
                return;
            }

            // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
            AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
            if (messageField.getText() == null || messageField.getText().length() == 0 ||
                    !messageField.getText().toString().endsWith("‏‏")) { // So your service doesn't process any message, but the ones ending your apps suffix
                return;
            }

            // Whatsapp send button id
            List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
                return;
            }

            AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
            if (!sendMessageButton.isVisibleToUser()) {
                return;
            }

            // Now fire a click on the send button
            sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            if (selfTaskId != -1) {
                if (activityManager == null)
                    activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.moveTaskToFront(selfTaskId, 0, null);
            } else
                Toast.makeText(getApplicationContext(), "Failed,close the app and open again", Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {

    }

    public void setTaskId(int id)
    {
        this.selfTaskId=id;
    }

}