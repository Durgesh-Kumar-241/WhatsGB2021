package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.dktechhub.mnnit.ee.whatsweb.WContact;

import java.util.ArrayList;

public class WContactsManager {
    public static SharedPreferences sharedPreferences;


    public static ArrayList<WContact> loadContacts(Context context)
    {


        ArrayList<WContact> loaded = new ArrayList<>();



        //This class provides applications access to the content model.
        ContentResolver cr =context.getContentResolver();

//RowContacts for filter Account Types
        Cursor query = cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                "account_type= ? and mimetype= ?",
                new String[]{"com.whatsapp","vnd.android.cursor.item/vnd.com.whatsapp.profile"},
                "display_name COLLATE NOCASE");

//ArrayList for Store Whatsapp Contact
        // ArrayList<String> myWhatsappContacts = new ArrayList<>();

        if(query!=null&&query.getCount()>0)
        {   if(query.moveToFirst()){
            do {
                long id = query.getLong(query.getColumnIndex("_id"));
                String display_name = query.getString(query.getColumnIndex("display_name"));
                String data_1 = query.getString(query.getColumnIndex("data1"));
                //String mimetype = query.getString(query.getColumnIndex("mimetype"));
                if(display_name!=null) {
                    //Log.w("gggg", "  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                    long id2 = id + 1;
                    if (data_1.contains("@")) {
                        data_1 = data_1.substring(0, data_1.indexOf("@")).trim();
                    }
                    //Log.w("gggg", "Finally  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                    loaded.add(new WContact(display_name,data_1,String.valueOf(id)));
                }
            }while (query.moveToNext());

        }
            query.close();

        }





        //showLogI(TAG, " WhatsApp contact size :  " + myWhatsappContacts.size());

        return loaded;
    }

}
