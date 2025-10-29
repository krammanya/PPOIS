#include <gtest/gtest.h>
#include "../CustomSet.h"
#include <sstream>
#include <vector>


TEST(CustomSetConstructorTest, DefaultConstructor) {
    CustomSet s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(CustomSetConstructorTest, StringConstructorValid) {
    CustomSet s("{a,b,{c}}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "b", "{c}"}));
}

TEST(CustomSetConstructorTest, StringConstructorInvalid) {
    CustomSet s("{a,,b}");
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(CustomSetConstructorTest, CStrConstructorValid) {
    CustomSet s("{x,{y,z}}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"x", "{y,z}"}));
}

TEST(CustomSetConstructorTest, CStrConstructorInvalid) {
    CustomSet s("{x,{y,,z}}");
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());
}

TEST(CustomSetConstructorTest, CopyConstructor) {
    CustomSet original("{a,b}");
    CustomSet copy(original);
    EXPECT_EQ(copy.get_elements(), original.get_elements());
    EXPECT_EQ(copy.is_valid(), original.is_valid());
}

TEST(CustomSetConstructorTest, CopyConstructorInvalid) {
    CustomSet broken("{a,,b}");
    CustomSet copy(broken);
    EXPECT_FALSE(copy.is_valid());
    EXPECT_TRUE(copy.get_elements().empty());
}


TEST(CustomSetAssignmentTest, AssignmentOperator) {
    CustomSet a("{x,y}");
    CustomSet b;
    b = a;
    EXPECT_EQ(b.get_elements(), a.get_elements());
    EXPECT_TRUE(b.is_valid());
}

TEST(CustomSetAssignmentTest, AssignmentFromInvalid) {
    CustomSet broken("{x,,y}");
    CustomSet b;
    b = broken;
    EXPECT_FALSE(b.is_valid());
    EXPECT_TRUE(b.get_elements().empty());
}

TEST(CustomSetAssignmentTest, SelfAssignment) {
    CustomSet s("{1,2,3}");
    s = s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements().size(), 3);
}


TEST(CustomSetValidityTest, InvalidCases) {
    CustomSet set1("{a,b,{c,d}");
    EXPECT_FALSE(set1.is_valid());

    CustomSet set2("{a,b,}");
    EXPECT_FALSE(set2.is_valid());

    CustomSet set3("   ");
    EXPECT_FALSE(set3.is_valid());

    CustomSet set4("{a,,b}");
    EXPECT_FALSE(set4.is_valid());

    CustomSet set5("{a,{b,c,}}");
    EXPECT_FALSE(set5.is_valid());
}

TEST(CustomSetParseTest, ValidParsing) {
    CustomSet set1("{ x , { y , { z , { } } } , w }");
    EXPECT_TRUE(set1.is_valid());
    EXPECT_EQ(set1.get_elements(), (std::vector<std::string>{"x", "{ y , { z , { } } }", "w"}));

    CustomSet set2("{ { a , { b , { c , { d } } } } , e }");
    EXPECT_TRUE(set2.is_valid());
    EXPECT_EQ(set2.get_elements(), (std::vector<std::string>{"{ a , { b , { c , { d } } } }", "e"}));
}

TEST(CustomSetParseTest, InvalidParsing) {
    CustomSet set1("{a,{b,{c,{d,{e}}}}");
    EXPECT_FALSE(set1.is_valid());
    EXPECT_TRUE(set1.get_elements().empty());

    CustomSet set2("{a,{b,c},,{d,e}}");
    EXPECT_FALSE(set2.is_valid());
    EXPECT_TRUE(set2.get_elements().empty());

    CustomSet set3("{,{a,{b,{c,d}");
    EXPECT_FALSE(set3.is_valid());
    EXPECT_TRUE(set3.get_elements().empty());

    CustomSet set4("{a,,b}");
    EXPECT_FALSE(set4.is_valid());
    EXPECT_TRUE(set4.get_elements().empty());
}


TEST(CustomSetOperatorTest, Equality) {
    CustomSet a("{x,y,{z}}"), b("{x,y,{z}}"), c("{b,a}"), d("{}");
    EXPECT_TRUE(a == b);
    EXPECT_TRUE(d == CustomSet("{}"));
    EXPECT_FALSE(a == c);
}

TEST(CustomSetOperatorTest, Output) {
    CustomSet s1("{a,b,c}"), s2("{}"), s3("{x,{y,z},w}");
    std::ostringstream oss;

    oss << s1;
    EXPECT_EQ(oss.str(), "{a, b, c}");

    oss.str(""); oss << s2;
    EXPECT_EQ(oss.str(), "{}");

    oss.str(""); oss << s3;
    EXPECT_EQ(oss.str(), "{x, {y,z}, w}");
}

TEST(CustomSetOperatorTest, Inequality) {
    CustomSet a("{x,y}"), b("{x,y,z}"), c("{}");
    EXPECT_TRUE(a != b);
    EXPECT_TRUE(a != c);
    EXPECT_FALSE(a != a);
}

TEST(CustomSetOperatorTest, Input) {
    CustomSet s;
    std::istringstream iss1("{a,b,{c,d}}"), iss2("{a,b,,c}"), iss3("{{}}");

    iss1 >> s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "b", "{c,d}"}));

    iss2 >> s;
    EXPECT_FALSE(s.is_valid());
    EXPECT_TRUE(s.get_elements().empty());

    iss3 >> s;
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"{}"}));
}

