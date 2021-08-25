package com.dktechhub.mnnit.ee.whatsweb.Utils;
import android.content.SharedPreferences;
        import android.os.Parcel;
        import android.os.RemoteException;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.gms.dynamic.IObjectWrapper;
        import com.google.android.gms.internal.ads.zzccn;
        import com.google.android.gms.internal.ads.zzcct;
       // import com.google.android.gms.internal.ads.zzcmk;
       // import com.google.android.gms.internal.ads.zzgdw;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.HashSet;
        import org.xmlpull.v1.XmlPullParser;
        import p008b.p094w.C1105v;

/* renamed from: c.b.a.a.a */
public class outline {
    /* renamed from: a */
    public static float m3271a(float f, float f2, float f3, float f4) {
        return ((f - f2) * f3) + f4;
    }

    /* renamed from: b */
    public static int m3272b(int i, int i2, int i3, int i4) {
        return i + i2 + i3 + i4;
    }

    /* renamed from: c */
    public static String m3273c(String str, Fragment fragment, String str2) {
        return str + fragment + str2;
    }

    /* renamed from: d */
    public static String m3274d(String str, String str2) {
        return str + str2;
    }

    /* renamed from: e */
    public static String m3275e(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    /* renamed from: f */
    public static String m3276f(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    /* renamed from: g */
    public static String m3277g(StringBuilder sb, String str, String str2, String str3) {
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb.toString();
    }

    /* renamed from: h */
    public static String m3278h(StringBuilder sb, String str, String str2, String str3, String str4) {
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        return sb.toString();
    }

    /* renamed from: i */
    public static String m3279i(XmlPullParser xmlPullParser, StringBuilder sb, String str) {
        sb.append(xmlPullParser.getPositionDescription());
        sb.append(str);
        return sb.toString();
    }

    /* renamed from: j */
    public static StringBuilder m3280j(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    /* renamed from: k */
    public static ArrayList m3281k(int i, HashMap hashMap, ArrayList arrayList, int i2, String str) {
        hashMap.put(Integer.valueOf(i), arrayList);
        ArrayList arrayList2 = new ArrayList(i2);
        arrayList2.add(str);
        return arrayList2;
    }

    /* renamed from: l */
    public static ArrayList m3282l(ArrayList arrayList, String str, int i, HashMap hashMap, ArrayList arrayList2, int i2) {
        arrayList.add(str);
        hashMap.put(Integer.valueOf(i), arrayList2);
        return new ArrayList(i2);
    }

    /* renamed from: m */
    public static int m3283m(int i, int i2, int i3, int i4) {
        return ((i - i2) * i3) / i4;
    }

    /* renamed from: n */
    public static void m3284n(SharedPreferences sharedPreferences, String str, boolean z) {
        sharedPreferences.edit().putBoolean(str, z).commit();
    }

    /* renamed from: o */
    public static void m3285o(StringBuilder sb, String str, String str2, String str3, String str4) {
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
    }

    /* renamed from: p */
    public static void m3286p(HashSet hashSet, String str, String str2, String str3, String str4) {
        hashSet.add(str);
        hashSet.add(str2);
        hashSet.add(str3);
        hashSet.add(str4);
    }

    /* renamed from: q */
    public static boolean m3287q(String str, String str2) {
        return str2.equals(C1105v.m3128C(C1105v.m3128C(str)));
    }

    /* renamed from: r */
    public static int m3288r(int i, int i2, int i3, int i4) {
        return ((i * i2) / i3) + i4;
    }

    /* renamed from: s */
    public static RemoteException m3289s(String str, Throwable th) {
        zzccn.zzg(str, th);
        return new RemoteException();
    }

    /* renamed from: t */
    public static IObjectWrapper m3290t(Parcel parcel) {
        IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(parcel.readStrongBinder());
        parcel.recycle();
        return asInterface;
    }

    /* renamed from: u */
    public static zzcct m3291u(zzcmk zzcmk) {
        zzcct zzc = zzcmk.zzD(zzcmk).zzc();
        zzgdw.zzb(zzc);
        return zzc;
    }

    /* renamed from: v */
    public static String m3292v(int i, String str, int i2) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(i2);
        return sb.toString();
    }

    /* renamed from: w */
    public static String m3293w(int i, String str, int i2, String str2, int i3) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(i2);
        sb.append(str2);
        sb.append(i3);
        return sb.toString();
    }

    /* renamed from: x */
    public static String m3294x(RecyclerView recyclerView, StringBuilder sb) {
        sb.append(recyclerView.mo1042A());
        return sb.toString();
    }
}
