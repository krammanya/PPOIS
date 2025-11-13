package com.system.management;

import com.system.users.Tenant;
import java.util.List;

public class PropertyAllocationResult {
    private Tenant tenant;
    private List<PropertyMatch> matches;
    private int totalPropertiesConsidered;

    public PropertyAllocationResult(Tenant tenant, List<PropertyMatch> matches, int totalPropertiesConsidered) {
        this.tenant = tenant;
        this.matches = matches;
        this.totalPropertiesConsidered = totalPropertiesConsidered;
    }

    public PropertyMatch getBestMatch() {
        return matches.isEmpty() ? null : matches.get(0);
    }

    public List<PropertyMatch> getTopMatches(int count) {
        return matches.stream().limit(count).toList();
    }

    public boolean hasPerfectMatch() {
        return matches.stream().anyMatch(match -> match.getScore() >= 0.9);
    }

    public String getSummary() {
        return String.format("Для %s найдено %d вариантов из %d. %s",
                tenant.getFullName(),
                matches.size(),
                totalPropertiesConsidered,
                hasPerfectMatch() ? "Есть отличные варианты!" : "Подберите другие параметры.");
    }

    public Tenant getTenant() { return tenant; }
    public List<PropertyMatch> getMatches() { return matches; }
    public int getTotalPropertiesConsidered() { return totalPropertiesConsidered; }
}