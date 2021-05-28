package com.dktechhub.mnnit.ee.whatsweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WhatsWeb";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTables(@NotNull SQLiteDatabase db)
    {
        String recentChats="CREATE TABLE recentChats (contactId INTEGER PRIMARY KEY,name TEXT,number TEXT,lastMessage TEXT,unseen INTEGER,lastTime TIME);",messages="CREATE TABLE messages(messageId INTEGER PRIMARY KEY,contactId INTEGER,sent BOOLEAN,message TEXT,messageTime TIME,seen BOOLEAN);";
        String mediaStorage="CREATE TABLE mediaStore(mediaId INTEGER PRIMARY KEY, messageId INTEGER,fileName TEXT,mimeType TEXT);";
        db.execSQL(recentChats);
        db.execSQL(messages);
        db.execSQL(mediaStorage);
        Log.d("tables","created");
    }

    public void updateRecentChatList()
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("contactId",1);
        contentValues.put("name","Test");
        contentValues.put("number","919616280088");
        db.insert("recentChats",null,contentValues);
    }
}
