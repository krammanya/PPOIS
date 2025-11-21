#include <gtest/gtest.h>
#include <algorithm>
#include <vector>
#include <set>
#include "../UndirectedGraph/UndirectedGraph.h"

TEST(Iterators, VertexIterator) {
    UndirectedGraph<int> graph;
    std::vector<int> expected = {1, 3, 2, 4};  // В порядке добавления
    
    for (int v : expected) {
        graph.add_vertex(v);
    }
    
    std::vector<int> actual;
    for (auto it = graph.vertex_begin(); it != graph.vertex_end(); ++it) {
        actual.push_back(*it);
    }
    
    EXPECT_EQ(actual, expected);
}

TEST(Iterators, ConstVertexIterator) {
    UndirectedGraph<std::string> graph;
    graph.add_vertex("A");
    graph.add_vertex("B");
    
    const auto& const_graph = graph;
    std::vector<std::string> vertices;
    
    for (auto it = const_graph.vertex_begin(); it != const_graph.vertex_end(); ++it) {
        vertices.push_back(*it);
    }
    
    EXPECT_EQ(vertices.size(), 2);
    EXPECT_TRUE(std::find(vertices.begin(), vertices.end(), "A") != vertices.end());
    EXPECT_TRUE(std::find(vertices.begin(), vertices.end(), "B") != vertices.end());
}

TEST(Iterators, EdgeIterator) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    std::vector<std::pair<int, int>> edges;
    for (auto it = graph.edge_begin(); it != graph.edge_end(); ++it) {
        edges.push_back(*it);
    }
    
    EXPECT_EQ(edges.size(), 2);
    
    // Проверяем, что все возвращенные ребра действительно существуют
    for (const auto& edge : edges) {
        EXPECT_TRUE(graph.has_edge(edge.first, edge.second));
    }
}

TEST(Iterators, ConstEdgeIterator) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_edge(1, 2);
    
    const auto& const_graph = graph;
    std::vector<std::pair<int, int>> edges;
    
    for (auto it = const_graph.edge_begin(); it != const_graph.edge_end(); ++it) {
        edges.push_back(*it);
    }
    
    EXPECT_EQ(edges.size(), 1);
    EXPECT_EQ(edges[0].first, 1);
    EXPECT_EQ(edges[0].second, 2);
}

TEST(Iterators, IncidentEdgeIterator) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_vertex(4);
    graph.add_edge(1, 2);
    graph.add_edge(1, 3);
    graph.add_edge(1, 4);
    graph.add_edge(2, 3);  // Ребро не инцидентное вершине 1
    
    std::vector<std::pair<int, int>> incident_edges;
    for (auto it = graph.incident_edges_begin(1); it != graph.incident_edges_end(1); ++it) {
        incident_edges.push_back(*it);
    }
    
    EXPECT_EQ(incident_edges.size(), 3);
    
    // Проверяем, что все инцидентные ребра содержат вершину 1
    for (const auto& edge : incident_edges) {
        EXPECT_TRUE(edge.first == 1 || edge.second == 1);
    }
}

TEST(Iterators, AdjacentVertexIterator) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_vertex(4);
    graph.add_edge(1, 2);
    graph.add_edge(1, 3);
    graph.add_edge(1, 4);
    
    std::set<int> neighbors;
    for (auto it = graph.adjacent_vertices_begin(1); it != graph.adjacent_vertices_end(1); ++it) {
        neighbors.insert(*it);
    }
    
    EXPECT_EQ(neighbors.size(), 3);
    EXPECT_TRUE(neighbors.find(2) != neighbors.end());
    EXPECT_TRUE(neighbors.find(3) != neighbors.end());
    EXPECT_TRUE(neighbors.find(4) != neighbors.end());
    EXPECT_TRUE(neighbors.find(1) == neighbors.end());  // Сама вершина не должна быть соседней
}

TEST(Iterators, ReverseIterators) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    std::vector<int> forward, reverse;
    
    for (auto it = graph.vertex_begin(); it != graph.vertex_end(); ++it) {
        forward.push_back(*it);
    }
    
    for (auto it = graph.vertex_rbegin(); it != graph.vertex_rend(); ++it) {
        reverse.push_back(*it);
    }
    
    EXPECT_EQ(forward.size(), reverse.size());
    EXPECT_EQ(forward.size(), 3);
    
    // Проверяем обратный порядок
    EXPECT_EQ(forward[0], reverse[2]);
    EXPECT_EQ(forward[1], reverse[1]);
    EXPECT_EQ(forward[2], reverse[0]);
}

TEST(Iterators, EmptyIterators) {
    UndirectedGraph<int> graph;
    
    // Для пустого графа итераторы начала и конца должны быть равны
    EXPECT_EQ(graph.vertex_begin(), graph.vertex_end());
    EXPECT_EQ(graph.edge_begin(), graph.edge_end());
    
    graph.add_vertex(1);
    // Для вершины без рёбер итераторы инцидентных рёбер должны быть равны
    EXPECT_EQ(graph.incident_edges_begin(1), graph.incident_edges_end(1));
    EXPECT_EQ(graph.adjacent_vertices_begin(1), graph.adjacent_vertices_end(1));
}