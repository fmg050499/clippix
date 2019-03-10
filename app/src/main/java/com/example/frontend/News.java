package com.example.frontend;

public class News {
    String headline;
    String body;
    String filename;
    String userId;
    String tagName;

    public News(String headline, String body, String filename, String tagName, String userId) {

        this.headline = headline;
        this.body = body;
        this.userId = userId;
        this.tagName = tagName;
        this.filename = filename;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
