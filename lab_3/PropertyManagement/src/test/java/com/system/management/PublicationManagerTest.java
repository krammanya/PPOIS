package com.system.management;

import com.system.news.*;
import com.system.property.Property;
import com.system.property.Address;
import com.system.property.Apartment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PublicationManagerTest {
    private PublicationManager publicationManager;
    private Author author1;
    private Author author2;
    private NewsArticle newsArticle;
    private Advice advice;

    @BeforeEach
    void setUp() {
        publicationManager = new PublicationManager();

        author1 = new Author("Иван", "Иванов", "+79990000000", "ivan@test.ru", "Недвижимость");
        author2 = new Author("Петр", "Петров", "+79991111111", "petr@test.ru", "Финансы");

        Property property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);

        newsArticle = new NewsArticle("Новость о недвижимости", "Содержание новости", author1, "Краткое описание");
        advice = new Advice("Советы по аренде", "Содержание советов", author2, "Совет 1", "Совет 2", "Совет 3");

        newsArticle.publish();
        advice.publish();

        newsArticle.addView();
        newsArticle.addView();
        advice.addView();

        newsArticle.addTag("недвижимость");
        newsArticle.addTag("новости");
        advice.addTag("аренда");
        advice.addTag("советы");
    }

    @Test
    void testPublicationManagement() {
        publicationManager.addPublication(newsArticle);
        publicationManager.addPublication(advice);

        assertEquals(2, publicationManager.getPublications().size());
        assertEquals(2, publicationManager.getAuthors().size());

        assertTrue(publicationManager.removePublication(newsArticle));
        assertEquals(1, publicationManager.getPublications().size());

        assertTrue(publicationManager.removeAuthor(author2));
        assertEquals(1, publicationManager.getAuthors().size());
        assertEquals(0, publicationManager.getPublications().size());
    }

    @Test
    void testPublicationFiltering() {
        NewsArticle draftArticle = new NewsArticle("Черновик", "Содержание", author1, "Описание");

        publicationManager.addPublication(newsArticle);
        publicationManager.addPublication(advice);
        publicationManager.addPublication(draftArticle);

        assertEquals(1, publicationManager.getPublishedNews().size());
        assertEquals(1, publicationManager.getAllPublishedAdvice().size());
        assertEquals(2, publicationManager.getPublicationsByAuthor(author1).size());

        assertEquals(newsArticle, publicationManager.getMostPopularPublication());

        List<Author> topAuthors = publicationManager.getTopAuthors();
        assertEquals(2, topAuthors.size());

        assertEquals(2, publicationManager.getRecentPublications(5).size());
        assertEquals(1, publicationManager.getPublicationsByTag("аренда").size());
        assertEquals(1, publicationManager.getPublicationsByTag("недвижимость").size());
        assertEquals(0, publicationManager.getPublicationsByTag("несуществующий").size());

        List<NewsArticle> allNews = publicationManager.getPublicationsByType(NewsArticle.class, null);
        assertEquals(2, allNews.size());

        List<NewsArticle> publishedNews = publicationManager.getPublicationsByType(NewsArticle.class, PublicationStatus.PUBLISHED);
        assertEquals(1, publishedNews.size());
    }
}