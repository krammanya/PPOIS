#pragma once

#include "UndirectedGraph.h"

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator==(const UndirectedGraph& other) const {
    return vertices == other.vertices && incidence_matrix == other.incidence_matrix;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator!=(const UndirectedGraph& other) const {
    return !(*this == other);
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator<(const UndirectedGraph& other) const {
    if (vertex_count() != other.vertex_count())
        return vertex_count() < other.vertex_count();
    return edge_count() < other.edge_count();
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator>(const UndirectedGraph& other) const {
    return other < *this;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator<=(const UndirectedGraph& other) const {
    return !(*this > other);
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::operator>=(const UndirectedGraph& other) const {
    return !(*this < other);
}

template<typename VertexType>
std::ostream& operator<<(std::ostream& os, const UndirectedGraph<VertexType>& g) {
    os << "Vertices: ";
    std::for_each(g.vertex_begin(), g.vertex_end(), [&os](const VertexType& v) {
        os << v << " ";
    });
    os << "\nEdges: ";
    std::for_each(g.edge_begin(), g.edge_end(), [&os](const auto& edge) {
        os << "(" << edge.first << "-" << edge.second << ") ";
    });
    return os;
}