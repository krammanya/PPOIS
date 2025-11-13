package com.system.management;

import com.system.users.Tenant;
import com.system.property.Property;
import com.system.property.Address;
import com.system.property.Apartment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class PropertyAllocationResultTest {
    private Tenant tenant;
    private List<PropertyMatch> matches;

    @BeforeEach
    void setUp() {
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.ru", null);

        Address address1 = new Address("Ленина", "Москва", "1");
        Address address2 = new Address("Пушкина", "Санкт-Петербург", "2");
        Address address3 = new Address("Гагарина", "Казань", "3");

        Property property1 = new Apartment("Элитная квартира", address1, 75.0, 5, 3);
        Property property2 = new Apartment("Стандартная квартира", address2, 50.0, 3, 2);
        Property property3 = new Apartment("Бюджетная квартира", address3, 35.0, 2, 1);

        matches = new ArrayList<>();
        matches.add(new PropertyMatch(property1, 0.95));
        matches.add(new PropertyMatch(property2, 0.75));
        matches.add(new PropertyMatch(property3, 0.45));
    }

    @Test
    void testGetBestMatchReturnsHighestScore() {
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, matches, 50);

        PropertyMatch bestMatch = result.getBestMatch();

        assertNotNull(bestMatch);
        assertEquals(0.95, bestMatch.getScore(), 0.001);
    }

    @Test
    void testGetBestMatchWithEmptyListReturnsNull() {
        List<PropertyMatch> emptyMatches = new ArrayList<>();
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, emptyMatches, 50);

        assertNull(result.getBestMatch());
    }

    @Test
    void testGetTopMatchesReturnsLimitedResults() {
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, matches, 50);

        List<PropertyMatch> top2 = result.getTopMatches(2);

        assertEquals(2, top2.size());
        assertEquals(0.95, top2.get(0).getScore(), 0.001);
        assertEquals(0.75, top2.get(1).getScore(), 0.001);
    }

    @Test
    void testGetTopMatchesWithLargeCountReturnsAll() {
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, matches, 50);

        List<PropertyMatch> top10 = result.getTopMatches(10);

        assertEquals(3, top10.size());
    }

    @Test
    void testHasPerfectMatchWhenScoreAboveThreshold() {
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, matches, 50);

        assertTrue(result.hasPerfectMatch());
    }

    @Test
    void testHasPerfectMatchWhenNoHighScores() {
        List<PropertyMatch> noPerfectMatches = new ArrayList<>();
        noPerfectMatches.add(new PropertyMatch(matches.get(0).getProperty(), 0.85));
        noPerfectMatches.add(new PropertyMatch(matches.get(1).getProperty(), 0.75));

        PropertyAllocationResult result = new PropertyAllocationResult(tenant, noPerfectMatches, 50);

        assertFalse(result.hasPerfectMatch());
    }

    @Test
    void testHasPerfectMatchWithExactThreshold() {
        List<PropertyMatch> thresholdMatches = new ArrayList<>();
        thresholdMatches.add(new PropertyMatch(matches.get(0).getProperty(), 0.9));

        PropertyAllocationResult result = new PropertyAllocationResult(tenant, thresholdMatches, 50);

        assertTrue(result.hasPerfectMatch());
    }

    @Test
    void testGetSummaryShowsPerfectMatchMessage() {
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, matches, 100);

        String summary = result.getSummary();

        assertTrue(summary.contains("Есть отличные варианты!"));
        assertTrue(summary.contains("3"));
        assertTrue(summary.contains("100"));
    }

    @Test
    void testGetSummaryShowsAlternativeMessageWhenNoPerfectMatch() {
        List<PropertyMatch> noPerfectMatches = new ArrayList<>();
        noPerfectMatches.add(new PropertyMatch(matches.get(0).getProperty(), 0.85));
        noPerfectMatches.add(new PropertyMatch(matches.get(1).getProperty(), 0.75));

        PropertyAllocationResult result = new PropertyAllocationResult(tenant, noPerfectMatches, 50);

        String summary = result.getSummary();

        assertTrue(summary.contains("Подберите другие параметры."));
        assertTrue(summary.contains("2"));
        assertTrue(summary.contains("50"));
    }

    @Test
    void testGetSummaryWithNoMatches() {
        List<PropertyMatch> emptyMatches = new ArrayList<>();
        PropertyAllocationResult result = new PropertyAllocationResult(tenant, emptyMatches, 100);

        String summary = result.getSummary();

        assertTrue(summary.contains("0 вариантов"));
        assertTrue(summary.contains("Подберите другие параметры."));
    }
}