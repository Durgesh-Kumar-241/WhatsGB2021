package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;

public class NotificationTitleAdapter extends RecyclerView.Adapter<NotificationTitleAdapter.NotiViewHolder>{
    ArrayList<NotificationTitle> mList=new ArrayList<>();
    ColorGenerator colorGenerator=ColorGenerator.MATERIAL;
    //TextDrawable.IBuilder builder;
    TextDrawable.IBuilder builder = TextDrawable.builder().round();
    OnItemClickListener onItemClickListener;

    // TextDrawable.Builder builder ;
    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.layout_item_off_chat_overview,parent,false);
        return new NotiViewHolder(AppView);
    }



    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        //holder.number.setText(mList.get(position).number);
        //holder.name.setText(mList.get(position).name);

        //holder.profile.setImageDrawable(builder.build(String.valueOf(mList.get(position).name.charAt(0)), colorGenerator.getRandomColor()));
        holder.name.setText(mList.get(position).title);
        holder.num_unread.setText(mList.get(position).number);
        holder.dateTime.setText(mList.get(position).date);
        holder.summary.setText(mList.get(position).summary);
        //holder.name.setText(mList.get(position).title);
        try {
            if (mList.get(position).photo == null)
                holder.profile.setImageDrawable(builder.build(String.valueOf(mList.get(position).title.charAt(0)), colorGenerator.getRandomColor()));
            else
                holder.profile.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(mList.get(position).photo, 0, mList.get(position).photo.length), holder.profile.getWidth(), holder.profile.getHeight(), false));
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(mList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public static class NotiViewHolder extends RecyclerView.ViewHolder{
        public TextView name,number,summary;
        public LinearLayout main;
        public ImageView profile;
        public TextView num_unread ;
        public TextView dateTime;
        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.title_conv);
            summary=itemView.findViewById(R.id.summary);
            num_unread = itemView.findViewById(R.id.num_unread);
            main=itemView.findViewById(R.id.main_lay);
            dateTime = itemView.findViewById(R.id.datetime);
            profile=itemView.findViewById(R.id.profile_contact);

        }
    }

    public void setmList(ArrayList<NotificationTitle> items)
    {
        this.mList.clear();
        mList.addAll(items);
        notifyDataSetChanged();

    }

    public NotificationTitleAdapter(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(NotificationTitle notificationTitle);
    }


}
