package com.dktechhub.mnnit.ee.whatswebcloner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_picker);
        getPermissionToReadUserContacts();
        RecyclerView recyclerView = findViewById(R.id.chatlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ConactsAdapter conactsAdapter = new ConactsAdapter(this);
        conactsAdapter.setOnItemClickListener(new ConactsAdapter.OnItemClickListener() {
            @Override
            public void onContactCicked(Contact contact) {
                onChatPicked(contact);
            }
        });
        recyclerView.setAdapter(conactsAdapter);
        new ContactLoader(new LoadCompleteListner(){

            @Override
            public void onLoaded(Contact loaded) {
                conactsAdapter.addContact(loaded);
                conactsAdapter.notifyDataSetChanged();
                Log.d("contact",loaded.name+loaded.profile_uri+loaded.number);
            }
        }).execute();


    }

    public class ContactLoader extends AsyncTask<Void,Contact,Void>{
        LoadCompleteListner loadCompleteListner;
        ArrayList<Contact> mContact=new ArrayList<>();

        public ContactLoader(LoadCompleteListner loadCompleteListner) {
            this.loadCompleteListner=loadCompleteListner;
        }


        @Override
        protected void onProgressUpdate(Contact... values) {
            super.onProgressUpdate(values);
            loadCompleteListner.onLoaded(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final String[] projection={
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Data.MIMETYPE,
                    "account_type",
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.PHOTO_URI,
            };

            final String selection= ContactsContract.Data.MIMETYPE+" =? and account_type=?";
            final String[] selectionArgs = {
                    "vnd.android.cursor.item/vnd.com.whatsapp.profile",
                    "com.whatsapp"
            };
            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
            while(c.moveToNext()){
                String id=c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                String numberW=c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
                String[] parts = numberW.split("@");
                String numberPhone = parts[0];
                String number = "Tel : + " + numberPhone.substring(0, 2) + " " + numberPhone.substring(2);
                String image_uri = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                //photoList.add(image_uri);
                String name="";
                Cursor mCursor=getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                        ContactsContract.Contacts._ID+" =?",
                        new String[]{id},
                        null);
                while(mCursor.moveToNext()){
                    name=mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d("contact",name+number+image_uri);
                }
                mCursor.close();
                publishProgress(new Contact(name,numberPhone,image_uri));

            }
            c.close();
            return null;
        }


    }

    public interface LoadCompleteListner{
        void onLoaded(Contact loaded);
    }



    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    /**
     *
     * Called when the user is performing an action which requires the app to read the user's contacts
     */
    public void getPermissionToReadUserContacts() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {

            }
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * Callback with the request from calling requestPermissions(...)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onChatPicked(Contact contact)
    {
        Intent i = new Intent();
        i.putExtra("name",contact.name);
        i.putExtra("number",contact.number);
        i.putExtra("photo_uri",contact.profile_uri);
        setResult(RESULT_OK,i);
        finish();
    }
}