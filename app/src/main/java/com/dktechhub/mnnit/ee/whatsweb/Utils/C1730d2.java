package com.dktechhub.mnnit.ee.whatsweb.Utils;




        import android.net.Uri;
        import com.dktechhub.mnnit.ee.whatsweb.Utils.outline;

/* renamed from: c.k.a.d2 */
public class C1730d2 implements Comparable<C1730d2> {

    /* renamed from: b */
    public String f5463b;

    /* renamed from: c */
    public String f5464c;

    /* renamed from: d */
    public Integer f5465d;

    /* renamed from: e */
    public String f5466e;

    /* renamed from: f */
    public String f5467f;

    /* renamed from: g */
    public String f5468g;

    /* renamed from: h */
    public long f5469h;

    /* renamed from: i */
    public Uri f5470i;

    /* renamed from: j */
    public String f5471j;

    public C1730d2(Integer num, String str, String str2, String str3, String str4) {
        this.f5465d = num;
        this.f5463b = str;
        this.f5464c = str2;
        this.f5466e = str3;
        this.f5467f = str4;
    }

    public C1730d2(String str, long j, Uri uri, String str2) {
        this.f5468g = str;
        this.f5469h = j;
        this.f5470i = uri;
        this.f5471j = str2;
    }

    public C1730d2(String str, String str2, String str3, String str4) {
        this.f5463b = str;
        this.f5464c = str2;
        this.f5466e = str3;
        this.f5467f = str4;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // java.lang.Comparable
    public int compareTo(C1730d2 d2Var) {
        return this.f5463b.compareTo(d2Var.f5463b);
    }

    public String toString() {
        StringBuilder j = outline.m3280j("Id: ");
        j.append(this.f5465d);
        j.append("  Name: ");
        j.append(this.f5463b);
        j.append("  Number: ");
        j.append(this.f5464c);
        j.append("  voip1: ");
        j.append(this.f5466e);
        j.append("  voip2: ");
        j.append(this.f5467f);
        return j.toString();
    }
}