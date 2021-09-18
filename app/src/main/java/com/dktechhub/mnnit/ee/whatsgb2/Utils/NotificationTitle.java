package com.dktechhub.mnnit.ee.whatsgb2.Utils;

/* renamed from: c.k.a.f2 */
public class NotificationTitle {

    /* renamed from: a */
    public Integer id;

    /* renamed from: b */
    public String title;
    public String summary;
    /* renamed from: c */
    public byte[] photo;

    /* renamed from: d */
    public Integer count;

    /* renamed from: e */
    public String date;

    /* renamed from: f */
    public String number;

    public NotificationTitle(Integer id, String title, byte[] photo, Integer count, String date, String number,String summary) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.count = count;
        this.date = date;
        this.summary=summary;
        this.number = number;
    }


}