package com.dktechhub.mnnit.ee.whatsweb.Utils;




import android.net.Uri;

/* renamed from: c.k.a.d2 */
public class wContact implements Comparable<wContact> {

    /* renamed from: b */
    public String name;

    /* renamed from: c */
    public String number;

    /* renamed from: d */
    public Integer id;

    /* renamed from: e */
    public String voip1;

    /* renamed from: f */
    public String voip2;

    /* renamed from: g */
    public String f5468g;

    /* renamed from: h */
    public long f5469h;

    /* renamed from: i */
    public Uri uri;

    /* renamed from: j */
    public String f5471j;

    public wContact(Integer num, String str, String str2, String str3, String str4) {
        this.id = num;
        this.name = str;
        this.number = str2;
        this.voip1 = str3;
        this.voip2 = str4;
    }

    public wContact(String str, long j, Uri uri, String str2) {
        this.f5468g = str;
        this.f5469h = j;
        this.uri = uri;
        this.f5471j = str2;
    }

    public wContact(String str, String str2, String str3, String str4) {
        this.name = str;
        this.number = str2;
        this.voip1 = str3;
        this.voip2 = str4;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // java.lang.Comparable
    public int compareTo(wContact d2Var) {
        return this.name.compareTo(d2Var.name);
    }

    public String toString() {
        StringBuilder j = outline.m3280j("Id: ");
        j.append(this.id);
        j.append("  Name: ");
        j.append(this.name);
        j.append("  Number: ");
        j.append(this.number);
        j.append("  voip1: ");
        j.append(this.voip1);
        j.append("  voip2: ");
        j.append(this.voip2);
        return j.toString();
    }
}