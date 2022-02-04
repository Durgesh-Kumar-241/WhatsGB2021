package com.dktechhub.gbchat22.statussaver22;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dktechhub.gbchat22.statussaver22.Utils.AsynkLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


public class FragmentStaus extends Fragment {
    int GET_DOCUMENT_TREE_CODE = 222043;
    stsadpr adapter;
    String TAG = "Durgesh";
    private final boolean inSavedMode;
    private DocumentFile documentFile;
    public FragmentStaus(boolean inSavedMode) {
        this.inSavedMode=inSavedMode;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isReadPermission()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }else if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)
        {
            return getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        }else {
            List<UriPermission> all = getActivity().getContentResolver().getPersistedUriPermissions();
            for(UriPermission p : all)
            {
                if(p.getUri().toString().equals("content://com.android.externalstorage.documents/tree/primary%3A"))
                {
                    this.documentFile = DocumentFile.fromTreeUri(getContext(),p.getUri());
                    return true;
                }
            }
         return false;
        }
    }

    public void checkPermissions()
    {
        Log.d(TAG,"Check perm");
        if(!isReadPermission())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Alert");
            builder.setMessage("Grant permission to read External storage to see your recent stories");
            builder.setPositiveButton("Grant", (dialogInterface, i) -> {
                requestPermissions();
                dialogInterface.dismiss();
            });
            builder.setNegativeButton("Not now", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Toast.makeText(getContext(), "Storage permission is required to work this app properly", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            });

            builder.create().show();
        }
    }
    public void requestPermissions()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)
        {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
        }else{
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(i,GET_DOCUMENT_TREE_CODE);
        }
    }

    RecyclerView recyclerView;
    TextView empty;
    SwipeRefreshLayout srl ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                if(status.documentFile!=null)
                    status.documentFile.delete();
                else
                new File(status.source).delete();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                refreshItems();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
    }

    public void refreshItems()
    {   adapter.reset();
        new AsynkLoader(inSavedMode, new AsynkLoader.AsynkLoaderInterface() {
            @Override
            public void onProgress(sts sts) {
                adapter.addStatusItem(sts);
            }

            @Override
            public void onFinished() {
                showEmpty(adapter.getItemCount()==0);
                //Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        },this.documentFile).execute();
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


    public void viewStatus(sts status)
    {
        //Toast.makeText(this, getString(R.string.opening)+status.name, Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        Uri uri;
        if (status.documentFile==null)
            uri= FileProvider.getUriForFile(getContext(),BuildConfig.APPLICATION_ID+".provider", new File(status.source));
        else uri = status.documentFile.getUri();
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
            FileInputStream inputStream;
            FileOutputStream outputStream;
            Uri outUri;
            byte[] buffer = new byte[512];
                try{
                    if(status.documentFile==null)
                    {
                        inputStream = new FileInputStream(status.source);
                        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"GB Version 21.0/Saved Statuses/");
                        f.mkdirs();
                        outputStream = new FileOutputStream(f.getAbsolutePath()+"/"+status.name);
                        outUri= Uri.fromFile(new File(status.source));
                    }else
                    {
                        inputStream = (FileInputStream) getContext().getContentResolver().openInputStream(status.documentFile.getUri());
                        DocumentFile folder = documentFile.findFile("GB Version 21.0");
                        if(folder==null)
                        {
                            folder = documentFile.createDirectory("GB Version 21.0");
                        }
                        DocumentFile folder2 = folder.findFile("Saved Statuses");
                        if(folder2==null)
                        {
                            folder2 = folder.createDirectory("Saved Statuses");
                        }
                        DocumentFile out = folder2.createFile("*/*",status.name);
                        outUri=out.getUri();
                        outputStream = (FileOutputStream) getContext().getContentResolver().openOutputStream(out.getUri());
                    }
                   int read;
                    while ((read=inputStream.read(buffer))>0)
                    {
                        outputStream.write(buffer,0,read);
                        //Log.d("COPY", String.valueOf(read));
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    addToGallery(outUri);
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
        }
        private void addToGallery(Uri uri)
        {
            Intent m = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            m.setData(uri);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_DOCUMENT_TREE_CODE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Log.d(TAG,data.getData().toString());
                getActivity().getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_READ_URI_PERMISSION);
                this.documentFile = DocumentFile.fromTreeUri(getContext(),data.getData());
            }else {
                Toast.makeText(getContext(), "External storage read permission is not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }





    public void share(sts s)
    {

            try{

                    Intent i = new Intent(Intent.ACTION_SEND);
                    //  i.setDataAndType( FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID+".provider", apkPath2),"*/*");
                    i.setType("*/*");
                    Uri uri;
                    if(s.documentFile==null)
                        uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(s.source));
                    else uri = s.documentFile.getUri();
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

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
    }
}