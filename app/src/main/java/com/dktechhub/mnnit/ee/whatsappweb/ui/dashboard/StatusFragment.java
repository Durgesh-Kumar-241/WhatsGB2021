package com.dktechhub.mnnit.ee.whatsappweb.ui.dashboard;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
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

import com.dktechhub.mnnit.ee.whatsappweb.R;
import com.dktechhub.mnnit.ee.whatsappweb.Status;
import com.dktechhub.mnnit.ee.whatsappweb.StatusItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusFragment extends Fragment {


    RecyclerView photos;
    ArrayList<Status> selected=new ArrayList<>();
    FloatingActionButton fab;
    StatusItemAdapter photoAdapter;
    CheckBox selectAll;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_status, container, false);
        photos=root.findViewById(R.id.images);
        //videos=root.findViewById(R.id.videos);
        fab=root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
                StatusFragment.this.onCheckBoxClicked(status);
            }

            @Override
            public void onIconClicked(Status status) {
                StatusFragment.this.onCheckBoxClicked(status);
            }
        });
        //StatusItemAdapter videosAdapter=new StatusItemAdapter();


        photos.setLayoutManager(new GridLayoutManager(getContext(),4));
        photos.setAdapter(photoAdapter);
        //videos.setLayoutManager(new GridLayoutManager(getContext(),4));
        //videos.setAdapter(videosAdapter);
        new Loader(new OnLoadCompleteListener() {
            @Override
            public void onLoaded(Status status) {
                photoAdapter.addStatusItem(status);
                photoAdapter.notifyDataSetChanged();
            }
        }).execute();
        return root;
    }
    public class Loader extends AsyncTask<Void,com.dktechhub.mnnit.ee.whatsappweb.Status,Void> {
        ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> statuses;
        OnLoadCompleteListener onLoadCompleteListener;

        Loader(OnLoadCompleteListener onLoadCompleteListener){
            this.onLoadCompleteListener=onLoadCompleteListener;
        }
        public void getStatuses()
        {
            try{
                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/.Statuses/");

                if(!f.isDirectory())
                {   Log.d("File",f.toString());
                    return;
                }
                File[] all = f.listFiles();
                Log.d("File List", Arrays.toString(all));
                if(all!=null) {
                    for (File f1 : all) {Bitmap thumb;
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
           // Toast.makeText(getContext(), status.name+"added", Toast.LENGTH_SHORT).show();
        }else selected.remove(status);
        photoAdapter.notifyDataSetChanged();
    }

    public void save()
    {
        new Saver(this.selected).execute();
    }
    public class Saver extends AsyncTask<Void,Void,Void>{
        ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> selected;
        ProgressDialog progressDialog;
        public Saver(ArrayList<com.dktechhub.mnnit.ee.whatsappweb.Status> selected)
        {
            this.selected=selected;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Saving..please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Web/Saved Statuses/");
            f.mkdirs();
            byte[] buffer = new byte[512];

            for(com.dktechhub.mnnit.ee.whatsappweb.Status temp:selected)
            {
                try{
                    FileInputStream is = new FileInputStream(temp.source);
                    //File inf = new File(temp.source);
                    //File ouf=new File(f.getAbsolutePath()+"/"+temp.name);
                    FileOutputStream os = new FileOutputStream(f.getAbsolutePath()+"/"+temp.name);
                    //Files.copy(inf.toPath(),ouf.toPath())

                    //read=0;
                       int read;
                    while ((read=is.read(buffer))>0)
                    {
                        os.write(buffer,0,read);
                        Log.d("COPY", String.valueOf(read));
                    }

                    os.flush();

                    os.close();
                    is.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }
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
}