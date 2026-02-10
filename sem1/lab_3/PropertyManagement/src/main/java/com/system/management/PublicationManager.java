package com.system.management;

import com.system.news.*;
import com.system.property.PropertyType;
import java.util.*;
import java.util.stream.Collectors;

public class PublicationManager {
    private List<Publication> publications;
    private List<Author> authors;

    public PublicationManager() {
        this.publications = new ArrayList<>();
        this.authors = new ArrayList<>();
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
        Author author = publication.getAuthor();
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    public void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    public <T extends Publication> List<T> getPublicationsByType(Class<T> publicationType, PublicationStatus status) {
        return publications.stream()
                .filter(publicationType::isInstance)
                .filter(p -> status == null || p.getStatus() == status)
                .map(publicationType::cast)
                .sorted((a, b) -> b.getPublishDate().compareTo(a.getPublishDate()))
                .collect(Collectors.toList());
    }

    public List<NewsArticle> getPublishedNews() {
        return getPublicationsByType(NewsArticle.class, PublicationStatus.PUBLISHED);
    }

    public List<Advice> getAllPublishedAdvice() {
        return getPublicationsByType(Advice.class, PublicationStatus.PUBLISHED);
    }

    public List<Publication> getPublicationsByAuthor(Author author) {
        return publications.stream()
                .filter(p -> p.getAuthor().equals(author))
                .collect(Collectors.toList());
    }

    public Publication getMostPopularPublication() {
        return publications.stream()
                .filter(p -> p.getStatus() == PublicationStatus.PUBLISHED)
                .max(Comparator.comparingInt(Publication::getViews))
                .orElse(null);
    }

    public List<Author> getTopAuthors() {
        return authors.stream()
                .sorted((a, b) -> Integer.compare(b.getRating(), a.getRating()))
                .collect(Collectors.toList());
    }

    public List<Publication> getRecentPublications(int count) {
        return publications.stream()
                .filter(p -> p.getStatus() == PublicationStatus.PUBLISHED)
                .sorted((a, b) -> b.getPublishDate().compareTo(a.getPublishDate()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Publication> getPublicationsByTag(String tag) {
        return publications.stream()
                .filter(p -> p.getStatus() == PublicationStatus.PUBLISHED)
                .filter(p -> p.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    public List<Publication> getPublications() {
        return new ArrayList<>(publications);
    }

    public List<Author> getAuthors() {
        return new ArrayList<>(authors);
    }

    public boolean removePublication(Publication publication) {
        return publications.remove(publication);
    }

    public boolean removeAuthor(Author author) {
        publications.removeIf(p -> p.getAuthor().equals(author));
        return authors.remove(author);
    }
}