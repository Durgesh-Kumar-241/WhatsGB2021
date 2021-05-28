package com.dktechhub.mnnit.ee.whatsweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ConactsAdapter extends RecyclerView.Adapter<ConactsAdapter.ChatViewHolder> {
    ArrayList<Contact> all = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    //View.OnClickListener onClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.item_chat,parent,false);
        return new ChatViewHolder(AppView);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Contact contact=all.get(position);
        holder.nameText.setText(contact.name);
        if(contact.profile_uri!=null)
            Glide.with(context).load(contact.profile_uri).centerCrop().into(holder.imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onContactCicked(contact);
            }
        });
    }

   

    @Override
    public int getItemCount() {
        return all.size();
    }

    public ConactsAdapter(Context context)
    {
this.context=context;
    }

    public void addContact(Contact contact)
    {
        this.all.add(contact);
    }
    public static class ChatViewHolder extends  RecyclerView.ViewHolder{
        
        public ImageView imageView;
        public ConstraintLayout constraintLayout;
        public TextView nameText;
        public ChatViewHolder(View itemView)
        {
            super(itemView);
            nameText=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.imageView);
            constraintLayout=itemView.findViewById(R.id.body);

            //linearLayout=itemView.findViewById(R.id.main_item_app);
        }
    }


    public interface  OnItemClickListener{
        void onContactCicked(Contact contact);
    }
}