TEST(CustomSetFunctionTest, GetElements) {
    CustomSet s1("{x,{y,z}}"), s2("{}");
    EXPECT_EQ(s1.get_elements(), (std::vector<std::string>{"x", "{y,z}"}));
    EXPECT_TRUE(s2.get_elements().empty());
}

TEST(CustomSetFunctionTest, Print) {
    CustomSet s1("{a,b}"), s2("{}");
    std::ostringstream oss;
    std::streambuf* old = std::cout.rdbuf(oss.rdbuf());

    s1.print();
    EXPECT_EQ(oss.str(), "{a, b}\n");

    oss.str(""); s2.print();
    EXPECT_EQ(oss.str(), "{}\n");

    std::cout.rdbuf(old);
}

TEST(CustomSetFunctionTest, IsValid) {
    CustomSet s1("{a,{b}}"), s2("{a,,b}");
    EXPECT_TRUE(s1.is_valid());
    EXPECT_FALSE(s2.is_valid());
}

TEST(CustomSetFunctionTest, AddElement) {
    CustomSet s("{a}");
    EXPECT_TRUE(s.is_valid());

    s.add_element("x");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "x"}));

    s.add_element("{y,z}");
    EXPECT_TRUE(s.is_valid());
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"a", "x", "{y,z}"}));
}

TEST(CustomSetFunctionTest, AddDuplicateElement) {
    CustomSet s("{a,b}");
    s.add_element("a");
    EXPECT_EQ(s.size(), 2);
}

TEST(CustomSetFunctionTest, RemoveElement) {
    CustomSet s("{a,b,{c,d}}");

    EXPECT_TRUE(s.remove_element("a"));
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"b", "{c,d}"}));

    EXPECT_TRUE(s.remove_element("{c,d}"));
    EXPECT_EQ(s.get_elements(), (std::vector<std::string>{"b"}));

    EXPECT_FALSE(s.remove_element("nonexistent"));
}

TEST(CustomSetFunctionTest, Size) {
    CustomSet s1("{}"), s2("{a}"), s3("{a,b,{c,d}}");
    EXPECT_EQ(s1.size(), 0);
    EXPECT_EQ(s2.size(), 1);
    EXPECT_EQ(s3.size(), 3);
}

