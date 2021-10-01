package com.dktechhub.mnnit.ee.whatsweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dktechhub.mnnit.ee.whatsweb.Utils.ContactsAdapter;
import com.dktechhub.mnnit.ee.whatsweb.Utils.WContactsManager;

import java.util.ArrayList;

public class ChatPickerActivity extends AppCompatActivity {

    //private NameComp nameComp;
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    private final ArrayList<WContact> all = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_picker);
         recyclerView = findViewById(R.id.contactsList);
         contactsAdapter = new ContactsAdapter(this::startDetailedChat);
         all.addAll(WContactsManager.loadContacts(this));

        contactsAdapter.setItems(all);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //loadContacts();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
       // MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.menu_contacts,menu);
        getMenuInflater().inflate(R.menu.menu_app_contact, menu);
        SearchView searchView2 = (SearchView) menu.findItem(R.id.search_contacts).getActionView();
        searchView2.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchView2.setMaxWidth(Integer.MAX_VALUE);
        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;

    }

    public void filter(String toSearch)
    {
        ArrayList<WContact> contacts = new ArrayList<>();
        for(WContact wContact : all )
        {
            if(wContact.name.toLowerCase().contains(toSearch.toLowerCase())||wContact.number.toLowerCase().contains(toSearch.toLowerCase()))
                contacts.add(wContact);
        }

        if(contacts.isEmpty())
        {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        }else {
            contactsAdapter.setItems(contacts);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_contacts_ref)
        {
            //WContactsManager.refreshContactsDatabase(this);
            refresh();
        }
        return true;
    }


    public void refresh()
    {   all.clear();
        all.addAll(WContactsManager.loadContacts(this));
        contactsAdapter.setItems(all);
       // contactsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact list has been updated", Toast.LENGTH_SHORT).show();
    }

    public void startDetailedChat(WContact wContact)
    {
        Intent i = new Intent(ChatPickerActivity.this, OFCDA.class);
        i.putExtra("name",wContact.name);
        i.putExtra("number",wContact.number);
        i.putExtra("id",wContact.id);
        startActivity(i);
        this.finish();
    }


}