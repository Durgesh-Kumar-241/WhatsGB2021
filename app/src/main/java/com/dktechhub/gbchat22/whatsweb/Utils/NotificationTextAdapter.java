package com.dktechhub.gbchat22.whatsweb.Utils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.gbchat22.whatsweb.R;

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
       if(mList.get(position).incoming)
       {
           ((IncomingViewHolder) holder)
                   .mes.setText(Html.fromHtml(mList.get(position).text));
           ((IncomingViewHolder) holder)
                   .time.setText(mList.get(position).date);
       }else {
           ((OutGoingViewHolder) holder)
                   .mess.setText(Html.fromHtml(mList.get(position).text));
           ((OutGoingViewHolder) holder)
                   .time.setText(mList.get(position).date);
       }
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

    public void addMessage(WMessage wMessage)
    {
        mList.add(wMessage);
        notifyDataSetChanged();
    }
    public static class IncomingViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        com.vanniktech.emoji.EmojiTextView mes;
        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            mes=itemView.findViewById(R.id.mesi);
            time = itemView.findViewById(R.id.timi);
        }
    }

    public static class OutGoingViewHolder extends RecyclerView.ViewHolder{
        TextView mess,time;
        public OutGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            mess=itemView.findViewById(R.id.meso);
            time = itemView.findViewById(R.id.timo);

        }
    }

}
