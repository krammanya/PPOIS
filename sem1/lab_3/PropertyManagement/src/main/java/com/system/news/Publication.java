package com.system.news;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.system.interfaces.TypedPublication;

public class Publication implements TypedPublication{
    private String title;
    private String content;
    private Author author;
    private LocalDateTime publishDate;
    private PublicationStatus status;
    private int views;
    private List<String> tags;

    public Publication(String title, String content, Author author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishDate = LocalDateTime.now();
        this.status = PublicationStatus.DRAFT;
        this.views = 0;
        this.tags = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Author getAuthor() { return author; }
    public LocalDateTime getPublishDate() { return publishDate; }
    public PublicationStatus getStatus() { return status; }
    public int getViews() { return views; }
    public List<String> getTags() { return tags; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setStatus(PublicationStatus status) { this.status = status; }
    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    public void publish() {
        this.status = PublicationStatus.PUBLISHED;
        this.publishDate = LocalDateTime.now();
        this.author.increaseRating();
    }

    public void addView() { this.views++; }
    public void addTag(String tag) { tags.add(tag); }

    @Override
    public String getType() {
        return "Публикация";
    }

    public String getFullInfo() {
        return String.format("%s: %s\nАвтор: %s\nПросмотры: %d\nДата: %s",
                getType(), title, author.getFullName(), views, publishDate);
    }
}

