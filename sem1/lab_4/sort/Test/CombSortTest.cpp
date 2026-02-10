#include <gtest/gtest.h>
#include <vector>
#include <algorithm>
#include "../Sort/CombSort.h"
#include "../Sort/Book.h"

TEST(CombSortTest, EmptyVector) {
    std::vector<int> empty;
    CombSort<int> sorter;
    sorter.sort(empty);
    EXPECT_TRUE(empty.empty());
}

TEST(CombSortTest, SingleElement) {
    std::vector<int> single = {42};
    CombSort<int> sorter;
    sorter.sort(single);
    EXPECT_EQ(single.size(), 1);
    EXPECT_EQ(single[0], 42);
}

TEST(CombSortTest, SortedArray) {
    std::vector<int> sorted = {1, 2, 3, 4, 5};
    std::vector<int> expected = sorted;
    CombSort<int> sorter;
    sorter.sort(sorted);
    EXPECT_EQ(sorted, expected);
}

TEST(CombSortTest, ReverseSorted) {
    std::vector<int> reverse = {5, 4, 3, 2, 1};
    std::vector<int> expected = {1, 2, 3, 4, 5};
    CombSort<int> sorter;
    sorter.sort(reverse);
    EXPECT_EQ(reverse, expected);
}

TEST(CombSortTest, RandomArray) {
    std::vector<int> random = {64, 34, 25, 12, 22, 11, 90};
    CombSort<int> sorter;
    sorter.sort(random);
    EXPECT_TRUE(std::is_sorted(random.begin(), random.end()));
}

TEST(CombSortTest, WithBooks) {
    std::vector<Book> books = {
        Book("Book C", 2010),
        Book("Book A", 2000),
        Book("Book B", 2005)
    };
    
    CombSort<Book> sorter;
    sorter.sort(books);
    EXPECT_EQ(books[0].getYear(), 2000);
    EXPECT_EQ(books[1].getYear(), 2005);
    EXPECT_EQ(books[2].getYear(), 2010);
}