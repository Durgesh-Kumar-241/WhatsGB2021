package com.dktechhub.mnnit.ee.whatsweb.Utils;
import com.dktechhub.mnnit.ee.whatsweb.Utils.outline;

/* renamed from: c.k.a.e2 */
public class C1734e2 {

    /* renamed from: a */
    public String f5474a;

    /* renamed from: b */
    public String f5475b;

    /* renamed from: c */
    public String f5476c;

    /* renamed from: d */
    public Integer f5477d;

    /* renamed from: e */
    public String f5478e;

    /* renamed from: f */
    public String f5479f;

    /* renamed from: g */
    public String f5480g;

    /* renamed from: h */
    public long f5481h;

    public C1734e2(Integer num, String str, String str2, String str3, Integer num2, String str4, String str5, String str6, long j) {
        this.f5474a = str;
        this.f5475b = str2;
        this.f5476c = str3;
        this.f5477d = num2;
        this.f5478e = str4;
        this.f5479f = str5;
        this.f5480g = str6;
        this.f5481h = j;
    }

    public C1734e2(String str, String str2, String str3, Integer num, String str4, String str5, String str6) {
        this.f5474a = str;
        this.f5475b = str2;
        this.f5476c = str3;
        this.f5477d = num;
        this.f5478e = str4;
        this.f5479f = str5;
        this.f5480g = str6;
    }

    public String toString() {
        StringBuilder j = outline.m3280j("text: ");
        j.append(this.f5475b);
        j.append("      Direction:  ");
        j.append(this.f5479f);
        return j.toString();
    }
}