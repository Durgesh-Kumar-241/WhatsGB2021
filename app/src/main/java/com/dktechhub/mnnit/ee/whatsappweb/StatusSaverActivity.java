package com.dktechhub.mnnit.ee.whatsappweb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StatusSaverActivity extends AppCompatActivity {


    RecyclerView photos;
    ArrayList<Status> selected=new ArrayList<>();
    FloatingActionButton saveButton,shareButton;
    StatusItemAdapter photoAdapter;
    CheckedTextView selectAll;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView noItemsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.fragment_status);

        photos=findViewById(R.id.recyclerview);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
        //videos=root.findViewById(R.id.videos);
        saveButton=findViewById(R.id.saveButton);
        shareButton=findViewById(R.id.shareButton);
        noItemsTextView=findViewById(R.id.noItemsTextView);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        selectAll=findViewById(R.id.selectAllTextVIew);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll.setChecked(!selectAll.isChecked());
                if(selectAll.isChecked()){
                    photoAdapter.inSelectionMode=true;

                    selectAll.setCheckMarkDrawable(R.drawable.w0);
                selectAll();}
                else {
                    photoAdapter.inSelectionMode=false;
                    selectAll.setCheckMarkDrawable(R.drawable.w1);
                    deselectAll();
                }
                photoAdapter.listner.onSelectionModeChanged(photoAdapter.inSelectionMode);
            }
        });
        photoAdapter=new StatusItemAdapter(new StatusItemAdapter.StatusItemAdapterListner() {
            @Override
            public void onCheckBoxClicked(Status status) {
                StatusSaverActivity.this.onCheckBoxClicked(status);
            }

            @Override
            public void onIconClicked(Status status) {
               StatusSaverActivity.this.onIconClicked(status);
            }

            @Override
            public void onSelectionModeChanged(boolean selectionMode) {
                if(selectionMode)
                {
                    saveButton.setVisibility(View.VISIBLE);
                    shareButton.setVisibility(View.VISIBLE);
                }else{
                    saveButton.setVisibility(View.GONE);
                    shareButton.setVisibility(View.GONE);
                }
            }
        });
        //StatusItemAdapter videosAdapter=new StatusItemAdapter();


        photos.setLayoutManager(new GridLayoutManager(this,2));
        photos.setAdapter(photoAdapter);
        //videos.setLayoutManager(new GridLayoutManager(getContext(),4));
        //videos.setAdapter(videosAdapter);
        new Loader(new OnLoadCompleteListener() {
            @Override
            public void onLoaded(Status status) {
                photoAdapter.addStatusItem(status);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadCompleted() {
                if(photoAdapter.getItemCount()==0)
                {
                    noItemsTextView.setVisibility(View.VISIBLE);
                    photos.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    selectAll.setVisibility(View.GONE);
                }else {
                    noItemsTextView.setVisibility(View.GONE);
                    photos.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    selectAll.setVisibility(View.VISIBLE);
                }
            }

        }).execute();


    }

    public static class Loader extends AsyncTask<Void,com.dktechhub.mnnit.ee.whatsappweb.Status,Void> {
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
                    for (File f1 : all) {Bitmap thumb;String mime;
                        if (isImage(f1.getAbsolutePath())) {
                            thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f1.getAbsolutePath()), 512, 384);
                            mime="image/*";

                            //(new com.dktechhub.mnnit.ee.whatsappweb.Status(f.getAbsolutePath(), thumb));
                           // thumb = ThumbnailUtils.createImageThumbnail(f1.getAbsolutePath(), MediaStore.Audio.Thumbnails.MINI_KIND);
                        }else {
                            //thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f1.getAbsolutePath()), 512, 384);
                            thumb = ThumbnailUtils.createVideoThumbnail(f1.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

                            //publishProgress(new com.dktechhub.mnnit.ee.whatsappweb.Status(f.getAbsolutePath(), thumb));
                            mime="video/*";

                    }if(thumb!=null)
                        {
                            publishProgress(new com.dktechhub.mnnit.ee.whatsappweb.Status(f1.getAbsolutePath(), thumb,f1.getName(),mime));
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

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            onLoadCompleteListener.onLoadCompleted();
        }
    }
    public interface OnLoadCompleteListener{
        void onLoaded(Status status);
        void onLoadCompleted();
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

    public void onIconClicked(Status status)
    {
        Toast.makeText(this, "Opening..."+status.name, Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider", new File(status.source));
        i.setAction(Intent.ACTION_VIEW);

        i.setDataAndType(uri,status.mime);
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }

    public void save()
    {   if(selected.size()!=0)
        new Saver(this.selected).execute();
    }

    public void share()
    {
        if(selected.size()!=0)
        {
            try{
                if(selected.size()==1) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    //  i.setDataAndType( FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider", apkPath2),"*/*");
                    i.setType("*/*");
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(selected.get(0).source));
                    Log.d("Invite", uri.toString());
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(i, "Choose a way:"));
                }else{
                    Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    //  i.setDataAndType( FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider", apkPath2),"*/*");
                    i.setType("*/*");
                    ArrayList<Uri> filesToSend=new ArrayList<>();
                    for(Status status:selected)
                    {
                        filesToSend.add(FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(status.source)));
                    }
                    //Log.d("Invite", uri.toString());
                    //i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.putParcelableArrayListExtra(Intent.EXTRA_STREAM,filesToSend);;
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(i, "Choose a way:"));
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
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
            progressDialog=new ProgressDialog(StatusSaverActivity.this);
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
            deselectAll();
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
        selectAll.setChecked(true);
    }

    public void deselectAll()
    {
        for(Status t:photoAdapter.getmList())
        {
            t.isSelected=false;
            photoAdapter.notifyDataSetChanged();
            selected.remove(t);
        }
        selectAll.setChecked(false);
    }
    public void applyTheme()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_theme = sharedPreferences.getBoolean("dark_theme",false);
        if(dark_theme)
        {
            //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else setTheme(R.style.Theme_AppCompat_Light);
    }
}