package com.shubhobrata.roy.nasaspaceapps;

/**
 * Created by shubh on 4/28/2017.
 */

public class NewsHolder {
    public String url;

    public String getUrl() {
        return url;
    }

    public  NewsHolder () {}
    public NewsHolder(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String title;

}
