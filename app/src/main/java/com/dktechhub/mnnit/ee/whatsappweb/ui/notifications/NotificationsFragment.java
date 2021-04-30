package com.dktechhub.mnnit.ee.whatsappweb.ui.notifications;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dktechhub.mnnit.ee.whatsappweb.R;
import com.dktechhub.mnnit.ee.whatsappweb.Status;
import com.dktechhub.mnnit.ee.whatsappweb.StatusItemAdapter;
import com.dktechhub.mnnit.ee.whatsappweb.ui.dashboard.StatusFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsFragment extends Fragment {
    RecyclerView photos;
    ArrayList<Status> selected=new ArrayList<>();
    FloatingActionButton fab;
    StatusItemAdapter photoAdapter;
    CheckBox selectAll;
    SwipeRefreshLayout swipeRefreshLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_saved_status, container, false);
        photos=root.findViewById(R.id.images);
        //videos=root.findViewById(R.id.videos);
        fab=root.findViewById(R.id.floatingActionButton);
        swipeRefreshLayout=root.findViewById(R.id.swipeRefreshLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save();
                new deleter(selected).execute();
            }
        });
        selectAll=root.findViewById(R.id.select_all);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selectAll();
                else deselectAll();
            }
        });
        photoAdapter=new StatusItemAdapter(new StatusItemAdapter.StatusItemAdapterListner() {
            @Override
            public void onCheckBoxClicked(Status status) {
                NotificationsFragment.this.onCheckBoxClicked(status);
            }

            @Override
            public void onIconClicked(Status status) {
                NotificationsFragment.this.onCheckBoxClicked(status);
            }
        });
        //StatusItemAdapter videosAdapter=new StatusItemAdapter();


        photos.setLayoutManager(new GridLayoutManager(getContext(),4));
        photos.setAdapter(photoAdapter);

        new Loader(new OnLoadCompleteListener() {
            @Override
            public void onLoaded(Status status) {
                photoAdapter.addStatusItem(status);
                photoAdapter.notifyDataSetChanged();
            }
        }).execute();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }


    public void refresh()
    {
        photoAdapter.getmList().clear();
        new Loader(new OnLoadCompleteListener() {
            @Override
            public void onLoaded(Status status) {
                photoAdapter.addStatusItem(status);
                photoAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public void selectAll()
    {
        for(Status t:photoAdapter.getmList())
        {
            t.isSelected=true;
            photoAdapter.notifyDataSetChanged();
            selected.add(t);
        }
    }

    public void deselectAll()
    {
        for(Status t:photoAdapter.getmList())
        {
            t.isSelected=false;
            photoAdapter.notifyDataSetChanged();
            selected.remove(t);
        }
    }

    public class Loader extends AsyncTask<Void, Status,Void> {
        ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> statuses;
        OnLoadCompleteListener onLoadCompleteListener;

        Loader(OnLoadCompleteListener onLoadCompleteListener){
            this.onLoadCompleteListener=onLoadCompleteListener;
        }
        public void getStatuses()
        {
            try{
                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Web/Saved Statuses/");

                if(!f.isDirectory())
                {   Log.d("File",f.toString());
                    return;
                }
                File[] all = f.listFiles();
                Log.d("File List", Arrays.toString(all));
                if(all!=null) {
                    for (File f1 : all) {
                        Bitmap thumb;
                        if (isImage(f1.getAbsolutePath())) {
                            thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f1.getAbsolutePath()), 512, 384);
                            //(new com.dktechhub.mnnit.ee.whatsappweb.Status(f.getAbsolutePath(), thumb));
                            // thumb = ThumbnailUtils.createImageThumbnail(f1.getAbsolutePath(), MediaStore.Audio.Thumbnails.MINI_KIND);
                        }else {
                            thumb = ThumbnailUtils.createVideoThumbnail(f1.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                            //publishProgress(new com.dktechhub.mnnit.ee.whatsappweb.Status(f.getAbsolutePath(), thumb));

                        }if(thumb!=null)
                        {
                            publishProgress(new com.dktechhub.mnnit.ee.whatsappweb.Status(f1.getAbsolutePath(), thumb,f1.getName()));
                        }
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        public boolean isImage(String src)
        {
            if(src.contains(".png")||src.contains(".jpg")){
                return true;
            }return false;
        }
        @Override
        protected void onProgressUpdate(com.dktechhub.mnnit.ee.whatsappweb.Status... values) {
            super.onProgressUpdate(values);
            onLoadCompleteListener.onLoaded(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getStatuses();
            return null;
        }




    }
    public interface OnLoadCompleteListener{
        void onLoaded(Status status);
    }

    public void onCheckBoxClicked(Status status)
    {
        status.isSelected=!status.isSelected;
        if(status.isSelected)
        {
            selected.add(status);
            //Toast.makeText(getContext(), status.name+"added", Toast.LENGTH_SHORT).show();
        }else selected.remove(status);
        photoAdapter.notifyDataSetChanged();
    }
    public class deleter extends AsyncTask<Void,Void,Void>
    {   ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> selected;
        ProgressDialog progressDialog;
        public deleter(ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> selected)
        {
            this.selected=selected;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Removing..please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (com.dktechhub.mnnit.ee.whatsappweb.Status status : selected) {
                try {
                    new File(status.source).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
            photoAdapter.getmList().clear();
            new Loader(new OnLoadCompleteListener() {
                @Override
                public void onLoaded(com.dktechhub.mnnit.ee.whatsappweb.Status status) {
                    photoAdapter.addStatusItem(status);
                    photoAdapter.notifyDataSetChanged();
                }
            }).execute();
        }
    }
}
