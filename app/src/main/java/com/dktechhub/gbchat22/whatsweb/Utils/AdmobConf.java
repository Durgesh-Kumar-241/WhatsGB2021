package com.dktechhub.gbchat22.whatsweb.Utils;

public class AdmobConf {
    private int requests = 0;
    private int impressions =0;
    private int maxRequestsDaily =0;
    private int maxImpressionsDaily = 0;
    private String id ="";

    public int getRequests() {
        return requests;
    }
    public void setRequests(int requests) {
        this.requests = requests;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }
    public int getImpressions() {
        return impressions;
    }

    public int getMaxRequestsDaily() {
        return maxRequestsDaily;
    }

    public void setMaxRequestsDaily(int maxRequestsDaily) {
        this.maxRequestsDaily = maxRequestsDaily;
    }

    public int getMaxImpressionsDaily() {
        return maxImpressionsDaily;
    }

    public void setMaxImpressionsDaily(int maxImpressionsDaily) {
        this.maxImpressionsDaily = maxImpressionsDaily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
