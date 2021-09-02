package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.dktechhub.mnnit.ee.whatsweb.R;
import com.dktechhub.mnnit.ee.whatsweb.WContact;

import java.util.ArrayList;

public class WContactsManager {
    private static DBHelper dbHelper;
    public static SharedPreferences sharedPreferences;


    public static ArrayList<wContact> refreshContactsDatabase(Context context) {

        ArrayList<wContact> arrayList = new ArrayList<>();
        dbHelper = new DBHelper(context);


       // String C = C1105v.m3128C("+d639tCvHclE2RH+cmBmNVTDW1cAfOHKICUgpXfVn3E29S7OASqzPIQCnt76aou06m013fmNKNI=");
        try (Cursor query = context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "account_type= ?", new String[]{"com.whatsapp"}, "display_name")) {
            while (query.moveToNext()) {
                long j = query.getLong(query.getColumnIndex("_id"));
                String string = query.getString(query.getColumnIndex("display_name"));
                String string2 = query.getString(query.getColumnIndex("data1"));
                String string3 = query.getString(query.getColumnIndex("mimetype"));
                if (string != null) {
                    Log.w("gggg", "  Name: " + string + "  Number: " + string2 + "  voip1: " + j + "  type: " + string3);
                    long j2 = 1 + j;
                    if (string2.contains("@")) {
                        string2 = string2.substring(0, string2.indexOf("@")).trim();
                    }

                    Log.d("gggg","Name ' "+string+" Number: "+string2+" ");
                    arrayList.add(new wContact(string, string2, j + "", j2 + ""));
                    String str2 = j + "";
                    String str3 = j2 + "";
                    SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();




                    if (writableDatabase != null) {
                        try {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Name", string);
                            contentValues.put("Number", string2);
                            contentValues.put("vo1", str2);
                            contentValues.put("vo2", str3);
                            writableDatabase.insert("contactsW", null, contentValues);
                           // writableDatabase.close();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        } catch (Throwable th) {
                            writableDatabase.close();
                            throw th;
                        }
                        writableDatabase.close();
                    }
                }
            }
        }
        return arrayList;
    }
    public static String getSimCountryCodeISO(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase();
        //return "";
    }


    public static void setDeafaultCountryCode(Context context)
    {

         SharedPreferences sharedPreferences2 = context.getSharedPreferences("sharedPreferenceApplic", 0);
         String str;
        if (sharedPreferences2.getString("pref_countryCode", "").equals("")) {
            //SharedPreferences sharedPreferences2 = context.getSharedPreferences("sharedPreferenceApplic", 0);
            String t = getSimCountryCodeISO(context);

            if (!t.equals("")) {
                Toast.makeText(context,"Automatically detected country: "+t, Toast.LENGTH_SHORT).show();
                String[] stringArray = context.getResources().getStringArray(R.array.code_name_country);
                int length = stringArray.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        str = "";
                        break;
                    }
                    String[] split = stringArray[i].split(",");
                    if (split[1].trim().equals(t.trim())) {
                        str = split[0];
                        break;
                    }
                    i++;
                }
                sharedPreferences2.edit().putString("pref_countryCode",str).apply();
                //Log.d("Country_Code","+" + str);
            } else {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.app_country_code);
                BootstrapEditText bootstrapEditText = dialog.findViewById(R.id.edit_text_country);
                //bootstrapEditText.getText().toString();
                dialog.findViewById(R.id.button2).setOnClickListener(v -> {
                    sharedPreferences2.edit().putString("pref_countryCode",bootstrapEditText.getText().toString()).apply();
                    dialog.dismiss();
                    Toast.makeText(context, "Country code set to "+bootstrapEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                });
                dialog.findViewById(R.id.button1).setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
        }


        Log.d("Country_Code",sharedPreferences2.getString("pref_countryCode",""));
    }


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

    public static String getMob(String display_name,Context context)
    {
        ContentResolver cr =context.getContentResolver();
        String Mob="";
//RowContacts for filter Account Types
        Cursor query = cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                "account_type= ? and mimetype= ? and display_name= ? ",
                new String[]{"com.whatsapp","vnd.android.cursor.item/vnd.com.whatsapp.profile",display_name},
                "display_name COLLATE NOCASE");
        if(query!=null&&query.getCount()>0)
        {   if(query.moveToFirst()){
            do {
                //long id = query.getLong(query.getColumnIndex("_id"));
               // String display_name = query.getString(query.getColumnIndex("display_name"));
                String data_1 = query.getString(query.getColumnIndex("data1"));
                //String mimetype = query.getString(query.getColumnIndex("mimetype"));
                if(display_name!=null) {
                    //Log.w("gggg", "  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                   // long id2 = id + 1;
                    if (data_1.contains("@")) {
                        data_1 = data_1.substring(0, data_1.indexOf("@")).trim();
                    }
                    //Log.w("gggg", "Finally  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                    //loaded.add(new WContact(display_name,data_1,String.valueOf(id)));
                    Mob=data_1;
                    break;
                }
            }while (query.moveToNext());

        }
            query.close();

        }
    return Mob;

    }

}
