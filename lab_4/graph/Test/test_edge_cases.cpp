#include <gtest/gtest.h>
#include <stdexcept>
#include "../UndirectedGraph/UndirectedGraph.h" 

TEST(EdgeCases, DuplicateVertices) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    EXPECT_THROW(graph.add_vertex(1), std::invalid_argument);
}

TEST(EdgeCases, DuplicateEdges) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_edge(1, 2);
    EXPECT_THROW(graph.add_edge(1, 2), std::invalid_argument);
    EXPECT_THROW(graph.add_edge(2, 1), std::invalid_argument);  // Обратное тоже дубликат
}

TEST(EdgeCases, SelfLoops) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    EXPECT_THROW(graph.add_edge(1, 1), std::invalid_argument);
}


TEST(EdgeCases, CopyConstructor) {
    UndirectedGraph<int> graph1;
    
    graph1.add_vertex(1);
    graph1.add_vertex(2);
    graph1.add_vertex(3);
    graph1.add_edge(1, 2);
    graph1.add_edge(2, 3);
    
    // Конструктор копирования
    UndirectedGraph<int> graph2 = graph1;
    
    EXPECT_EQ(graph2.vertex_count(), 3);
    EXPECT_EQ(graph2.edge_count(), 2);
    EXPECT_TRUE(graph2.has_vertex(1));
    EXPECT_TRUE(graph2.has_vertex(2));
    EXPECT_TRUE(graph2.has_vertex(3));
    EXPECT_TRUE(graph2.has_edge(1, 2));
    EXPECT_TRUE(graph2.has_edge(2, 3));
    
    // Проверяем, что это глубокое копирование
    graph2.remove_vertex(1);
    EXPECT_FALSE(graph2.has_vertex(1));
    EXPECT_TRUE(graph1.has_vertex(1));  // Оригинал не должен измениться
}

TEST(EdgeCases, AssignmentOperator) {
    UndirectedGraph<int> graph1;
    
    graph1.add_vertex(1);
    graph1.add_vertex(2);
    graph1.add_edge(1, 2);
    
    UndirectedGraph<int> graph2;
    graph2.add_vertex(10);
    graph2.add_vertex(20);
    graph2.add_edge(10, 20);
    
    // Оператор присваивания
    graph2 = graph1;
    
    EXPECT_EQ(graph2.vertex_count(), 2);
    EXPECT_EQ(graph2.edge_count(), 1);
    EXPECT_TRUE(graph2.has_vertex(1));
    EXPECT_TRUE(graph2.has_vertex(2));
    EXPECT_TRUE(graph2.has_edge(1, 2));
    EXPECT_FALSE(graph2.has_vertex(10));  // Старые данные должны быть удалены
}

TEST(EdgeCases, ComplexVertexTypes) {
    struct Person {
        std::string name;
        int age;
        
        bool operator==(const Person& other) const {
            return name == other.name && age == other.age;
        }
        
        bool operator<(const Person& other) const {
            return name < other.name;
        }
    };
    
    UndirectedGraph<Person> graph;
    
    Person alice{"Alice", 25};
    Person bob{"Bob", 30};
    Person charlie{"Charlie", 35};
    
    graph.add_vertex(alice);
    graph.add_vertex(bob);
    graph.add_vertex(charlie);
    
    graph.add_edge(alice, bob);
    graph.add_edge(bob, charlie);
    
    EXPECT_TRUE(graph.has_vertex(alice));
    EXPECT_TRUE(graph.has_vertex(bob));
    EXPECT_TRUE(graph.has_vertex(charlie));
    EXPECT_TRUE(graph.has_edge(alice, bob));
    EXPECT_TRUE(graph.has_edge(bob, charlie));
    EXPECT_FALSE(graph.has_edge(alice, charlie));
    
    EXPECT_EQ(graph.vertex_degree(alice), 1);
    EXPECT_EQ(graph.vertex_degree(bob), 2);
    EXPECT_EQ(graph.vertex_degree(charlie), 1);
}

TEST(EdgeCases, StringVertices) {
    UndirectedGraph<std::string> graph;
    
    graph.add_vertex("Moscow");
    graph.add_vertex("St Petersburg");
    graph.add_vertex("Novosibirsk");
    
    graph.add_edge("Moscow", "St Petersburg");
    graph.add_edge("St Petersburg", "Novosibirsk");
    
    EXPECT_TRUE(graph.has_vertex("Moscow"));
    EXPECT_TRUE(graph.has_edge("Moscow", "St Petersburg"));
    EXPECT_FALSE(graph.has_edge("Moscow", "Novosibirsk"));
    
    // Проверяем итераторы со строками
    std::set<std::string> cities;
    for (auto it = graph.vertex_begin(); it != graph.vertex_end(); ++it) {
        cities.insert(*it);
    }
    
    EXPECT_EQ(cities.size(), 3);
    EXPECT_TRUE(cities.find("Moscow") != cities.end());
    EXPECT_TRUE(cities.find("St Petersburg") != cities.end());
    EXPECT_TRUE(cities.find("Novosibirsk") != cities.end());
}

TEST(EdgeCases, IteratorRemoveVertex) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    auto it = graph.vertex_begin();
    int first_value = *it;
    
    // Удаляем вершину, на которую НЕ указывает итератор
    graph.remove_vertex(2);
    
    // Итератор должен остаться валидным
    EXPECT_EQ(*it, first_value);
    EXPECT_EQ(graph.vertex_count(), 2);
}

TEST(EdgeCases, IteratorRemoveEdge) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    auto it = graph.edge_begin();
    auto first_edge = *it;
    
    // Удаляем ребро, на которое НЕ указывает итератор
    if (first_edge == std::make_pair(1, 2)) {
        graph.remove_edge(2, 3);
    } else {
        graph.remove_edge(1, 2);
    }
    
    // Итератор должен остаться валидным
    EXPECT_EQ(*it, first_edge);
    EXPECT_EQ(graph.edge_count(), 1);
}