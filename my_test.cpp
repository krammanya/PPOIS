#include <gtest/gtest.h>
#include "../myset.h"
#include <sstream>

// Конструкторы
TEST(MySetConstructorTest, DefaultConstructor) {
    MySet s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(MySetConstructorTest, StringConstructorValid) {
    MySet s("{a,b,{c}}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "b", "{c}"}));
}

TEST(MySetConstructorTest, StringConstructorInvalid) {
    MySet s("{a,,b}");
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(MySetConstructorTest, CStrConstructorValid) {
    MySet s("{x,{y,z}}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"x", "{y,z}"}));
}

TEST(MySetConstructorTest, CStrConstructorInvalid) {
    MySet s("{x,{y,,z}}");
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(MySetConstructorTest, CopyConstructor) {
    MySet original("{a,b}");
    MySet copy(original);
    EXPECT_EQ(copy.get_elements(), original.get_elements());
    EXPECT_EQ(copy.is_valid(), original.is_valid());
}

TEST(MySetConstructorTest, CopyConstructorInvalid) {
    MySet broken("{a,,b}");
    MySet copy(broken);
    EXPECT_FALSE(copy.is_valid());
    EXPECT_TRUE(copy.get_elements().empty());
}

// Оператор присваивания
TEST(MySetAssignmentTest, AssignmentOperator) {
    MySet a("{x,y}");
    MySet b;
    b = a;
    EXPECT_EQ(b.get_elements(), a.get_elements());
    EXPECT_TRUE(b.is_valid());
}

TEST(MySetAssignmentTest, AssignmentFromInvalid) {
    MySet broken("{x,,y}");
    MySet b;
    b = broken;
    EXPECT_FALSE(b.is_valid());
    EXPECT_TRUE(b.get_elements().empty());
}

TEST(MySetAssignmentTest, SelfAssignment) {
    MySet s("{1,2,3}");
    s = s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements().size(), 3);
}

// ТЕСТИРОВАНИЕ ВАЛИДНОСТИ ЧЕРЕЗ КОНСТРУКТОРЫ (вместо check)
TEST(MySetValidityTest, ValidCases) {
    MySet set1("{a,b,c}");
    EXPECT_TRUE(set1.is_valid());

    MySet set2("{a,{b,c},{d,{e}}}");
    EXPECT_TRUE(set2.is_valid());

    MySet set3("{ a , b , { c , d } }");
    EXPECT_TRUE(set3.is_valid());

    MySet set4("{}");
    EXPECT_TRUE(set4.is_valid());

    MySet set5("{{},{}}");
    EXPECT_TRUE(set5.is_valid());
}

TEST(MySetValidityTest, InvalidCases) {
    MySet set1("{a,b,{c,d}");
    EXPECT_FALSE(set1.is_valid());

    MySet set2("{a,b,}");
    EXPECT_FALSE(set2.is_valid());

    MySet set3("   ");
    EXPECT_FALSE(set3.is_valid());

    MySet set4("{a,,b}");
    EXPECT_FALSE(set4.is_valid());

    MySet set5("{a,{b,c,}}");
    EXPECT_FALSE(set5.is_valid());
}

// ТЕСТИРОВАНИЕ ПАРСИНГА ЧЕРЕЗ КОНСТРУКТОРЫ + get_elements()
TEST(MySetParseTest, ValidParsing) {
    MySet set1("{ x , { y , { z , { } } } , w }");
    EXPECT_TRUE(set1.is_valid());
    EXPECT_EQ(set1.get_elements(), (std::vector<std::string>{"x", "{ y , { z , { } } }", "w"}));

    MySet set2("{ { a , { b , { c , { d } } } } , e }");
    EXPECT_TRUE(set2.is_valid());
    EXPECT_EQ(set2.get_elements(), (std::vector<std::string>{"{ a , { b , { c , { d } } } }", "e"}));
}

TEST(MySetParseTest, InvalidParsing) {
    MySet set1("{a,{b,{c,{d,{e}}}}");
    EXPECT_FALSE(set1.is_valid());
    EXPECT_TRUE(set1.get_elements().empty());

    MySet set2("{a,{b,c},,{d,e}}");
    EXPECT_FALSE(set2.is_valid());
    EXPECT_TRUE(set2.get_elements().empty());

    MySet set3("{,{a,{b,{c,d}");
    EXPECT_FALSE(set3.is_valid());
    EXPECT_TRUE(set3.get_elements().empty());

    MySet set4("{a,,b}");
    EXPECT_FALSE(set4.is_valid());
    EXPECT_TRUE(set4.get_elements().empty());
}

// Операторы
TEST(MySetOperatorTest, Equality) {
    MySet a("{x,y,{z}}"), b("{x,y,{z}}"), c("{b,a}"), d("{}");
    EXPECT_TRUE(a == b);
    EXPECT_TRUE(d == MySet("{}"));
    EXPECT_FALSE(a == c);
}

TEST(MySetOperatorTest, Output) {
    MySet s1("{a,b,c}"), s2("{}"), s3("{x,{y,z},w}");
    std::ostringstream oss;

    oss << s1;
    EXPECT_EQ(oss.str(), "{a, b, c}");

    oss.str(""); oss << s2;
    EXPECT_EQ(oss.str(), "{}");

    oss.str(""); oss << s3;
    EXPECT_EQ(oss.str(), "{x, {y,z}, w}");
}

TEST(MySetOperatorTest, Input) {
    MySet s;
    std::istringstream iss1("{a,b,{c,d}}"), iss2("{a,b,,c}"), iss3("{}");

    iss1 >> s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "b", "{c,d}"}));

    iss2 >> s;
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());

    iss3 >> s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

// Публичные методы
TEST(MySetFunctionTest, GetElements) {
    MySet s1("{x,{y,z}}"), s2("{}");
    EXPECT_EQ(s1.get_elements(), (std::vector<std::string>{"x", "{y,z}"}));
    EXPECT_TRUE(s2.get_elements().empty());
}

TEST(MySetFunctionTest, Print) {
    MySet s1("{a,b}"), s2("{}");
    std::ostringstream oss;
    std::streambuf* old = std::cout.rdbuf(oss.rdbuf());

    s1.print();
    EXPECT_EQ(oss.str(), "{a, b}\n");

    oss.str(""); s2.print();
    EXPECT_EQ(oss.str(), "{}\n");

    std::cout.rdbuf(old);
}

TEST(MySetFunctionTest, IsValid) {
    MySet s1("{a,{b}}"), s2("{a,,b}");
    EXPECT_TRUE(s1.is_valid());
    EXPECT_FALSE(s2.is_valid());
}

// Тестирование парсинга C-строк через конструктор
TEST(MySetFunctionTest, ParseCStringThroughConstructor) {
    MySet set("{1,2,3}");
    EXPECT_TRUE(set.is_valid());
    EXPECT_EQ(set.get_elements(), (std::vector<std::string>{"1", "2", "3"}));
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}