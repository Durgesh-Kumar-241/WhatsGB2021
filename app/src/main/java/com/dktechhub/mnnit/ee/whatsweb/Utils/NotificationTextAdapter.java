package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.mnnit.ee.whatsweb.R;

import java.util.ArrayList;

public class NotificationTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<WMessage> mList = new ArrayList<>();
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        //View itemView;
        if(viewType==0)
        {
            return new OutGoingViewHolder(inflator.inflate(R.layout.chat_outgoing,parent,false));
        }else return new IncomingViewHolder(inflator.inflate(R.layout.chat_incoming,parent,false));

    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).incoming)
            return 1;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setmList(ArrayList<WMessage> arrayList)
    {
        this.mList.clear();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public static class IncomingViewHolder extends RecyclerView.ViewHolder{

        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public static class OutGoingViewHolder extends RecyclerView.ViewHolder{

        public OutGoingViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
