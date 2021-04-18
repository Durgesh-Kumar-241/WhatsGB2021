package com.dktechhub.mnnit.ee.whatsappweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatusItemAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.item_status,parent,false);
        ViewHolder viewHolder = new ViewHolder(AppView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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
    private List<Status> mList = new ArrayList<>();
}
