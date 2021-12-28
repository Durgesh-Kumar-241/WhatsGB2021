package com.dktechhub.gbchat22.whatsweb.Utils;

/* renamed from: c.k.a.e2 */
public class WMessage {

    /* renamed from: a */
    //public String title;

    /* renamed from: b */
    public String text;

    /* renamed from: c */
    public String date;

    /* renamed from: d */
    public Integer idTitle;

    /* renamed from: e */
    public String pathPhoto;

    /* renamed from: f */
    public boolean incoming = false;

    /* renamed from: g */
    public String pathVoice;

    /* renamed from: h */
    //public long timeMilleSecond;


    public WMessage( String text, String date, Integer idTitle, String pathPhoto, boolean incoming, String pathVoice) {
       // this.title = title;
        this.text = text;
        this.date = date;
        this.idTitle = idTitle;
        this.pathPhoto = pathPhoto;
        this.incoming=incoming;
        this.pathVoice = pathVoice;
    }



}