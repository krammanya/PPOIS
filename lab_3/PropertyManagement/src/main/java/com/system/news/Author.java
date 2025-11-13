package com.system.news;

import com.system.users.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

public class Author extends Person {
    private String specialization;
    private int rating;
    private List<Publication> publications;

    public Author(String firstName, String lastName, String phone, String email, String specialization) {
        super(firstName, lastName, phone, email);
        this.specialization = specialization;
        this.rating = 0;
        this.publications = new ArrayList<>();
    }

    public String getSpecialization() { return specialization; }
    public int getRating() { return rating; }
    public List<Publication> getPublications() { return new ArrayList<>(publications); }

    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void increaseRating() { this.rating++; }

    public void addPublication(Publication publication) {
        publications.add(publication);
    }

    public List<Publication> getPublishedPublications() {
        return publications.stream()
                .filter(p -> p.getStatus() == PublicationStatus.PUBLISHED)
                .collect(Collectors.toList());
    }

    public String getAuthorInfo() {
        return String.format("%s (%s) - Рейтинг: %d", getFullName(), specialization, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return getEmail().equals(author.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}