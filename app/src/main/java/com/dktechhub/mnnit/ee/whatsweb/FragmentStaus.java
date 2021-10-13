package com.dktechhub.mnnit.ee.whatsweb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FragmentStaus extends Fragment {

    stsadpr adapter;
    private final boolean inSavedMode;
    public FragmentStaus(boolean inSavedMode) {

        this.inSavedMode=inSavedMode;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},WRITE_EXTERNAL_STORAGE_CODE);
        }
    }
    RecyclerView recyclerView;
    TextView empty;
    SwipeRefreshLayout srl ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_staus, container, false);
        recyclerView=root.findViewById(R.id.recycler);
        empty=root.findViewById(R.id.empty);
        adapter=new stsadpr(new stsadpr.StatusItemAdapterListner() {
            @Override
            public void onSaveButtonClicked(sts status) {
                new Szr(status).execute();
            }

            @Override
            public void onShareButtonClicked(sts status) {
                share(status);
            }

            @Override
            public void onIconClicked(sts status) {
                viewStatus(status);
            }

            @Override
            public void onDeleteButtonClicked(sts status) {
                new Dlx(status).execute();
            }
        },getActivity(),inSavedMode);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        showEmpty(false);
        srl=root.findViewById(R.id.refresh);
        srl.setOnRefreshListener(this::refreshItems);
        //refreshItems();
        return root;
    }

    public void refreshItems()
    {   adapter.reset();
       // showEmpty(true);
        //adapter.notifyDataSetChanged();
        new Ldx(new OnLoadCompleteListener() {
            @Override
            public void onLoaded(sts status) {
                adapter.addStatusItem(status);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadCompleted() {
                showEmpty(adapter.getItemCount()==0);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        },inSavedMode).execute();
    }

    public void showEmpty(boolean show)
    {
        if(show)
        {
            recyclerView.setVisibility(View.GONE);
            //buttonToggleGroup.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            //buttonToggleGroup.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }


    public static boolean isImage(String src)
    {
        return src.contains(".png") || src.contains(".jpg");
    }
    public static boolean isVideo(String src)
    {
        return src.contains(".mp4");
    }
    public void viewStatus(sts status)
    {
        //Toast.makeText(this, getString(R.string.opening)+status.name, Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        Uri uri = FileProvider.getUriForFile(getContext(),BuildConfig.APPLICATION_ID+".provider", new File(status.source));
        i.setAction(Intent.ACTION_VIEW);

        i.setDataAndType(uri,status.mime);
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }


    public class Szr extends AsyncTask<Void,Void,Void> {
        sts status;

        public Szr(sts status)
        {
            this.status=status;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GB What s App/Saved Statuses/");
            f.mkdirs();
            byte[] buffer = new byte[512];
                try{
                    FileInputStream is = new FileInputStream(status.source);
                    FileOutputStream os = new FileOutputStream(f.getAbsolutePath()+"/"+status.name);
                    int read;
                    while ((read=is.read(buffer))>0)
                    {
                        os.write(buffer,0,read);
                        //Log.d("COPY", String.valueOf(read));
                    }
                    os.flush();
                    os.close();
                    is.close();
                    addToGallery(f);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getContext(), "Saved to \"/GB What s App/Saved Statuses/\"", Toast.LENGTH_SHORT).show();
            //addToGallery();
           // Toast toast = new Toast(getContext());
           // toast.setText("Saved to \"/GB What s App/Saved Statuses/\"");

        }
        private void addToGallery(File f)
        {
            Intent m = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            m.setData(contentUri);
            getActivity().sendBroadcast(m);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE&&grantResults.length>0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission is required to work this app properly", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 257;


    public static class Ldx extends AsyncTask<Void, sts,Void> {
        OnLoadCompleteListener onLoadCompleteListener;
        boolean loadSaved;

        Ldx(OnLoadCompleteListener onLoadCompleteListener, boolean loadSaved){
            this.onLoadCompleteListener=onLoadCompleteListener;
            this.loadSaved=loadSaved;
        }

        public void loadStatusFromDir(File file)
        {   //Log.d("Loader","loading now from"+ file.getAbsolutePath());
            try{
                if(!file.isDirectory())
                    return;
                File[] list= file.listFiles();
                if(list!=null)
                {
                    for (File f1 : list) {
                        if (isImage(f1.getAbsolutePath())) {
                            publishProgress(new sts(f1.getAbsolutePath(),f1.getName(),"image/*"));

                        }else if(isVideo(f1.getAbsolutePath())){

                            publishProgress(new sts(f1.getAbsolutePath(),f1.getName(),"video/*"));


                        }
                        //Log.d("Loader","loading now "+ f1.getAbsolutePath());
                    }
                }
            }catch (Exception ignored)
            {

            }
        }


        @Override
        protected void onProgressUpdate(sts... values) {
            super.onProgressUpdate(values);
            onLoadCompleteListener.onLoaded(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Log.d("Loader","Executing loader now with "+this.loadSaved);
            if(loadSaved)
            {
                loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GB What s App/Saved Statuses/"));
            }else {
                loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/.Statuses/"));
                loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/.Statuses/"));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            onLoadCompleteListener.onLoadCompleted();
        }
    }
    public interface OnLoadCompleteListener{
        void onLoaded(sts status);
        void onLoadCompleted();
    }



    public void share(sts s)
    {

            try{

                    Intent i = new Intent(Intent.ACTION_SEND);
                    //  i.setDataAndType( FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider", apkPath2),"*/*");
                    i.setType("*/*");
                    Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(s.source));
                    Log.d("Invite", uri.toString());
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(i, getString(R.string.chooseaway)));

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }

    }

    class Dlx extends AsyncTask<Void,Void,Void>
    {   sts status;
        public Dlx(sts status)
        {
            this.status=status;
        }

     

        @Override
        protected Void doInBackground(Void... voids) {
             try {
                    new File(status.source).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
             return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            refreshItems();
            //refresh();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(getContext(), "resumed", Toast.LENGTH_SHORT).show();
        refreshItems();
    }
}