TEST(CustomSetFunctionTest, OperatorBracket) {
    CustomSet s("{a,b,{c,d}}");
    EXPECT_TRUE(s["a"]);
    EXPECT_TRUE(s["{c,d}"]);
    EXPECT_FALSE(s["nonexistent"]);
}
TEST(CustomSetFunctionTest, OperatorBracketSubstring) {
    CustomSet s("{abc,def}");

    EXPECT_TRUE(s["abc"]);
    EXPECT_FALSE(s["ab"]);
    EXPECT_FALSE(s["bc"]);
    EXPECT_TRUE(s["def"]);
    EXPECT_FALSE(s["de"]);
    EXPECT_FALSE(s["ef"]);
}

TEST(CustomSetTrimTest, ThroughPublicMethods) {
    CustomSet s;
    s.add_element("  hello  ");
    EXPECT_TRUE(s["hello"]);

    s.add_element(" world  ");
    EXPECT_TRUE(s["world"]);
}

TEST(CustomSetUnionTest, SelfUnion) {

    CustomSet a("{x,y,z}");

    a += a;
    EXPECT_TRUE(a.is_valid());
    EXPECT_EQ(a.size(), 3);

    CustomSet result = a + a;
    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3);
}

TEST(CustomSetUnionTest, ComplexNestedUnion) {
    CustomSet a("{{1,2},3}");
    CustomSet b("{3,{4,5},{1,2}}");

    CustomSet result = a + b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3);
    EXPECT_TRUE(result["{1,2}"]);
    EXPECT_TRUE(result["3"]);
    EXPECT_TRUE(result["{4,5}"]);
}

TEST(CustomSetDifferenceTest, DifferenceOperatorValidSets) {
    CustomSet a("{1,2,3,4,5}");
    CustomSet b("{2,4,6}");

    CustomSet result = a - b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 3);
    EXPECT_TRUE(result["1"]);
    EXPECT_TRUE(result["3"]);
    EXPECT_TRUE(result["5"]);
    EXPECT_FALSE(result["2"]);
    EXPECT_FALSE(result["4"]);
}

TEST(CustomSetIntersectionTest, IntersectionOperatorValidSets) {

    CustomSet a("{apple,banana,cherry}");
    CustomSet b("{banana,cherry,date}");

    CustomSet result = a * b;

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 2);
    EXPECT_TRUE(result["banana"]);
    EXPECT_TRUE(result["cherry"]);
    EXPECT_TRUE(a.is_valid());
    EXPECT_EQ(a.size(), 3);
    EXPECT_TRUE(b.is_valid());
    EXPECT_EQ(b.size(), 3);
}

TEST(CustomSetPowersetTest, PowersetWithNestedSets) {
    CustomSet nested_set("{a,{b,c}}");
    std::vector<CustomSet> result = nested_set.powerset();

    EXPECT_EQ(result.size(), 4);
    bool found_nested = false;
    for (const auto& subset : result) {
        if (subset["{b,c}"]) {
            found_nested = true;
            break;
        }
    }
    EXPECT_TRUE(found_nested);
}
TEST(CustomSetPowersetTest, PowersetFourElements) {
    CustomSet four_elements("{a,b,c,d}");
    std::vector<CustomSet> result = four_elements.powerset();

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

TEST(CustomSetCantorTest, CantorSetWithNestedSets) {
    CustomSet set("{{1,2},3,{4,5},6,{7,8}}");
    CustomSet result = set.cantor_set(1);

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 4);
    EXPECT_TRUE(result["{1,2}"]);
    EXPECT_TRUE(result["3"]);
    EXPECT_TRUE(result["6"]);
    EXPECT_TRUE(result["{7,8}"]);
    EXPECT_FALSE(result["{4,5}"]);
}

TEST(CustomSetCantorTest, CantorSetThreeIterations) {
    CustomSet set("{1,2,3,4,5,6,7,8,9,10,11,12}");
    CustomSet result = set.cantor_set(3);

    EXPECT_TRUE(result.is_valid());
    EXPECT_EQ(result.size(), 4);
}


TEST(CustomSetFunctionTest, ParseCStringThroughConstructor) {
    CustomSet set("{1,2,3}");
    EXPECT_TRUE(set.is_valid());
    EXPECT_EQ(set.get_elements(), (std::vector<std::string>{"1", "2", "3"}));
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
