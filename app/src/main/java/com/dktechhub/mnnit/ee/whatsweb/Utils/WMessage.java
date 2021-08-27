package com.dktechhub.mnnit.ee.whatsweb.Utils;

/* renamed from: c.k.a.e2 */
public class WMessage {

    /* renamed from: a */
    public String title;

    /* renamed from: b */
    public String text;

    /* renamed from: c */
    public String date;

    /* renamed from: d */
    public Integer idTitle;

    /* renamed from: e */
    public String pathPhoto;

    /* renamed from: f */
    public String direction;

    /* renamed from: g */
    public String pathVoice;

    /* renamed from: h */
    public long timeMilleSecond;

    public WMessage(Integer num, String str, String str2, String str3, Integer num2, String str4, String str5, String str6, long j) {
        this.title = str;
        this.text = str2;
        this.date = str3;
        this.idTitle = num2;
        this.pathPhoto = str4;
        this.direction = str5;
        this.pathVoice = str6;
        this.timeMilleSecond = j;
    }

    public WMessage(String str, String str2, String str3, Integer num, String str4, String str5, String str6) {
        this.title = str;
        this.text = str2;
        this.date = str3;
        this.idTitle = num;
        this.pathPhoto = str4;
        this.direction = str5;
        this.pathVoice = str6;
    }

    public String toString() {
        StringBuilder j = outline.m3280j("text: ");
        j.append(this.text);
        j.append("      Direction:  ");
        j.append(this.direction);
        return j.toString();
    }
}