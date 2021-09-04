package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dktechhub.mnnit.ee.whatsweb.R;
import com.dktechhub.mnnit.ee.whatsweb.WContact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.CViewHolder>{
    ArrayList<WContact> mList=new ArrayList<>();
    ColorGenerator colorGenerator=ColorGenerator.MATERIAL;
    //TextDrawable.IBuilder builder;
    TextDrawable.IBuilder builder = TextDrawable.builder().round();

    ContactPickerInterface contactPickerInterface;
   // TextDrawable.Builder builder ;
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

        holder.profile.setImageDrawable(builder.build(String.valueOf(mList.get(position).name.charAt(0)), colorGenerator.getRandomColor()));
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
        public ImageView profile;
        public CViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.title_conv);
            number=itemView.findViewById(R.id.summary);
            main=itemView.findViewById(R.id.main_lay);
            profile=itemView.findViewById(R.id.profile_contact);

        }
    }


    public void setItems(ArrayList<WContact> wContacts)
    {
        this.mList.clear();
        this.mList.addAll(wContacts);
        notifyDataSetChanged();
    }

    public ContactsAdapter(ContactPickerInterface contactPickerInterface)
    {
        this.contactPickerInterface= contactPickerInterface;
    }
    public interface ContactPickerInterface{
        void OnContactClicked(WContact wContact);
    }

}
