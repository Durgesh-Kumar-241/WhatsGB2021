package com.dktechhub.mnnit.ee.whatsweb.Utils;

import com.dktechhub.mnnit.ee.whatsweb.Utils.outline;

/* renamed from: c.k.a.f2 */
public class C1738f2 {

    /* renamed from: a */
    public Integer f5485a;

    /* renamed from: b */
    public String f5486b;

    /* renamed from: c */
    public byte[] f5487c;

    /* renamed from: d */
    public Integer f5488d;

    /* renamed from: e */
    public String f5489e;

    /* renamed from: f */
    public String f5490f;

    public C1738f2(Integer num, String str, byte[] bArr, Integer num2, String str2, String str3) {
        this.f5485a = num;
        this.f5486b = str;
        this.f5487c = bArr;
        this.f5488d = num2;
        this.f5489e = str2;
        this.f5490f = str3;
    }

    public String toString() {
        StringBuilder j = outline.m3280j("Title: ");
        j.append(this.f5486b);
        j.append("   number: ");
        j.append(this.f5490f);
        j.append("  count: ");
        j.append(this.f5488d);
        return j.toString();
    }
}