#include <gtest/gtest.h>
#include "../myset.h"
#include <sstream>
#include <vector>


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

TEST(MySetOperatorTest, Inequality) {
    MySet a("{x,y}"), b("{x,y,z}"), c("{}");
    EXPECT_TRUE(a != b);
    EXPECT_TRUE(a != c);
    EXPECT_FALSE(a != a);
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

TEST(MySetFunctionTest, AddElement) {
    MySet s("{}"); 
    EXPECT_TRUE(s.is_valid());

    s.add_element("x");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"x"}));

    s.add_element("{y,z}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"x", "{y,z}"}));
}

TEST(MySetFunctionTest, AddDuplicateElement) {
    MySet s("{a,b}");
    s.add_element("a"); 
    EXPECT_EQ(s.size(), 2);  
}

TEST(MySetFunctionTest, RemoveElement) {
    MySet s("{a,b,{c,d}}");

    EXPECT_TRUE(s.remove_element("a"));
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"b", "{c,d}"}));

    EXPECT_TRUE(s.remove_element("{c,d}"));
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"b"}));

    EXPECT_FALSE(s.remove_element("nonexistent"));
}

TEST(MySetFunctionTest, Size) {
    MySet s1("{}"), s2("{a}"), s3("{a,b,{c,d}}");
    EXPECT_EQ(s1.size(), 0);
    EXPECT_EQ(s2.size(), 1);
    EXPECT_EQ(s3.size(), 3);
}

TEST(MySetFunctionTest, Contains) {
    MySet s("{a,b,{c,d}}");
    EXPECT_TRUE(s.contains("a"));
    EXPECT_TRUE(s.contains("{c,d}"));
    EXPECT_FALSE(s.contains("nonexistent"));
}

TEST(MySetFunctionTest, OperatorBracket) {
    MySet s("{a,b,{c,d}}");
    EXPECT_TRUE(s["a"]);
    EXPECT_TRUE(s["{c,d}"]);
    EXPECT_FALSE(s["nonexistent"]);
}
TEST(MySetFunctionTest, OperatorBracketSubstring) {
    MySet s("{abc,def}");

    EXPECT_TRUE(s["abc"]);
    EXPECT_FALSE(s["ab"]); 
    EXPECT_FALSE(s["bc"]);
    EXPECT_TRUE(s["def"]);
    EXPECT_FALSE(s["de"]);
    EXPECT_FALSE(s["ef"]);
}

TEST(MySetTrimTest, ThroughPublicMethods) {
    MySet s;
    s.add_element("  hello  ");
    EXPECT_TRUE(s.contains("hello"));

    s.add_element("world  ");
    EXPECT_TRUE(s.contains("world"));
}

TEST(MySetUnionTest, SelfUnion) {
    
    MySet a("{x,y,z}");

    a += a;
    EXPECT_TRUE(a.is_valid());
    EXPECT_EQ(a.size(), 3);

    MySet result = a + a; 
    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3);
}

TEST(MySetUnionTest, ComplexNestedUnion) {
    MySet a("{{1,2},3}");
    MySet b("{3,{4,5},{1,2}}");

    MySet result = a + b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3); 
    EXPECT_TRUE(result.contains("{1,2}"));
    EXPECT_TRUE(result.contains("3"));
    EXPECT_TRUE(result.contains("{4,5}"));
}

TEST(MySetDifferenceTest, DifferenceOperatorValidSets) {
    MySet a("{1,2,3,4,5}");
    MySet b("{2,4,6}");

    MySet result = a - b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3);
    EXPECT_TRUE(result.contains("1"));
    EXPECT_TRUE(result.contains("3"));
    EXPECT_TRUE(result.contains("5"));
    EXPECT_FALSE(result.contains("2"));
    EXPECT_FALSE(result.contains("4"));
}

TEST(MySetIntersectionTest, IntersectionOperatorValidSets) {

    MySet a("{apple,banana,cherry}");
    MySet b("{banana,cherry,date}");

    MySet result = a * b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 2);
    EXPECT_TRUE(result.contains("banana"));
    EXPECT_TRUE(result.contains("cherry"));
    EXPECT_TRUE(a.is_valid());
    EXPECT_EQ(a.size(), 3);
    EXPECT_TRUE(b.is_valid());
    EXPECT_EQ(b.size(), 3);
}

TEST(MySetPowersetTest, PowersetWithNestedSets) {
    MySet nested_set("{a,{b,c}}");
    std::vector<MySet> result = nested_set.powerset();

    EXPECT_EQ(result.size(), 4); 
    bool found_nested = false;
    for (const auto& subset : result) {
        if (subset.contains("{b,c}")) {
            found_nested = true;
            break;
        }
    }
    EXPECT_TRUE(found_nested);
}
TEST(MySetPowersetTest, PowersetFourElements) {
    MySet four_elements("{a,b,c,d}");
    std::vector<MySet> result = four_elements.powerset();

    EXPECT_EQ(result.size(), 16); 
    int size_counts[5] = { 0 };
    for (const auto& subset : result) {
        size_counts[subset.size()]++;
    }
    EXPECT_EQ(size_counts[0], 1);
    EXPECT_EQ(size_counts[1], 4);
    EXPECT_EQ(size_counts[2], 6);
    EXPECT_EQ(size_counts[3], 4);
    EXPECT_EQ(size_counts[4], 1);
}

TEST(MySetCantorTest, CantorSetWithNestedSets) {
    MySet set("{{1,2},3,{4,5},6,{7,8}}");
    MySet result = set.cantor_set(1);

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 4);
    EXPECT_TRUE(result.contains("{1,2}"));
    EXPECT_TRUE(result.contains("3"));
    EXPECT_TRUE(result.contains("6"));
    EXPECT_TRUE(result.contains("{7,8}"));
    EXPECT_FALSE(result.contains("{4,5}"));
}

TEST(MySetCantorTest, CantorSetThreeIterations) {
    MySet set("{1,2,3,4,5,6,7,8,9,10,11,12}"); 
    MySet result = set.cantor_set(3);

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 4);
}


TEST(MySetFunctionTest, ParseCStringThroughConstructor) {
    MySet set("{1,2,3}");
    EXPECT_TRUE(set.is_valid());
    EXPECT_EQ(set.get_elements(), (std::vector<std::string>{"1", "2", "3"}));
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
