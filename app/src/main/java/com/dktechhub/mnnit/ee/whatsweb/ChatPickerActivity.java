package com.dktechhub.mnnit.ee.whatsweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsweb.Utils.ContactsAdapter;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WContactsManager;
import com.dktechhub.mnnit.ee.whatsweb.Utils.wContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatPickerActivity extends AppCompatActivity {

    //private NameComp nameComp;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    private SearchView searchView;

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
                String mimetype = query.getString(query.getColumnIndex("mimetype"));
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts,menu);
        getMenuInflater().inflate(R.menu.menu_app_contact, menu);
        SearchView searchView2 = (SearchView) menu.findItem(R.id.search_contacts).getActionView();
        this.searchView = searchView2;
        searchView2.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        this.searchView.setMaxWidth(Integer.MAX_VALUE);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_contacts_ref)
        {
            //WContactsManager.refreshContactsDatabase(this);
        }else if ( item.getItemId()==R.id.search_contacts)
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