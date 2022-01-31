package com.dktechhub.gbchat22.whatsweb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class stsadpr extends RecyclerView.Adapter<stsadpr.ViewHolder> {

    boolean inSavedItemsMode = false;
    int layoutId;
    Activity activity;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(layoutId,parent,false);
        return new ViewHolder(AppView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sts status=mList.get(position);

        Glide.with(this.activity).load(status.source).centerCrop().placeholder(R.drawable.ic_album).into(holder.iconView);
        holder.iconView.setOnClickListener(v -> listner.onIconClicked(status));
        holder.saveButton.setOnClickListener(v -> {
            if(inSavedItemsMode)
                listner.onDeleteButtonClicked(status);
            else
            listner.onSaveButtonClicked(status);
        });
        holder.shareButton.setOnClickListener(v -> listner.onShareButtonClicked(status));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{


        public ImageView iconView;
        public ImageButton shareButton,saveButton;

        public ViewHolder(View itemView)
        {
            super(itemView);

            shareButton= itemView.findViewById(R.id.share_button);
            saveButton= itemView.findViewById(R.id.saveButtonm);

            iconView = itemView.findViewById(R.id.imageViewStatusItem);
            //linearLayout=itemView.findViewById(R.id.main_item_app);
        }
    }

    public void addStatusItem(sts item)
    {
        this.mList.add(item);
    }
    private final List<sts> mList = new ArrayList<>();
    StatusItemAdapterListner listner;


    public stsadpr(StatusItemAdapterListner listner, Activity activity, boolean inSavedItemsMode)
    {   this.listner=listner;
        this.activity=activity;
        this.inSavedItemsMode=inSavedItemsMode;
        if(inSavedItemsMode)
        {
            layoutId=R.layout.item_status_saved;
        }else layoutId=R.layout.item_status;

    }

    public interface StatusItemAdapterListner{
        void onSaveButtonClicked(sts status);
        void onShareButtonClicked(sts status);
        void onIconClicked(sts status);
        void onDeleteButtonClicked(sts status);
    }

    public void reset()
    {
        this.mList.clear();
    }
}
