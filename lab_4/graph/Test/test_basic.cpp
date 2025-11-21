#include <gtest/gtest.h>
#include "../UndirectedGraph/UndirectedGraph.h"
#include <stdexcept>

TEST(BasicOperations, EmptyGraph) {
    UndirectedGraph<int> graph;
    EXPECT_TRUE(graph.empty());
    EXPECT_EQ(graph.vertex_count(), 0);
    EXPECT_EQ(graph.edge_count(), 0);
}

TEST(BasicOperations, AddVertices) {
    UndirectedGraph<std::string> graph;
    
    graph.add_vertex("A");
    graph.add_vertex("B");
    graph.add_vertex("C");
    
    EXPECT_FALSE(graph.empty());
    EXPECT_EQ(graph.vertex_count(), 3);
    EXPECT_TRUE(graph.has_vertex("A"));
    EXPECT_TRUE(graph.has_vertex("B"));
    EXPECT_TRUE(graph.has_vertex("C"));
    EXPECT_FALSE(graph.has_vertex("D"));
}

TEST(BasicOperations, AddEdges) {
    UndirectedGraph<int> graph;
    
    for (int i = 1; i <= 4; ++i) {
        graph.add_vertex(i);
    }
    
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    graph.add_edge(3, 4);
    graph.add_edge(4, 1);
    
    EXPECT_EQ(graph.edge_count(), 4);
    EXPECT_TRUE(graph.has_edge(1, 2));
    EXPECT_TRUE(graph.has_edge(2, 1));  // Проверяем симметричность
    EXPECT_FALSE(graph.has_edge(1, 3));
}

TEST(BasicOperations, VertexDegree) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(1, 3);
    
    EXPECT_EQ(graph.vertex_degree(1), 2);
    EXPECT_EQ(graph.vertex_degree(2), 1);
    EXPECT_EQ(graph.vertex_degree(3), 1);
}

TEST(BasicOperations, EdgeDegree) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_edge(1, 2);
    
    EXPECT_EQ(graph.edge_degree(1, 2), 2);  // Всегда 2 для неориентированного графа
}

TEST(BasicOperations, RemoveVertices) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    graph.remove_vertex(2);
    
    EXPECT_EQ(graph.vertex_count(), 2);
    EXPECT_FALSE(graph.has_vertex(2));
    EXPECT_FALSE(graph.has_edge(1, 2));
    EXPECT_FALSE(graph.has_edge(2, 3));
    EXPECT_EQ(graph.edge_count(), 0);
}

TEST(BasicOperations, RemoveEdges) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    graph.remove_edge(1, 2);
    
    EXPECT_EQ(graph.vertex_count(), 3);
    EXPECT_EQ(graph.edge_count(), 1);
    EXPECT_FALSE(graph.has_edge(1, 2));
    EXPECT_TRUE(graph.has_edge(2, 3));
}

TEST(BasicOperations, ClearGraph) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_edge(1, 2);
    
    graph.clear();
    
    EXPECT_TRUE(graph.empty());
    EXPECT_EQ(graph.vertex_count(), 0);
    EXPECT_EQ(graph.edge_count(), 0);
}