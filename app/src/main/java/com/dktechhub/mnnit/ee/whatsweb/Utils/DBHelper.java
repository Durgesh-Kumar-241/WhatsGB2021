package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    //SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase db;

    public void createMessage(String text, int incoming, String number)
    {     }

    public void cretaMessageOutgoing(String text,String number)
    {
         }

    public void createMessageIncoming(String text,String number)
    {

    }

    public void loadMessageAll(String number)
    {

    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notificationTitle(ID integer primary key autoincrement, Title text, Photo BLOB, Count integer, date text, number text)");
        db.execSQL("create table notificationText(ID integer primary key autoincrement, Title text, Text text, date text, IDTitle integer, pathPhoto text,direction text, pathVoice text)");
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

    public int mo5007l(Integer num, String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return -1;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", str);
            return writableDatabase.update("notificationTitle", contentValues, "ID = ?", new String[]{String.valueOf(num)});
        } catch (Exception unused) {
            return -1;
        } finally {
            writableDatabase.close();
        }
    }

    public int mo5006k(String str) {
        int i = -1;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select ID from notificationText where pathVoice= ? ORDER BY ID DESC LIMIT 1", new String[]{" "});
            if (rawQuery != null && rawQuery.moveToLast()) {
                i = rawQuery.getInt(0);
            }
            rawQuery.close();
        } catch (Exception ignored) {
        } catch (Throwable th) {
            readableDatabase.close();
            throw th;
        }
        readableDatabase.close();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("pathVoice", str);
                return writableDatabase.update("notificationText", contentValues, "ID = ?", new String[]{String.valueOf(i)});
            } catch (Exception ignored) {
            } finally {
                writableDatabase.close();
            }
        }
        return -1;
    }


    public int mo5005j(Integer num, String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return -1;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("number", str);
            return writableDatabase.update("notificationTitle", contentValues, "ID = ?", new String[]{String.valueOf(num)});
        } catch (Exception unused) {
            return -1;
        } finally {
            writableDatabase.close();
        }
    }

    public int mo5004i(Integer num, Integer num2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase == null) {
            return -1;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Count", num2);
            return writableDatabase.update("notificationTitle", contentValues, "ID = ?", new String[]{String.valueOf(num)});
        } catch (Exception unused) {
            return -1;
        } finally {
            writableDatabase.close();
        }
    }

    public void mo5003h(C1734e2 e2Var) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase != null) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("Title", e2Var.f5474a);
                contentValues.put("Text", e2Var.f5475b);
                contentValues.put("date", e2Var.f5476c);
                contentValues.put("IDTitle", e2Var.f5477d);
                contentValues.put("pathPhoto", e2Var.f5478e);
                contentValues.put("direction", e2Var.f5479f);
                contentValues.put("pathVoice", e2Var.f5480g);
                contentValues.put("timeMilleSecond", Long.valueOf(e2Var.f5481h));
                writableDatabase.insert("notificationText", null, contentValues);
            } catch (Exception unused) {
            } catch (Throwable th) {
                writableDatabase.close();
                throw th;
            }
            writableDatabase.close();
        }
    }


    public ArrayList<C1734e2> mo5001f(String str) {
        ArrayList<C1734e2> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationText where Title= ?", new String[]{str});
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                readableDatabase.close();
                return arrayList;
            }
            do {
                arrayList.add(new C1734e2(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getString(2), rawQuery.getString(3), Integer.valueOf(rawQuery.getInt(4)), rawQuery.getString(5), rawQuery.getString(6), rawQuery.getString(7), rawQuery.getLong(8)));
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

    public C1734e2 mo5000e(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        C1734e2 e2Var = null;
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationText where Title= ?", new String[]{str});
            if (rawQuery != null && rawQuery.getCount() > 0 && rawQuery.moveToLast()) {
                e2Var = new C1734e2(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getString(2), rawQuery.getString(3), Integer.valueOf(rawQuery.getInt(4)), rawQuery.getString(5), rawQuery.getString(6), rawQuery.getString(7), rawQuery.getLong(8));
            }
            rawQuery.close();
        } catch (Exception unused) {
        } catch (Throwable th) {
            readableDatabase.close();
            throw th;
        }
        readableDatabase.close();
        return e2Var;
    }

    public Integer mo4999d(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Integer num = null;
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationTitle where Title= ? and number= ?", new String[]{str, str2});
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                readableDatabase.close();
                return num;
            }
            do {
                num = rawQuery.getInt(0);
            } while (rawQuery.moveToNext());
            rawQuery.close();
            readableDatabase.close();
            return num;
        } catch (Exception unused) {
        } catch (Throwable th) {
            readableDatabase.close();
            throw th;
        }
        return 0;
    }

    public ArrayList<C1738f2> mo4998c() {
        ArrayList<C1738f2> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        try {
            Cursor rawQuery = readableDatabase.rawQuery("select * from notificationTitle", null);
            if (rawQuery == null || !rawQuery.moveToFirst()) {
                rawQuery.close();
                readableDatabase.close();
                return arrayList;
            }
            do {
                arrayList.add(new C1738f2(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getBlob(2), rawQuery.getInt(3), rawQuery.getString(4), rawQuery.getString(5)));
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

    public ArrayList<C1730d2> mo4997b() {
        ArrayList<C1730d2> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from contactsW", null);
        if (rawQuery != null) {
            try {
                if (rawQuery.moveToFirst()) {
                    do {
                        arrayList.add(new C1730d2(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getString(2), rawQuery.getString(3), rawQuery.getString(4)));
                    } while (rawQuery.moveToNext());
                }
            } catch (Exception ignored) {
            } catch (Throwable th) {
                readableDatabase.close();
                throw th;
            }
        }
        if (rawQuery!=null&&!rawQuery.isClosed()) {
            rawQuery.close();
        }
        readableDatabase.close();
        return arrayList;
    }

    public void deleteAllContactsdata() {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS contactsW");
        getWritableDatabase().execSQL("create table contactsW(ID integer primary key autoincrement, Name text, Number text, vo1 text, vo2 text)");
    }




    public DBHelper(Context context)
    {
        super(context,"gb_data",null,1);
    }


    public static class Message{
        String text;
        boolean incoming;
        String dateTime;
    }
}
