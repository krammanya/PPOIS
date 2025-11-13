package com.system.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDateTime;

class AuthorTest {

    private Author author;
    private Publication publication1;
    private Publication publication2;

    @BeforeEach
    void setUp() {
        author = new Author("Иван", "Петров", "+79990000000", "ivan@test.com", "Технологии");
        publication1 = new Publication("Java Guide", "Содержание Java", author);
        publication2 = new Publication("Spring Tutorial", "Содержание Spring", author);
    }

    @Test
    void testConstructor() {
        assertEquals("Иван Петров", author.getFullName());
        assertEquals("+79990000000", author.getPhone());
        assertEquals("ivan@test.com", author.getEmail());
        assertEquals("Технологии", author.getSpecialization());
        assertEquals(0, author.getRating());
        assertTrue(author.getPublications().isEmpty());
    }

    @Test
    void testGetSpecialization() {
        assertEquals("Технологии", author.getSpecialization());
    }

    @Test
    void testSetSpecialization() {
        author.setSpecialization("Наука");
        assertEquals("Наука", author.getSpecialization());
    }

    @Test
    void testGetRating() {
        assertEquals(0, author.getRating());
    }

    @Test
    void testIncreaseRating() {
        author.increaseRating();
        assertEquals(1, author.getRating());

        author.increaseRating();
        author.increaseRating();
        assertEquals(3, author.getRating());
    }

    @Test
    void testAddPublication() {
        author.addPublication(publication1);
        assertEquals(1, author.getPublications().size());
        assertEquals(publication1, author.getPublications().get(0));
    }

    @Test
    void testAddMultiplePublications() {
        author.addPublication(publication1);
        author.addPublication(publication2);

        List<Publication> publications = author.getPublications();
        assertEquals(2, publications.size());
        assertTrue(publications.contains(publication1));
        assertTrue(publications.contains(publication2));
    }

    @Test
    void testGetPublicationsReturnsCopy() {
        author.addPublication(publication1);
        List<Publication> publications = author.getPublications();
        publications.clear();

        assertEquals(1, author.getPublications().size());
    }

    @Test
    void testGetPublishedPublications() {
        Publication published1 = new Publication("Published 1", "Содержание 1", author);
        Publication published2 = new Publication("Published 2", "Содержание 2", author);
        Publication draft = new Publication("Draft", "Содержание черновика", author);

        published1.publish();
        published2.publish();

        author.addPublication(published1);
        author.addPublication(draft);
        author.addPublication(published2);

        List<Publication> published = author.getPublishedPublications();
        assertEquals(2, published.size());
        assertTrue(published.contains(published1));
        assertTrue(published.contains(published2));
        assertFalse(published.contains(draft));
    }

    @Test
    void testGetPublishedPublicationsEmpty() {
        assertTrue(author.getPublishedPublications().isEmpty());
    }

    @Test
    void testGetPublishedPublicationsOnlyDrafts() {
        Publication draft1 = new Publication("Draft 1", "Содержание 1", author);
        Publication draft2 = new Publication("Draft 2", "Содержание 2", author);

        author.addPublication(draft1);
        author.addPublication(draft2);

        assertTrue(author.getPublishedPublications().isEmpty());
    }

    @Test
    void testGetAuthorInfo() {
        String info = author.getAuthorInfo();
        assertTrue(info.contains("Иван Петров"));
        assertTrue(info.contains("Технологии"));
        assertTrue(info.contains("Рейтинг: 0"));
    }

    @Test
    void testGetAuthorInfoWithRating() {
        author.increaseRating();
        author.increaseRating();

        String info = author.getAuthorInfo();
        assertTrue(info.contains("Рейтинг: 2"));
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(author, author);
    }

    @Test
    void testEqualsSameEmail() {
        Author author2 = new Author("Иван", "Иванов", "+79991111111", "ivan@test.com", "Другая специализация");
        assertEquals(author, author2);
    }

    @Test
    void testEqualsDifferentEmail() {
        Author author2 = new Author("Иван", "Петров", "+79990000000", "different@test.com", "Технологии");
        assertNotEquals(author, author2);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(null, author);
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals(author, "not an author");
    }

    @Test
    void testHashCodeSameEmail() {
        Author author1 = new Author("Иван", "Петров", "+79990000000", "ivan@test.com", "Технологии");
        Author author2 = new Author("Петр", "Иванов", "+79991111111", "ivan@test.com", "Наука");

        assertEquals(author1.hashCode(), author2.hashCode());
    }

    @Test
    void testHashCodeDifferentEmail() {
        Author author1 = new Author("Иван", "Петров", "+79990000000", "ivan@test.com", "Технологии");
        Author author2 = new Author("Иван", "Петров", "+79990000000", "petr@test.com", "Технологии");

        assertNotEquals(author1.hashCode(), author2.hashCode());
    }

    @Test
    void testEmptyPublications() {
        assertTrue(author.getPublications().isEmpty());
        assertTrue(author.getPublishedPublications().isEmpty());
    }

    @Test
    void testMixedPublicationStatuses() {
        Publication published = new Publication("Published", "Содержание", author);
        Publication draft = new Publication("Draft", "Содержание черновика", author);

        published.publish();

        author.addPublication(published);
        author.addPublication(draft);

        assertEquals(2, author.getPublications().size());
        assertEquals(1, author.getPublishedPublications().size());
        assertEquals(published, author.getPublishedPublications().get(0));
    }

    @Test
    void testAuthorRatingIncreasesWhenPublicationPublished() {
        Publication publication = new Publication("Test", "Content", author);
        assertEquals(0, author.getRating());

        publication.publish();
        assertEquals(1, author.getRating());
    }

    @Test
    void testMultiplePublicationsIncreaseRating() {
        Publication pub1 = new Publication("Pub 1", "Content 1", author);
        Publication pub2 = new Publication("Pub 2", "Content 2", author);

        pub1.publish();
        assertEquals(1, author.getRating());

        pub2.publish();
        assertEquals(2, author.getRating());
    }

    @Test
    void testPublicationWithDifferentAuthor() {
        Author otherAuthor = new Author("Другой", "Автор", "+79992222222", "other@test.com", "Другое");
        Publication publication = new Publication("Test", "Content", otherAuthor);

        author.addPublication(publication);
        assertEquals(1, author.getPublications().size());
        assertEquals(publication, author.getPublications().get(0));
    }
}