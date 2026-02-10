package com.system.users;

import com.system.property.Property;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LikedCleaner {

    public static void removeUnavailableProperties(Liked liked) {
        List<Property> unavailable = liked.getProperties().stream()
                .filter(property -> !property.isAvailableForRent())
                .toList();

        unavailable.forEach(liked::remove);
    }

    public static void removeExpiredLikes(Liked liked, int maxDays) {
        if (liked.size() > 10) {
            List<Property> properties = new ArrayList<>(liked.getProperties());
            for (int i = 0; i < properties.size() - 10; i++) {
                liked.remove(properties.get(i));
            }
        }
    }

    public static void cleanAllLiked(Liked liked) {
        removeUnavailableProperties(liked);
        removeExpiredLikes(liked, 30);
    }

    public static String getCleanupReport(Liked liked) {
        int initialSize = liked.size();
        cleanAllLiked(liked);
        int finalSize = liked.size();
        int removed = initialSize - finalSize;

        return String.format(
                "Очистка избранного для %s:\n" +
                        "Удалено объектов: %d\n" +
                        "Осталось в избранном: %d",
                liked.getTenant().getFullName(),
                removed,
                finalSize
        );
    }
}
