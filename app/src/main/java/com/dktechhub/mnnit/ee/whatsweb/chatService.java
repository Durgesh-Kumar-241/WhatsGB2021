package com.dktechhub.mnnit.ee.whatsweb;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class chatService extends AccessibilityService {
    String number,name,message;
    private static chatService instance;
    public chatService() {
    }

    @Override
    public void onAccessibilityEvent (AccessibilityEvent event) {
        if (getRootInActiveWindow () == null||message==null) {
            return;
        }
        if(event.getPackageName()!=null)
        {
             if(event.getPackageName().toString().startsWith("com.whatsapp"))
            {
                //Toast.makeText(this,event.getEventType(), Toast.LENGTH_SHORT).show();
                   // Toast.makeText(this, "Window chganged", Toast.LENGTH_SHORT).show();
                    AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

                    // Whatsapp Message EditText id
                    List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
                    if (messageNodeList == null || messageNodeList.isEmpty ()) {
                        return;
                    }

                    // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
                    AccessibilityNodeInfoCompat messageField = messageNodeList.get (0);
                    if (messageField.getText () == null || messageField.getText ().length () == 0|| !(messageField.getText().toString().equals(message))) { // So your service doesn't process any message, but the ones ending your apps suffix
                        return;
                    }

                    // Whatsapp send button id
                    List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
                    if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
                        return;
                    }

                    AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get (0);
                    if (!sendMessageButton.isVisibleToUser ()) {
                        return;
                    }

                    // Now fire a click on the send button
                    sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);
                    message=null;
                //Thread.sleep (100);// hack for certain devices in which the immediate back click is too fast to handle

                //Thread.sleep (100);  // same hack as above
                
                }

        }
    }

    public void sendMessage(String message,String number)
    {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("android.intent.extra.TEXT", message);
            intent.putExtra("jid", number+"@s.whatsapp.net");
            startActivity(intent);
        } catch (Exception unused) {

            Toast.makeText(this, getString(R.string.errortryagainlater)+unused.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        instance=this;
    }
    public static chatService getInstance()
    {
        return instance;
    }
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

    }
}