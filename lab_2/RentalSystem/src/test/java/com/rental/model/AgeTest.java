package com.rental.model;

import com.rental.exceptions.InvalidAgeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgeTest {

    @Test
    void shouldCoverAllLogicAndBranches() {
        Age age18 = new Age(18);
        assertTrue(age18.isAdult());
        assertTrue(age18.isAtLeast(18));
        assertFalse(age18.isAtLeast(19));
        assertTrue(age18.isBetween(18, 25));
        assertFalse(age18.isBetween(19, 25));

        Age age25 = new Age(25);
        assertTrue(age25.isAdult());
        assertTrue(age25.isAtLeast(18));
        assertTrue(age25.isAtLeast(25));
        assertFalse(age25.isAtLeast(26));
        assertTrue(age25.isBetween(20, 30));
        assertFalse(age25.isBetween(30, 40));

        assertThrows(InvalidAgeException.class, () -> new Age(-1));
        assertThrows(InvalidAgeException.class, () -> new Age(121));
        assertThrows(InvalidAgeException.class, () -> new Age(200));
        assertThrows(InvalidAgeException.class, () -> new Age(-100));

        assertDoesNotThrow(() -> new Age(0));
        assertDoesNotThrow(() -> new Age(120));
    }
}