package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    //SQLiteDatabase sqLiteDatabase;
    // SQLiteDatabase db;
    Context context;
    HashMap<String, Integer> IdTitleMap = new HashMap<>();
    HashMap<String, String> mobTitleMap = new HashMap<>();
    SQLiteDatabase readable,writable;
    static DBHelper instance;

    public static DBHelper getInstance(Context context)
    {
        if(instance==null)
        {instance=new DBHelper(context);
            Log.d("DBHelper","New Instance "+context.toString());}
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notificationTitle(ID integer primary key autoincrement, Title text, Photo BLOB, Count integer, date text, number text,summary text)");
        db.execSQL("create table notificationText(ID integer primary key autoincrement, Text text, date text, IDTitle integer, pathPhoto text,incoming boolean, pathVoice text )");
        db.execSQL("create table contactsW(ID integer primary key autoincrement, Name text, Number text, vo1 text, vo2 text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("Drop table if exists notificationTitle");
            db.execSQL("Drop table if exists notificationText");
            db.execSQL("Drop table if exists contactsW");
            onCreate(db);
        }

    }





    public void insertMessage(WMessage e2Var) {
        //SQLiteDatabase writableDatabase = writable;
        if (writable != null) {
            try {
                ContentValues contentValues = new ContentValues();
                //contentValues.put("Title", e2Var.title);
                contentValues.put("Text", e2Var.text);
                contentValues.put("date", e2Var.date);
                contentValues.put("IDTitle", e2Var.idTitle);
                contentValues.put("pathPhoto", e2Var.pathPhoto);
                contentValues.put("incoming", e2Var.incoming ? 1 : 0);
                contentValues.put("pathVoice", e2Var.pathVoice);
                //contentValues.put("timeMilleSecond", e2Var.timeMilleSecond);
                writable.insert("notificationText", null, contentValues);
            } catch (Exception ignored) {
            }
            //writable.close();
        }
    }


    public ArrayList<WMessage> getMessageByTitleId(Integer notificationTitleId) {
        ArrayList<WMessage> arrayList = new ArrayList<>();
        //SQLiteDatabase readableDatabase = readable;
        try {
            Cursor rawQuery = readable.rawQuery("select * from notificationText where IDTitle= ?", new String[]{String.valueOf(notificationTitleId)});
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                //readable.close();
                return arrayList;
            }
            do {
                arrayList.add(new WMessage( rawQuery.getString(1), rawQuery.getString(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getInt(5) == 1, rawQuery.getString(6)));
            } while (rawQuery.moveToNext());
            rawQuery.close();
            //readable.close();
            return arrayList;
        } catch (Exception ignored) {
        }
        return arrayList;
    }




    public ArrayList<NotificationTitle> loadNotificationData() {
        ArrayList<NotificationTitle> arrayList = new ArrayList<>();

        try {
            Cursor rawQuery = readable.rawQuery("select * from notificationTitle", null);
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                //readable.close();
                return arrayList;
            }
            do {
                arrayList.add(new NotificationTitle(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getBlob(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getString(5), rawQuery.getString(6)));
            } while (rawQuery.moveToNext());
            rawQuery.close();
            //readable.close();
            return arrayList;
        } catch (Exception unused) {
        } catch (Throwable th) {
           // readable.close();
            throw th;
        }

        return arrayList;
    }



    public int insertNotificationData(NotificationTitle notificationTitle) {
       // SQLiteDatabase db = writable;
        long id =-1;
        if (writable != null) {
            try {

                Cursor qwery = writable.rawQuery("SELECT * FROM notificationTitle WHERE Title = ? ", new String[]{notificationTitle.title});
                if (qwery.getCount() == 0) {
                    //Title text, Photo BLOB, Count integer, date text, number text
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Title", notificationTitle.title);
                    contentValues.put("Photo", notificationTitle.photo);
                    contentValues.put("Count", notificationTitle.count);
                    contentValues.put("date", notificationTitle.date);
                    contentValues.put("number", notificationTitle.number);
                    contentValues.put("summary", notificationTitle.summary);
                  id =  writable.insert("notificationTitle", null, contentValues);

                } else {
                    ContentValues contentValues = new ContentValues();
                    //contentValues.put("Title", notificationTitle.title);
                    contentValues.put("Photo", notificationTitle.photo);
                    contentValues.put("Count", notificationTitle.count);
                    contentValues.put("date", notificationTitle.date);
                    contentValues.put("number", notificationTitle.number);
                    contentValues.put("summary", notificationTitle.summary);
                   writable.update("notificationTitle", contentValues, "Title = ?", new String[]{notificationTitle.title});
                   if(qwery.moveToFirst())
                    id=qwery.getLong(qwery.getColumnIndex("ID"));
                }
                qwery.close();
            } catch (Exception unused) {
                unused.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
                //writable.close();
                throw th;
            }
           // writable.close();
        }
        return (int) id;
    }


    public DBHelper(Context context) {
        super(context, "gb_data", null, 1);
        this.context = context;instance=this;
        writable=getWritableDatabase();
        readable=getReadableDatabase();

    }

    public String getMob(String display_name) {
        if (mobTitleMap.containsKey(display_name)) {
            return mobTitleMap.get(display_name);
        }
        ContentResolver cr = context.getContentResolver();
        //RowContacts for filter Account Types
        Cursor query = cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                "account_type= ? and mimetype= ? ",
                new String[]{"com.whatsapp", "vnd.android.cursor.item/vnd.com.whatsapp.profile"},
                "display_name COLLATE NOCASE");
        String Mob = "";
        if (query != null && query.getCount() > 0) {
            if (query.moveToFirst()) {
                do {

                    String display_name1 = query.getString(query.getColumnIndex("display_name"));
                    String data_1 = query.getString(query.getColumnIndex("data1"));

                    if (display_name != null) {
                        if (data_1.contains("@")) {
                            data_1 = data_1.substring(0, data_1.indexOf("@")).trim();
                        }
                        mobTitleMap.put(display_name1, data_1);
                        if(display_name.equals(display_name1))
                        {Mob = data_1;
                        break;}
                    }
                } while (query.moveToNext());

            }
            query.close();

        }
        return Mob;

    }



    public int getNotificationTitleId(NotificationTitle notificationTitle) {
        int id = -1;
        if (IdTitleMap.containsKey(notificationTitle.title)) {
            return IdTitleMap.get(notificationTitle.title);
        } else {
            //SQLiteDatabase readableDatabase = readable;
            try {
                Cursor rawQuery = readable.rawQuery("select * from notificationTitle", null);
                if (rawQuery == null || !rawQuery.moveToFirst()) {
                    rawQuery.close();
                    //readable.close();
                    return -1;
                }
                do {
                   // arrayList.add(new NotificationTitle(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getBlob(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getString(5), rawQuery.getString(6)));
               IdTitleMap.put(rawQuery.getString(1),rawQuery.getInt(0));
               if(rawQuery.getString(1).equals(notificationTitle.title))
               { id = rawQuery.getInt(0);break;}
                } while (rawQuery.moveToNext());
                rawQuery.close();
                //readable.close();
                return id;
            } catch (Exception unused) {
            } catch (Throwable th) {
                //readable.close();
                throw th;
            }

        }
    return id;
    }




    public void close()
    {
        if(readable.isOpen())
            readable.close();
        if(writable.isOpen())
            writable.close();
    }
}


