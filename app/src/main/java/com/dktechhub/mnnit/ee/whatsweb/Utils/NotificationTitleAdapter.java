package com.dktechhub.mnnit.ee.whatsweb.Utils;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
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
import com.bumptech.glide.Glide;
import com.dktechhub.mnnit.ee.whatsweb.R;
import java.io.File;
import java.util.ArrayList;

public class NotificationTitleAdapter extends RecyclerView.Adapter<NotificationTitleAdapter.NotiViewHolder>{
    ArrayList<NotificationTitle> mList=new ArrayList<>();
    ColorGenerator colorGenerator=ColorGenerator.MATERIAL;
    //TextDrawable.IBuilder builder;
    TextDrawable.IBuilder builder = TextDrawable.builder().round();
    OnItemClickListener onItemClickListener;
    Context context;
    File destDir = new File(Environment.getExternalStorageDirectory()+File.separator+"GB What s App"+File.separator+".caches");

    // TextDrawable.Builder builder ;
    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);
        View AppView = inflator.inflate(R.layout.layout_item_off_chat_overview,parent,false);
        return new NotiViewHolder(AppView);
    }



    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        holder.name.setText(Html.fromHtml(mList.get(position).title));
        holder.num_unread.setText(mList.get(position).number);
        holder.dateTime.setText(mList.get(position).date);
        holder.summary.setText(Html.fromHtml(mList.get(position).summary));
        //holder.name.setText(mList.get(position).title);
        try {
            if (mList.get(position).photo == null)
                holder.profile.setImageDrawable(builder.build(String.valueOf(mList.get(position).title.charAt(0)), colorGenerator.getRandomColor()));
            else
                Glide.with(context).load(destDir.getAbsolutePath()+File.separator+mList.get(position).photo).centerCrop().placeholder(R.drawable.ic_album).into(holder.profile);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.main.setOnClickListener(v -> onItemClickListener.onItemClicked(mList.get(position)));

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
