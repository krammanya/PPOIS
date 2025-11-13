package com.system.management;
import com.system.property.Property;

public class PropertyMatch {
    private Property property;
    private double score;

    public PropertyMatch(Property property, double score) {
        this.property = property;
        this.score = score;
    }

    public Property getProperty() { return property; }
    public double getScore() { return score; }

    @Override
    public String toString() {
        return String.format("PropertyMatch{property=%s, score=%.2f}",
                property.getName(), score);
    }
}