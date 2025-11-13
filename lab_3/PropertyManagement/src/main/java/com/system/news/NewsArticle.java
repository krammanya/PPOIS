package com.system.news;

import com.system.property.Property;

public class NewsArticle extends Publication {
    private Property relatedProperty;
    private String summary;

    public NewsArticle(String title, String content, Author author, String summary) {
        super(title, content, author);
        this.summary = summary;
    }

    @Override
    public String getType() {
        return "Новость";
    }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Property getRelatedProperty() { return relatedProperty; }
    public void setRelatedProperty(Property relatedProperty) {
        this.relatedProperty = relatedProperty;
    }
}