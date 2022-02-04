package com.dktechhub.gbchat22.statussaver22;


import androidx.documentfile.provider.DocumentFile;

public class sts {
    public String source;
    public String mime="*/*";
    public String name;
    public DocumentFile documentFile;
    public sts(String source, String name, String mime)
    {
        this.source=source;
        this.name=name;
        this.mime=mime;
    }
    public sts(DocumentFile documentFile)
    {
        this.documentFile = documentFile;
        this.name = documentFile.getName();
        this.mime = documentFile.getType();
    }
}
