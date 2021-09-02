package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.room.Database;

import com.dktechhub.mnnit.ee.whatsweb.Access;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class DBHelper extends SQLiteOpenHelper {
    //SQLiteDatabase sqLiteDatabase;
    // SQLiteDatabase db;
    Context context;
    HashMap<String, Integer> IdTitleMap = new HashMap<>();
    HashMap<String, String> mobTitleMap = new HashMap<>();

    static DBHelper instance;

    public static DBHelper getInstance(Context context)
    {
        if(instance==null)
            instance=new DBHelper(context);
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
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null) {
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
                writableDatabase.insert("notificationText", null, contentValues);
            } catch (Exception unused) {
            } catch (Throwable th) {
                writableDatabase.close();
                throw th;
            }
            writableDatabase.close();
        }
    }


    public ArrayList<WMessage> getMessageByTitleId(Integer notificationTitleId) {
        ArrayList<WMessage> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationText where IDTitle= ?", new String[]{String.valueOf(notificationTitleId)});
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                readableDatabase.close();
                return arrayList;
            }
            do {
                arrayList.add(new WMessage( rawQuery.getString(1), rawQuery.getString(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getInt(5) == 1, rawQuery.getString(6)));
            } while (rawQuery.moveToNext());
            rawQuery.close();
            readableDatabase.close();
            return arrayList;
        } catch (Exception unused) {
        } catch (Throwable th) {
            readableDatabase.close();
            throw th;
        }
        return arrayList;
    }




    public ArrayList<NotificationTitle> loadNotificationData() {
        ArrayList<NotificationTitle> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationTitle", null);
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                readableDatabase.close();
                return arrayList;
            }
            do {
                arrayList.add(new NotificationTitle(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getBlob(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getString(5), rawQuery.getString(6)));
            } while (rawQuery.moveToNext());
            rawQuery.close();
            readableDatabase.close();
            return arrayList;
        } catch (Exception unused) {
        } catch (Throwable th) {
            readableDatabase.close();
            throw th;
        }

        return arrayList;
    }

    public ArrayList<wContact> loadAllContacts() {
        ArrayList<wContact> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from contactsW", null);
        if (rawQuery != null) {
            try {
                if (rawQuery.moveToFirst()) {
                    do {
                        arrayList.add(new wContact(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getString(2), rawQuery.getString(3), rawQuery.getString(4)));
                    } while (rawQuery.moveToNext());
                }
            } catch (Exception ignored) {
            } catch (Throwable th) {
                readableDatabase.close();
                throw th;
            }
        }
        if (rawQuery != null && !rawQuery.isClosed()) {
            rawQuery.close();
        }
        readableDatabase.close();
        return arrayList;
    }

    public void deleteAllContactsdata() {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS contactsW");
        getWritableDatabase().execSQL("create table contactsW(ID integer primary key autoincrement, Name text, Number text, vo1 text, vo2 text)");
    }

    public void insertNotificationData(NotificationTitle notificationTitle) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {

                Cursor qwery = db.rawQuery("SELECT notificationTitle.id FROM notificationTitle WHERE Title = ? ", new String[]{notificationTitle.title});
                if (qwery.getCount() == 0) {
                    //Title text, Photo BLOB, Count integer, date text, number text
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Title", notificationTitle.title);
                    contentValues.put("Photo", notificationTitle.photo);
                    contentValues.put("Count", notificationTitle.count);
                    contentValues.put("date", notificationTitle.date);
                    contentValues.put("number", notificationTitle.number);
                    contentValues.put("summary", notificationTitle.summary);
                    db.insert("notificationTitle", null, contentValues);
                } else {
                    ContentValues contentValues = new ContentValues();
                    //contentValues.put("Title", notificationTitle.title);
                    contentValues.put("Photo", notificationTitle.photo);
                    contentValues.put("Count", notificationTitle.count);
                    contentValues.put("date", notificationTitle.date);
                    contentValues.put("number", notificationTitle.number);
                    contentValues.put("summary", notificationTitle.summary);
                    db.update("notificationTitle", contentValues, "Title = ?", new String[]{notificationTitle.title});
                }
                qwery.close();
            } catch (Exception unused) {
                unused.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
                db.close();
                throw th;
            }
            db.close();
        }
    }


    public DBHelper(Context context) {
        super(context, "gb_data", null, 1);
        this.context = context;instance=this;

    }

    public String getMob(String display_name) {
        if (mobTitleMap.containsKey(display_name)) {
            return mobTitleMap.get(display_name);
        }
        ContentResolver cr = context.getContentResolver();
        String Mob = "";
//RowContacts for filter Account Types
        Cursor query = cr.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                "account_type= ? and mimetype= ? ",
                new String[]{"com.whatsapp", "vnd.android.cursor.item/vnd.com.whatsapp.profile"},
                "display_name COLLATE NOCASE");
        if (query != null && query.getCount() > 0) {
            if (query.moveToFirst()) {
                do {
                    //long id = query.getLong(query.getColumnIndex("_id"));
                    String display_name1 = query.getString(query.getColumnIndex("display_name"));
                    String data_1 = query.getString(query.getColumnIndex("data1"));
                    //String mimetype = query.getString(query.getColumnIndex("mimetype"));
                    if (display_name != null) {
                        //Log.w("gggg", "  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                        // long id2 = id + 1;
                        if (data_1.contains("@")) {
                            data_1 = data_1.substring(0, data_1.indexOf("@")).trim();
                        }
                        //Log.w("gggg", "Finally  Name: " + display_name + "  Number: " + data_1 + "  voip1: " + id + "  type: " + mimetype);
                        //loaded.add(new WContact(display_name,data_1,String.valueOf(id)));
                        mobTitleMap.put(display_name1, data_1);
                        Mob = data_1;
                        break;
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
            SQLiteDatabase readableDatabase = getReadableDatabase();
            try {
                Cursor rawQuery = readableDatabase.rawQuery("select * from notificationTitle", null);
                if (rawQuery == null || !rawQuery.moveToFirst()) {
                    rawQuery.close();
                    readableDatabase.close();
                    return -1;
                }
                do {
                   // arrayList.add(new NotificationTitle(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getBlob(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getString(5), rawQuery.getString(6)));
               IdTitleMap.put(rawQuery.getString(1),rawQuery.getInt(0));
               if(rawQuery.getString(1).equals(notificationTitle.title))
               { id = rawQuery.getInt(0);break;}
                } while (rawQuery.moveToNext());
                rawQuery.close();
                readableDatabase.close();
                return id;
            } catch (Exception unused) {
            } catch (Throwable th) {
                readableDatabase.close();
                throw th;
            }

        }
    return id;
    }
}


