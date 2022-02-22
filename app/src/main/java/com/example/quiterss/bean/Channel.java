package com.example.quiterss.bean;

public class Channel {
    private String title;
    private String description;
    private String link;
    private String RSSlink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRSSlink() {
        return RSSlink;
    }

    public void setRSSlink(String RSSlink) {
        this.RSSlink = RSSlink;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", RSSlink='" + RSSlink + '\'' +
                '}';
    }
}
