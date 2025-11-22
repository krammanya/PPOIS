#include <gtest/gtest.h>
#include <vector>
#include <algorithm>
#include "../Sort/TournamentSort.h"
#include "../Sort/Book.h" 

TEST(TournamentSortTest, EmptyVector) {
    std::vector<int> empty;
    TombSort<int> sorter;
    sorter.sort(empty);
    EXPECT_TRUE(empty.empty());
}

TEST(TournamentSortTest, SingleElement) {
    std::vector<int> single = {42};
    TombSort<int> sorter;
    sorter.sort(single);
    EXPECT_EQ(single.size(), 1);
    EXPECT_EQ(single[0], 42);
}

TEST(TournamentSortTest, OddSize) {
    std::vector<int> odd = {3, 1, 2};
    TombSort<int> sorter;
    sorter.sort(odd);
    EXPECT_TRUE(std::is_sorted(odd.begin(), odd.end()));
}

TEST(TournamentSortTest, EvenSize) {
    std::vector<int> even = {4, 2, 1, 3};
    TombSort<int> sorter;
    sorter.sort(even);
    EXPECT_TRUE(std::is_sorted(even.begin(), even.end()));
}

TEST(TournamentSortTest, RandomArray) {
    std::vector<int> random = {5, 2, 8, 1, 9, 3, 7, 4, 6};
    TombSort<int> sorter;
    sorter.sort(random);
    EXPECT_TRUE(std::is_sorted(random.begin(), random.end()));
}

TEST(TournamentSortTest, WithBooks) {
    std::vector<Book> books = {
        Book("Book Z", 1995),
        Book("Book X", 2015),
        Book("Book Y", 2005)
    };
    
    TombSort<Book> sorter;
    sorter.sort(books);
    EXPECT_EQ(books[0].getYear(), 1995);
    EXPECT_EQ(books[1].getYear(), 2005);
    EXPECT_EQ(books[2].getYear(), 2015);
}