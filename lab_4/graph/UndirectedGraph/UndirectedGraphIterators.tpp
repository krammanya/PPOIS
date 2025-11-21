#pragma once

#include "UndirectedGraph.h"

// === vertex_iterator_base methods ===

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator_base::reference
UndirectedGraph<VertexType>::edge_iterator_base::operator*() const {
    auto vertices_pair = graph->get_edge_vertices(current_index);
    return reference{
        graph->vertices[vertices_pair.first],
        graph->vertices[vertices_pair.second]
    };
}

template<typename VertexType>
const VertexType*
UndirectedGraph<VertexType>::vertex_iterator_base::operator->() const {
    return &graph->vertices[current_index];
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator_base&
UndirectedGraph<VertexType>::vertex_iterator_base::operator++() {
    ++current_index;
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator_base
UndirectedGraph<VertexType>::vertex_iterator_base::operator++(int) {
    vertex_iterator_base tmp = *this;
    ++(*this);
    return tmp;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator_base&
UndirectedGraph<VertexType>::vertex_iterator_base::operator--() {
    --current_index;
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator_base
UndirectedGraph<VertexType>::vertex_iterator_base::operator--(int) {
    vertex_iterator_base tmp = *this;
    --(*this);
    return tmp;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::vertex_iterator_base::operator==(
    const vertex_iterator_base& other) const {
    return graph == other.graph && current_index == other.current_index;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::vertex_iterator_base::operator!=(
    const vertex_iterator_base& other) const {
    return !(*this == other);
}

// === edge_iterator_base methods ===


template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator_base&
UndirectedGraph<VertexType>::edge_iterator_base::operator++() {
    ++current_index;
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator_base
UndirectedGraph<VertexType>::edge_iterator_base::operator++(int) {
    edge_iterator_base tmp = *this;
    ++(*this);
    return tmp;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator_base&
UndirectedGraph<VertexType>::edge_iterator_base::operator--() {
    --current_index;
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator_base
UndirectedGraph<VertexType>::edge_iterator_base::operator--(int) {
    edge_iterator_base tmp = *this;
    --(*this);
    return tmp;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::edge_iterator_base::operator==(
    const edge_iterator_base& other) const {
    return graph == other.graph && current_index == other.current_index;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::edge_iterator_base::operator!=(
    const edge_iterator_base& other) const {
    return !(*this == other);
}

// === Helper functions for incident/adjacent iterators ===
template<typename VertexType>
void UndirectedGraph<VertexType>::incident_edge_iterator_base::skip_forward() {
    while (edge_cursor < graph->edge_count()) {
        if (graph->incidence_matrix[vertex_index][edge_cursor] == 1) break;
        ++edge_cursor;
    }
}

template<typename VertexType>
void UndirectedGraph<VertexType>::incident_edge_iterator_base::skip_backward() {
    while (true) {
        if (edge_cursor >= graph->edge_count()) {
            if (graph->edge_count() == 0) break;
            edge_cursor = graph->edge_count() - 1;
        }
        if (graph->incidence_matrix[vertex_index][edge_cursor] == 1) break;
        if (edge_cursor == 0) { edge_cursor = graph->edge_count(); break; }
        --edge_cursor;
    }
}

template<typename VertexType>
UndirectedGraph<VertexType>::incident_edge_iterator_base::incident_edge_iterator_base(
    const UndirectedGraph* g, size_type v_idx, size_type start)
    : graph(g), vertex_index(v_idx), edge_cursor(start) {
    skip_forward();
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator_base::value_type
UndirectedGraph<VertexType>::incident_edge_iterator_base::operator*() const {
    auto vertices_pair = graph->get_edge_vertices(edge_cursor);
    return {
        graph->vertices[vertices_pair.first],
        graph->vertices[vertices_pair.second]
    };
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator_base&
UndirectedGraph<VertexType>::incident_edge_iterator_base::operator++() {
    ++edge_cursor;
    skip_forward();
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator_base
UndirectedGraph<VertexType>::incident_edge_iterator_base::operator++(int) {
    incident_edge_iterator_base tmp = *this;
    ++(*this);
    return tmp;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator_base&
UndirectedGraph<VertexType>::incident_edge_iterator_base::operator--() {
    if (edge_cursor == 0) edge_cursor = graph->edge_count();
    --edge_cursor;
    skip_backward();
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator_base
UndirectedGraph<VertexType>::incident_edge_iterator_base::operator--(int) {
    incident_edge_iterator_base tmp = *this;
    --(*this);
    return tmp;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::incident_edge_iterator_base::operator==(
    const incident_edge_iterator_base& other) const {
    return graph == other.graph && vertex_index == other.vertex_index && edge_cursor == other.edge_cursor;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::incident_edge_iterator_base::operator!=(
    const incident_edge_iterator_base& other) const {
    return !(*this == other);
}

// === adjacent_vertex_iterator_base ===
template<typename VertexType>
void UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::skip_forward() {
    while (edge_cursor < graph->edge_count()) {
        if (graph->incidence_matrix[vertex_index][edge_cursor] == 1) break;
        ++edge_cursor;
    }
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator_base::reference
UndirectedGraph<VertexType>::vertex_iterator_base::operator*() const {
    return graph->vertices[current_index];
}

template<typename VertexType>
void UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::skip_backward() {
    while (true) {
        if (edge_cursor >= graph->edge_count()) {
            if (graph->edge_count() == 0) break;
            edge_cursor = graph->edge_count() - 1;
        }
        if (graph->incidence_matrix[vertex_index][edge_cursor] == 1) break;
        if (edge_cursor == 0) { edge_cursor = graph->edge_count(); break; }
        --edge_cursor;
    }
}

template<typename VertexType>
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::adjacent_vertex_iterator_base(
    const UndirectedGraph* g, size_type v_idx, size_type start)
    : graph(g), vertex_index(v_idx), edge_cursor(start) {
    skip_forward();
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::reference
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator*() const {
    auto vertices_pair = graph->get_edge_vertices(edge_cursor);
    size_type other = (vertices_pair.first == vertex_index ? vertices_pair.second : vertices_pair.first);
    return graph->vertices[other];
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::pointer
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator->() const {
    auto vertices_pair = graph->get_edge_vertices(edge_cursor);
    size_type other = (vertices_pair.first == vertex_index ? vertices_pair.second : vertices_pair.first);
    return &graph->vertices[other];
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base&
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator++() {
    ++edge_cursor;
    skip_forward();
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator++(int) {
    adjacent_vertex_iterator_base tmp = *this;
    ++(*this);
    return tmp;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base&
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator--() {
    if (edge_cursor == 0) edge_cursor = graph->edge_count();
    --edge_cursor;
    skip_backward();
    return *this;
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator_base
UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator--(int) {
    adjacent_vertex_iterator_base tmp = *this;
    --(*this);
    return tmp;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator==(
    const adjacent_vertex_iterator_base& other) const {
    return graph == other.graph && vertex_index == other.vertex_index && edge_cursor == other.edge_cursor;
}

template<typename VertexType>
bool UndirectedGraph<VertexType>::adjacent_vertex_iterator_base::operator!=(
    const adjacent_vertex_iterator_base& other) const {
    return !(*this == other);
}

// === Iterator access methods ===
template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator
UndirectedGraph<VertexType>::vertex_begin() {
    return vertex_iterator(this, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator
UndirectedGraph<VertexType>::vertex_end() {
    return vertex_iterator(this, vertices.size());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_vertex_iterator
UndirectedGraph<VertexType>::vertex_begin() const {
    return const_vertex_iterator(this, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_vertex_iterator
UndirectedGraph<VertexType>::vertex_end() const {
    return const_vertex_iterator(this, vertices.size());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator
UndirectedGraph<VertexType>::edge_begin() {
    return edge_iterator(this, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator
UndirectedGraph<VertexType>::edge_end() {
    return edge_iterator(this, edge_count());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_edge_iterator
UndirectedGraph<VertexType>::edge_begin() const {
    return const_edge_iterator(this, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_edge_iterator
UndirectedGraph<VertexType>::edge_end() const {
    return const_edge_iterator(this, edge_count());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator
UndirectedGraph<VertexType>::incident_edges_begin(const VertexType& vertex) {
    size_type vi = find_vertex_index(vertex);
    return incident_edge_iterator(this, vi, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::incident_edge_iterator
UndirectedGraph<VertexType>::incident_edges_end(const VertexType& vertex) {
    size_type vi = find_vertex_index(vertex);
    return incident_edge_iterator(this, vi, edge_count());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_incident_edge_iterator
UndirectedGraph<VertexType>::incident_edges_begin(const VertexType& vertex) const {
    size_type vi = find_vertex_index(vertex);
    return const_incident_edge_iterator(this, vi, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_incident_edge_iterator
UndirectedGraph<VertexType>::incident_edges_end(const VertexType& vertex) const {
    size_type vi = find_vertex_index(vertex);
    return const_incident_edge_iterator(this, vi, edge_count());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator
UndirectedGraph<VertexType>::adjacent_vertices_begin(const VertexType& vertex) {
    size_type vi = find_vertex_index(vertex);
    return adjacent_vertex_iterator(this, vi, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::adjacent_vertex_iterator
UndirectedGraph<VertexType>::adjacent_vertices_end(const VertexType& vertex) {
    size_type vi = find_vertex_index(vertex);
    return adjacent_vertex_iterator(this, vi, edge_count());
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_adjacent_vertex_iterator
UndirectedGraph<VertexType>::adjacent_vertices_begin(const VertexType& vertex) const {
    size_type vi = find_vertex_index(vertex);
    return const_adjacent_vertex_iterator(this, vi, 0);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::const_adjacent_vertex_iterator
UndirectedGraph<VertexType>::adjacent_vertices_end(const VertexType& vertex) const {
    size_type vi = find_vertex_index(vertex);
    return const_adjacent_vertex_iterator(this, vi, edge_count());
}

// === Iterator-based removal ===
template<typename VertexType>
typename UndirectedGraph<VertexType>::vertex_iterator
UndirectedGraph<VertexType>::remove_vertex(vertex_iterator it) {
    if (it.index() >= vertices.size())
        throw std::out_of_range("remove_vertex(iterator): invalid iterator");
    VertexType val = vertices[it.index()];
    remove_vertex(val);
    size_type new_idx = (it.index() < vertices.size() ? it.index() : vertices.size());
    return vertex_iterator(this, new_idx);
}

template<typename VertexType>
typename UndirectedGraph<VertexType>::edge_iterator
UndirectedGraph<VertexType>::remove_edge(edge_iterator it) {
    if (it.index() >= edge_count())
        throw std::out_of_range("remove_edge(iterator): invalid iterator");
    size_type ei = it.index();
    for (auto& row : incidence_matrix) {
        row.erase(row.begin() + ei);
    }
    size_type new_idx = (ei < edge_count() ? ei : edge_count());
    return edge_iterator(this, new_idx);
}