package com.renbo.newsproject.splash.bean;

import java.util.List;

public class Ads {
    int next_req;
    String error;
    List<AdsDetail> ads;

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<AdsDetail> getAds() {
        return ads;
    }

    public void setAds(List<AdsDetail> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "next_req=" + next_req +
                ", error='" + error + '\'' +
                ", ads=" + ads +
                '}';
    }
}
