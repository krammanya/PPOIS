#include <gtest/gtest.h>
#include "../Sort/Book.h"

TEST(BookTest, Constructor) {
    Book book("Test Book", 2023);
    EXPECT_EQ(book.getTitle(), "Test Book");
    EXPECT_EQ(book.getYear(), 2023);
}

TEST(BookTest, ComparisonOperators) {
    Book book1("Book A", 2000);
    Book book2("Book B", 2010);
    
    EXPECT_TRUE(book1 < book2);
    EXPECT_TRUE(book2 > book1);
    EXPECT_FALSE(book1 > book2);
    EXPECT_FALSE(book2 < book1);
}

TEST(BookTest, OutputOperator) {
    Book book("The Great Gatsby", 1925);
    testing::internal::CaptureStdout();
    std::cout << book;
    std::string output = testing::internal::GetCapturedStdout();
    EXPECT_FALSE(output.empty());
}