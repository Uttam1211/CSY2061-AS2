package com.example.kewis.models;

public class Feed {

    private int id;
    private String feedName;

    public Feed(int id, String feedName) {
        this.id = id;
        this.feedName = feedName;
    }

    public Feed(String feedTitle) {
        this.feedName = feedTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
