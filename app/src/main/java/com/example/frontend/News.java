package com.example.frontend;

public class News implements Comparable<News> {
    String headline;
    String body;
    String filename;
    String userId;
    String tags;
    String time;

    public News(String headline, String body, String filename, String tags, String time, String userId) {

        this.headline = headline;
        this.body = body;
        this.userId = userId;
        this.tags = tags;
        this.filename = filename;
        this.time=time;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(News other) {
        return Integer.parseInt(other.getFilename())-Integer.parseInt(this.getFilename());
    }
}
