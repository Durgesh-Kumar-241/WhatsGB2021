package com.dktechhub.gbchat22.whatsweb;

import android.graphics.Bitmap;

public class sts {
    public String source;
Bitmap thumbnail;
public String mime="*/*";
public String name;
public sts(String source, Bitmap thumbnail, String name){
    this.thumbnail=thumbnail;
    this.source=source;
    this.name=name;
}
public sts(String source, Bitmap thumbnail, String name, String mime)
{
    this(source,thumbnail,name);
    this.mime=mime;
}
    public sts(String source, String name, String mime)
    {
        this.source=source;
        this.name=name;
        this.mime=mime;
    }
}
