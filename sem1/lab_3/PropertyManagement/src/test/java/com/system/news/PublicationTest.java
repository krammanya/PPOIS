package com.system.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PublicationTest {
    private Publication publication;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Иван", "Иванов", "+79990000000", "ivan@test.ru", "Недвижимость");
        publication = new Publication("Тестовая публикация", "Содержание публикации", author);
    }

    @Test
    void testPublicationLifecycle() {
        assertEquals("Тестовая публикация", publication.getTitle());
        assertEquals("Содержание публикации", publication.getContent());
        assertEquals(author, publication.getAuthor());
        assertEquals(PublicationStatus.DRAFT, publication.getStatus());
        assertEquals(0, publication.getViews());
        assertTrue(publication.getTags().isEmpty());
        assertEquals("Публикация", publication.getType());

        publication.publish();
        assertEquals(PublicationStatus.PUBLISHED, publication.getStatus());
        assertEquals(1, author.getRating());

        publication.addView();
        publication.addView();
        assertEquals(2, publication.getViews());

        publication.addTag("недвижимость");
        publication.addTag("советы");
        assertEquals(2, publication.getTags().size());
        assertTrue(publication.getTags().contains("недвижимость"));

        String fullInfo = publication.getFullInfo();
        assertTrue(fullInfo.contains("Тестовая публикация"));
        assertTrue(fullInfo.contains("Иван Иванов"));
        assertTrue(fullInfo.contains("2"));

        publication.setTitle("Новое название");
        publication.setContent("Новое содержание");
        assertEquals("Новое название", publication.getTitle());
        assertEquals("Новое содержание", publication.getContent());

        LocalDateTime newDate = LocalDateTime.now().minusDays(1);
        publication.setPublishDate(newDate);
        assertEquals(newDate, publication.getPublishDate());

        Author newAuthor = new Author("Петр", "Петров", "+79991111111", "petr@test.ru", "Финансы");
        publication.setAuthor(newAuthor);
        assertEquals(newAuthor, publication.getAuthor());
    }
}