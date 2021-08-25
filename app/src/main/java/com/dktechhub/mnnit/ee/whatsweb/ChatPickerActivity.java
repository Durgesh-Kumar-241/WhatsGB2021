package com.dktechhub.mnnit.ee.whatsweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsweb.Utils.ContactsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatPickerActivity extends AppCompatActivity {

    private NameComp nameComp;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_picker);
         recyclerView = findViewById(R.id.contactsList);
         contactsAdapter = new ContactsAdapter(this::startDetailedChat);
        contactsAdapter.setItems(loadContacts());
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //loadContacts();
    }

    ArrayList<WContact> loadContacts()
    {


        ArrayList<WContact> loaded = new ArrayList<>();



        //This class provides applications access to the content model.
        ContentResolver cr = getContentResolver();

//RowContacts for filter Account Types
        Cursor contactCursor = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

//ArrayList for Store Whatsapp Contact
       // ArrayList<String> myWhatsappContacts = new ArrayList<>();

        if (contactCursor != null) {
            if (contactCursor.getCount() > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null) {
                                whatsAppContactCursor.moveToFirst();
                                String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                               // number= PhoneNumberUtils.stripSeparators(number);
                                Log.d("Loaded",name+"\t"+number+"\t"+id);
                                whatsAppContactCursor.close();

                                //Add Number to ArrayList
                                loaded.add(new WContact(name,number,id));

                            }
                        }
                    } while (contactCursor.moveToNext());
                    contactCursor.close();
                }
            }
        }

        //showLogI(TAG, " WhatsApp contact size :  " + myWhatsappContacts.size());
        nameComp=new NameComp();
        Collections.sort(loaded,nameComp);

        return loaded;
    }

    public static class NameComp implements Comparator<WContact> {

        @Override
        public int compare(WContact o1, WContact o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.refresh)
        {
            refresh();
        }else if ( item.getItemId()==R.id.search)
        {

        }
        return true;
    }


    public void refresh()
    {
        contactsAdapter.setItems(loadContacts());
        contactsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact list has been updated", Toast.LENGTH_SHORT).show();
    }

    public void startDetailedChat(WContact wContact)
    {
        Intent i = new Intent(ChatPickerActivity.this,OfflineChatDetailedActivity.class);
        i.putExtra("name",wContact.name);
        i.putExtra("number",wContact.number);
        i.putExtra("id",wContact.id);
        startActivity(i);
        this.finish();
    }
}