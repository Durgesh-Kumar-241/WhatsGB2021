package com.dktechhub.gbchat22.statussaver22.Utils;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.dktechhub.gbchat22.statussaver22.sts;

import java.io.File;

public class AsynkLoader extends AsyncTask<Void, sts,Void> {
    private static final String TAG = "Durgesh";
    private final DocumentFile documentFile;
    private final AsynkLoaderInterface asynkLoaderInterface;
    private final boolean loadSaved;
    public AsynkLoader(boolean loadSaved,AsynkLoaderInterface asynkLoaderInterface,DocumentFile documentFile)
    {
        this.documentFile = documentFile;
        this.asynkLoaderInterface = asynkLoaderInterface;
        this.loadSaved=loadSaved;
    }
    public void run()
    {
       if(!loadSaved) {
            if(documentFile==null)
            {
                loadFromDirectFile();
            }else {
                loadFromDocumentFile();
                loadFromDocumentFile2();
            }
        }else {
           if(documentFile==null)
           loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GB Version 21.0/Saved Statuses"));
           else loadSavedFromDocumnetFile();
       }

       if(loadSaved)
       {
           if(documentFile!=null)
               loadSavedFromDocumnetFile();
           else  loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GB Version 21.0/Saved Statuses"));

       }else {
           if(documentFile!=null)
           {
               loadFromDocumentFile2();
               loadFromDocumentFile();
           }else{
               loadFromDirectFile();
           }
       }
    }

    public void loadFromDirectFile()
    {
        loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/.Statuses/"));
        loadStatusFromDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/.Statuses/"));
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
                    if (f1.getAbsolutePath().endsWith(".png") || f1.getAbsolutePath().endsWith(".jpg")) {
                        publishProgress(new sts(f1.getAbsolutePath(),f1.getName(),"image/*"));

                    }else if(f1.getAbsolutePath().endsWith(".mp4")){

                        publishProgress(new sts(f1.getAbsolutePath(),f1.getName(),"video/*"));


                    }
                    //Log.d("Loader","loading now "+ f1.getAbsolutePath());
                }
            }
        }catch (Exception ignored)
        {

        }
    }

    public void loadFromDocumentFile()
    {   try{
        DocumentFile dir = documentFile.findFile("WhatsApp").findFile("Media").findFile(".Statuses");
        if(dir.exists()&&dir.canRead())
        {
            for(DocumentFile documentFile1 : dir.listFiles())
            {   if(documentFile1.getName().equals(".nomedia"))
                continue;
                Log.d(TAG,documentFile1.getName()+documentFile1.getType()+documentFile1.isFile());
                publishProgress(new sts(documentFile1));
            }
        }
    }catch (Exception e){
        e.printStackTrace();
    }

    }

    public void loadFromDocumentFile2()
    {   try{
        DocumentFile dir = documentFile.findFile("WhatsApp Business").findFile("Media").findFile(".Statuses");
        if(dir.exists()&&dir.canRead())
        {
            for(DocumentFile documentFile1 : dir.listFiles())
            {   if(documentFile1.getName().equals(".nomedia"))
                continue;
                Log.d(TAG,documentFile1.getName()+documentFile1.getType()+documentFile1.isFile());
                publishProgress(new sts(documentFile1));
            }
        }
    }catch (Exception e){
        e.printStackTrace();
    }

    }

    public void loadSavedFromDocumnetFile()
    {
        try{
            DocumentFile dir = documentFile.findFile("GB Version 21.0").findFile("Saved Statuses");
            if(dir.exists()&&dir.canRead())
            {
                for(DocumentFile documentFile1 : dir.listFiles())
                {
                    Log.d(TAG,documentFile1.getName()+documentFile1.getType()+documentFile1.isFile()+"Saved File");
                    publishProgress(new sts(documentFile1));
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,"Could not loaded"+ e);
        }

    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{
            run();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(sts... values) {
        super.onProgressUpdate(values);
        asynkLoaderInterface.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        asynkLoaderInterface.onFinished();
    }

    public interface AsynkLoaderInterface{
        void onProgress(sts sts);
        void onFinished();
    }
}
