package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class StatusItemAdapter extends RecyclerView.Adapter<StatusItemAdapter.ViewHolder> {

    boolean inSelectionMode = false;
    Activity activity;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.item_status,parent,false);
        ViewHolder viewHolder = new ViewHolder(AppView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(this.activity).load(mList.get(position).source).centerCrop().placeholder(R.drawable.ic_album).into(holder.iconView);
        holder.checkBox.setChecked(mList.get(position).isSelected);
        holder.iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inSelectionMode)
                    listner.onCheckBoxClicked(mList.get(position));
                else listner.onIconClicked(mList.get(position));
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onCheckBoxClicked(mList.get(position));
            }
        });
        holder.iconView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inSelectionMode=!inSelectionMode;
                listner.onSelectionModeChanged(inSelectionMode);
                notifyDataSetChanged();
                return false;
            }
        });
        if(!inSelectionMode)
        holder.checkBox.setVisibility(View.GONE);
        else holder.checkBox.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public CheckBox checkBox;
        public ImageView iconView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.checkBoxStatusItem);
            iconView =(ImageView) itemView.findViewById(R.id.imageViewStatusItem);
            //linearLayout=itemView.findViewById(R.id.main_item_app);
        }
    }
    public void addStatusItem(ArrayList<Status> items)
    {
        this.mList.addAll(items);
    }
    public void addStatusItem(Status item)
    {
        this.mList.add(item);
    }
    private List<Status> mList = new ArrayList<>();
    StatusItemAdapterListner listner;
    public StatusItemAdapter(StatusItemAdapterListner listner, Activity activity)
    {this.listner=listner;
this.activity=activity;
    }
    public List<Status> getmList()
    {
        return  this.mList;
    }
    public interface StatusItemAdapterListner{
        void onCheckBoxClicked(Status status);
        void onIconClicked(Status status);
       void onSelectionModeChanged(boolean selectionMode);
    }


}
