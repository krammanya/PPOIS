#include <gtest/gtest.h>
#include <algorithm>
#include <numeric>
#include <vector>
#include <set>
#include "../UndirectedGraph/UndirectedGraph.h"

TEST(Algorithms, STLFindVertex) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(10);
    graph.add_vertex(20);
    graph.add_vertex(30);
    
    auto it = std::find(graph.vertex_begin(), graph.vertex_end(), 20);
    EXPECT_NE(it, graph.vertex_end());
    EXPECT_EQ(*it, 20);
    
    it = std::find(graph.vertex_begin(), graph.vertex_end(), 40);
    EXPECT_EQ(it, graph.vertex_end());
}

TEST(Algorithms, STLCountVertices) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    auto count = std::count(graph.vertex_begin(), graph.vertex_end(), 2);
    EXPECT_EQ(count, 1);
}

TEST(Algorithms, STLForEachVertex) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    std::vector<int> visited;
    std::for_each(graph.vertex_begin(), graph.vertex_end(), 
                 [&visited](int v) { visited.push_back(v * 2); });
    
    EXPECT_EQ(visited.size(), 3);
    EXPECT_TRUE(std::find(visited.begin(), visited.end(), 2) != visited.end());
    EXPECT_TRUE(std::find(visited.begin(), visited.end(), 4) != visited.end());
    EXPECT_TRUE(std::find(visited.begin(), visited.end(), 6) != visited.end());
}

TEST(Algorithms, STLAccumulateVertices) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    int sum = std::accumulate(graph.vertex_begin(), graph.vertex_end(), 0);
    EXPECT_EQ(sum, 6);
    
    int product = std::accumulate(graph.vertex_begin(), graph.vertex_end(), 1, 
                                 [](int a, int b) { return a * b; });
    EXPECT_EQ(product, 6);
}

TEST(Algorithms, STLCountEdges) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    int edge_count = 0;
    std::for_each(graph.edge_begin(), graph.edge_end(),
                 [&edge_count](const auto& edge) { ++edge_count; });
    
    EXPECT_EQ(edge_count, 2);
}

TEST(Algorithms, STLFindEdge) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    graph.add_edge(1, 2);
    graph.add_edge(2, 3);
    
    // Ищем ребро 1-2 (может быть представлено как (1,2) или (2,1))
    auto it = std::find_if(graph.edge_begin(), graph.edge_end(),
                          [](const auto& edge) {
                              return (edge.first == 1 && edge.second == 2) ||
                                     (edge.first == 2 && edge.second == 1);
                          });
    
    EXPECT_NE(it, graph.edge_end());
    
    auto found_edge = *it;
    // Проверяем, что нашли именно ребро между 1 и 2
    EXPECT_TRUE((found_edge.first == 1 && found_edge.second == 2) ||
                (found_edge.first == 2 && found_edge.second == 1));
}

TEST(Algorithms, STLTransformVertices) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    std::vector<int> squared_vertices;
    std::transform(graph.vertex_begin(), graph.vertex_end(),
                  std::back_inserter(squared_vertices),
                  [](int v) { return v * v; });
    
    EXPECT_EQ(squared_vertices.size(), 3);
    EXPECT_TRUE(std::find(squared_vertices.begin(), squared_vertices.end(), 1) != squared_vertices.end());
    EXPECT_TRUE(std::find(squared_vertices.begin(), squared_vertices.end(), 4) != squared_vertices.end());
    EXPECT_TRUE(std::find(squared_vertices.begin(), squared_vertices.end(), 9) != squared_vertices.end());
}

TEST(Algorithms, STLAnyAllNone) {
    UndirectedGraph<int> graph;
    
    graph.add_vertex(1);
    graph.add_vertex(2);
    graph.add_vertex(3);
    
    bool any_gt_2 = std::any_of(graph.vertex_begin(), graph.vertex_end(),
                               [](int v) { return v > 2; });
    EXPECT_TRUE(any_gt_2);
    
    bool all_gt_0 = std::all_of(graph.vertex_begin(), graph.vertex_end(),
                               [](int v) { return v > 0; });
    EXPECT_TRUE(all_gt_0);
    
    bool none_gt_10 = std::none_of(graph.vertex_begin(), graph.vertex_end(),
                                  [](int v) { return v > 10; });
    EXPECT_TRUE(none_gt_10);
}