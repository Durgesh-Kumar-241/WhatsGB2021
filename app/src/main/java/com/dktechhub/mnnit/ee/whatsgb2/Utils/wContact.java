package com.dktechhub.mnnit.ee.whatsgb2.Utils;




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

    public wContact(String str, long j, Uri uri, String str2) {
        this.f5468g = str;
        this.f5469h = j;
        this.uri = uri;
        this.f5471j = str2;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // java.lang.Comparable
    public int compareTo(wContact d2Var) {
        return this.name.compareTo(d2Var.name);
    }


}