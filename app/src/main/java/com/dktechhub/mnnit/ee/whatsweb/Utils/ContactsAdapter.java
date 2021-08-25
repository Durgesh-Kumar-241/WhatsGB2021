package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.mnnit.ee.whatsweb.R;
import com.dktechhub.mnnit.ee.whatsweb.WContact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.CViewHolder>{
    ArrayList<WContact> mList=new ArrayList<>();
    ContactPickerInterface contactPickerInterface;
    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.layout_contact,parent,false);
        return new CViewHolder(AppView);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.number.setText(mList.get(position).number);
        holder.name.setText(mList.get(position).name);
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactPickerInterface.OnContactClicked(mList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public static class CViewHolder extends RecyclerView.ViewHolder{
        public TextView name,number;
        public LinearLayout main;
        public CViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.cnumber);
            main=itemView.findViewById(R.id.main_lay);

        }
    }


    public void setItems(ArrayList<WContact> wContacts)
    {
        this.mList.clear();
        this.mList.addAll(wContacts);
    }

    public ContactsAdapter(ContactPickerInterface contactPickerInterface)
    {
        this.contactPickerInterface= contactPickerInterface;
    }
    public interface ContactPickerInterface{
        void OnContactClicked(WContact wContact);
    }
}
