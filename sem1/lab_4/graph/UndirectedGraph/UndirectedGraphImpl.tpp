#pragma once

#include "UndirectedGraph.h"

template<typename VertexType>
typename UndirectedGraph<VertexType>::size_type
UndirectedGraph<VertexType>::find_vertex_index(const VertexType& vertex) const {
    auto it = vertex_indices.find(vertex);
    if (it == vertex_indices.end())
        throw std::out_of_range("Vertex not found");
    return it->second;
}

template<typename VertexType>
void UndirectedGraph<VertexType>::update_vertex_indices() {
    vertex_indices.clear();
    for (size_type i = 0; i < vertices.size(); ++i) {
        vertex_indices[vertices[i]] = i;
    }
}

template<typename VertexType>
std::pair<typename UndirectedGraph<VertexType>::size_type,
          typename UndirectedGraph<VertexType>::size_type>
UndirectedGraph<VertexType>::get_edge_vertices(size_type edge_index) const {
    size_type first = static_cast<size_type>(-1);
    size_type second = static_cast<size_type>(-1);
    for (size_type i = 0; i < vertices.size(); ++i) {
        if (incidence_matrix[i][edge_index] == 1) {
            if (first == static_cast<size_type>(-1)) {
                first = i;
            } else {
                second = i;
                break;
            }
        }
    }
    return {first, second};
}

// Constructors / assignment
template<typename VertexType>
UndirectedGraph<VertexType>::UndirectedGraph(const UndirectedGraph& other) {
    *this = other;
}

template<typename VertexType>
UndirectedGraph<VertexType>& UndirectedGraph<VertexType>::operator=(const UndirectedGraph& other) {
    if (this != &other) {
        vertices = other.vertices;
        incidence_matrix = other.incidence_matrix;
        vertex_indices = other.vertex_indices;
    }
    return *this;
}

// Core methods
template<typename VertexType>
void UndirectedGraph<VertexType>::clear() {
    vertices.clear();
    incidence_matrix.clear();
    vertex_indices.clear();
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::has_vertex(const VertexType& vertex) const {
    return vertex_indices.find(vertex) != vertex_indices.end();
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::has_edge(const VertexType& v1, const VertexType& v2) const {
    if (!has_vertex(v1) || !has_vertex(v2)) return false;
    size_type i = find_vertex_index(v1);
    size_type j = find_vertex_index(v2);
    for (size_type col = 0; col < edge_count(); ++col) {
        if (incidence_matrix[i][col] == 1 && incidence_matrix[j][col] == 1) {
            return true;
        }
    }
    return false;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::size_type
UndirectedGraph<VertexType>::vertex_degree(const VertexType& vertex) const {
    size_type idx = find_vertex_index(vertex);
    const auto& row = incidence_matrix[idx];
    return static_cast<size_type>(std::count(row.begin(), row.end(), static_cast<char>(1)));
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::size_type
UndirectedGraph<VertexType>::edge_degree(const VertexType& v1, const VertexType& v2) const {
    if (!has_vertex(v1) || !has_vertex(v2))
        throw std::out_of_range("edge_degree: vertex not found");
    return 2;
}

// Addition
template<typename VertexType>
void UndirectedGraph<VertexType>::add_vertex(const VertexType& vertex) {
    if (has_vertex(vertex))
        throw std::invalid_argument("Vertex already exists");
    vertices.push_back(vertex);
    incidence_matrix.emplace_back(edge_count(), 0);
    vertex_indices[vertex] = vertices.size() - 1;
}

template<typename VertexType>
void UndirectedGraph<VertexType>::add_edge(const VertexType& v1, const VertexType& v2) {
    if (!has_vertex(v1) || !has_vertex(v2))
        throw std::out_of_range("add_edge: vertex not found");
    size_type i = find_vertex_index(v1);
    size_type j = find_vertex_index(v2);
    if (i == j)
        throw std::invalid_argument("add_edge: loops are not allowed");
    if (has_edge(v1, v2))
        throw std::invalid_argument("add_edge: edge already exists");

    for (auto& row : incidence_matrix) {
        row.push_back(0);
    }
    incidence_matrix[i].back() = 1;
    incidence_matrix[j].back() = 1;
}

// Removal
template<typename VertexType>
void UndirectedGraph<VertexType>::remove_edge(const VertexType& v1, const VertexType& v2) {
    if (!has_vertex(v1) || !has_vertex(v2))
        throw std::out_of_range("remove_edge: vertex not found");
    size_type i = find_vertex_index(v1);
    size_type j = find_vertex_index(v2);

    size_type found = static_cast<size_type>(-1);
    for (size_type col = 0; col < edge_count(); ++col) {
        if (incidence_matrix[i][col] == 1 && incidence_matrix[j][col] == 1) {
            found = col;
            break;
        }
    }
    if (found == static_cast<size_type>(-1))
        throw std::out_of_range("remove_edge: edge not found");

    for (auto& row : incidence_matrix) {
        row.erase(row.begin() + found);
    }
}

template<typename VertexType>
void UndirectedGraph<VertexType>::remove_vertex(const VertexType& vertex) {
    if (!has_vertex(vertex))
        throw std::out_of_range("remove_vertex: vertex not found");
    size_type vi = find_vertex_index(vertex);

    std::vector<size_type> edges_to_remove;
    for (size_type col = 0; col < edge_count(); ++col) {
        if (incidence_matrix[vi][col] == 1) {
            edges_to_remove.push_back(col);
        }
    }
    std::sort(edges_to_remove.begin(), edges_to_remove.end(), std::greater<size_type>());
    for (size_type col : edges_to_remove) {
        for (auto& row : incidence_matrix) {
            row.erase(row.begin() + col);
        }
    }

    incidence_matrix.erase(incidence_matrix.begin() + vi);
    vertices.erase(vertices.begin() + vi);
    update_vertex_indices();
}