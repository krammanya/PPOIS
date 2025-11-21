#pragma once

#include <vector>
#include <map>
#include <stdexcept>
#include <iterator>
#include <algorithm>
#include <iostream>
#include <memory>

template<typename VertexType>
class UndirectedGraph {
public:
    using value_type = VertexType;
    using reference = VertexType&;
    using const_reference = const VertexType&;
    using pointer = VertexType*;
    using const_pointer = const VertexType*;
    using size_type = size_t;

        // Операторы сравнения
    bool operator==(const UndirectedGraph<VertexType>& other) const;
    bool operator!=(const UndirectedGraph<VertexType>& other) const;
    bool operator<(const UndirectedGraph<VertexType>& other) const;
    bool operator>(const UndirectedGraph<VertexType>& other) const;
    bool operator<=(const UndirectedGraph<VertexType>& other) const;
    bool operator>=(const UndirectedGraph<VertexType>& other) const;

private:
    std::vector<VertexType> vertices;
    std::vector<std::vector<char>> incidence_matrix;
    std::map<VertexType, size_type> vertex_indices;

    size_type find_vertex_index(const VertexType& vertex) const;
    void update_vertex_indices();
    std::pair<size_type, size_type> get_edge_vertices(size_type edge_index) const;

public:
    // === Vertex iterator base ===
    class vertex_iterator_base {
    protected:
        const UndirectedGraph* graph = nullptr;
        size_type current_index = 0;

    public:
        using iterator_category = std::bidirectional_iterator_tag;
        using value_type = VertexType;
        using difference_type = std::ptrdiff_t;
        using pointer = const VertexType*;
        using reference = const VertexType&;

        vertex_iterator_base() = default;
        vertex_iterator_base(const UndirectedGraph* g, size_type idx) : graph(g), current_index(idx) {}

        reference operator*() const;
        pointer operator->() const;

        vertex_iterator_base& operator++();
        vertex_iterator_base operator++(int);
        vertex_iterator_base& operator--();
        vertex_iterator_base operator--(int);

        bool operator==(const vertex_iterator_base& other) const;
        bool operator!=(const vertex_iterator_base& other) const;

        size_type index() const { return current_index; }
    };

    class vertex_iterator : public vertex_iterator_base {
    public:
        vertex_iterator() = default;
        vertex_iterator(const UndirectedGraph* g, size_type idx) : vertex_iterator_base(g, idx) {}
    };

    class const_vertex_iterator : public vertex_iterator_base {
    public:
        const_vertex_iterator() = default;
        const_vertex_iterator(const UndirectedGraph* g, size_type idx) : vertex_iterator_base(g, idx) {}
    };

    // === Edge iterator base ===
    class edge_iterator_base {
    protected:
        const UndirectedGraph* graph = nullptr;
        size_type current_index = 0;

    public:
        using iterator_category = std::bidirectional_iterator_tag;
        using value_type = std::pair<VertexType, VertexType>;
        using difference_type = std::ptrdiff_t;
        using pointer = const value_type*;
        using reference = value_type;

        edge_iterator_base() = default;
        edge_iterator_base(const UndirectedGraph* g, size_type idx) : graph(g), current_index(idx) {}

        reference operator*() const;

        edge_iterator_base& operator++();
        edge_iterator_base operator++(int);
        edge_iterator_base& operator--();
        edge_iterator_base operator--(int);

        bool operator==(const edge_iterator_base& other) const;
        bool operator!=(const edge_iterator_base& other) const;

        size_type index() const { return current_index; }
    };

    class edge_iterator : public edge_iterator_base {
    public:
        edge_iterator() = default;
        edge_iterator(const UndirectedGraph* g, size_type idx) : edge_iterator_base(g, idx) {}
    };

    class const_edge_iterator : public edge_iterator_base {
    public:
        const_edge_iterator() = default;
        const_edge_iterator(const UndirectedGraph* g, size_type idx) : edge_iterator_base(g, idx) {}
    };

    // === Incident edge iterator base ===
    class incident_edge_iterator_base {
    protected:
        const UndirectedGraph* graph = nullptr;
        size_type vertex_index = 0;
        size_type edge_cursor = 0;

        void skip_forward();
        void skip_backward();

    public:
        using iterator_category = std::bidirectional_iterator_tag;
        using value_type = std::pair<VertexType, VertexType>;
        using difference_type = std::ptrdiff_t;

        incident_edge_iterator_base() = default;
        incident_edge_iterator_base(const UndirectedGraph* g, size_type v_idx, size_type start);

        value_type operator*() const;

        incident_edge_iterator_base& operator++();
        incident_edge_iterator_base operator++(int);
        incident_edge_iterator_base& operator--();
        incident_edge_iterator_base operator--(int);

        bool operator==(const incident_edge_iterator_base& other) const;
        bool operator!=(const incident_edge_iterator_base& other) const;
    };

    class incident_edge_iterator : public incident_edge_iterator_base {
    public:
        incident_edge_iterator() = default;
        incident_edge_iterator(const UndirectedGraph* g, size_type v_idx, size_type start)
            : incident_edge_iterator_base(g, v_idx, start) {}
    };

    class const_incident_edge_iterator : public incident_edge_iterator_base {
    public:
        const_incident_edge_iterator() = default;
        const_incident_edge_iterator(const UndirectedGraph* g, size_type v_idx, size_type start)
            : incident_edge_iterator_base(g, v_idx, start) {}
    };

    // === Adjacent vertex iterator base ===
    class adjacent_vertex_iterator_base {
    protected:
        const UndirectedGraph* graph = nullptr;
        size_type vertex_index = 0;
        size_type edge_cursor = 0;

        void skip_forward();
        void skip_backward();

    public:
        using iterator_category = std::bidirectional_iterator_tag;
        using value_type = VertexType;
        using difference_type = std::ptrdiff_t;
        using reference = const VertexType&;
        using pointer = const VertexType*;

        adjacent_vertex_iterator_base() = default;
        adjacent_vertex_iterator_base(const UndirectedGraph* g, size_type v_idx, size_type start);

        reference operator*() const;
        pointer operator->() const;

        adjacent_vertex_iterator_base& operator++();
        adjacent_vertex_iterator_base operator++(int);
        adjacent_vertex_iterator_base& operator--();
        adjacent_vertex_iterator_base operator--(int);

        bool operator==(const adjacent_vertex_iterator_base& other) const;
        bool operator!=(const adjacent_vertex_iterator_base& other) const;
    };

    class adjacent_vertex_iterator : public adjacent_vertex_iterator_base {
    public:
        adjacent_vertex_iterator() = default;
        adjacent_vertex_iterator(const UndirectedGraph* g, size_type v_idx, size_type start)
            : adjacent_vertex_iterator_base(g, v_idx, start) {}
    };

    class const_adjacent_vertex_iterator : public adjacent_vertex_iterator_base {
    public:
        const_adjacent_vertex_iterator() = default;
        const_adjacent_vertex_iterator(const UndirectedGraph* g, size_type v_idx, size_type start)
            : adjacent_vertex_iterator_base(g, v_idx, start) {}
    };

    // === Constructors / destructor ===
    UndirectedGraph() = default;
    UndirectedGraph(const UndirectedGraph& other);
    UndirectedGraph& operator=(const UndirectedGraph& other);
    ~UndirectedGraph() = default;

    // Core
    bool empty() const { return vertices.empty(); }
    void clear();

    // Access
    bool has_vertex(const VertexType& vertex) const;
    bool has_edge(const VertexType& v1, const VertexType& v2) const;
    size_type vertex_count() const { return vertices.size(); }
    size_type edge_count() const { return incidence_matrix.empty() ? 0 : incidence_matrix[0].size(); }
    size_type vertex_degree(const VertexType& vertex) const;
    size_type edge_degree(const VertexType& v1, const VertexType& v2) const;

    // Modification
    void add_vertex(const VertexType& vertex);
    void add_edge(const VertexType& v1, const VertexType& v2);
    void remove_edge(const VertexType& v1, const VertexType& v2);
    void remove_vertex(const VertexType& vertex);

    // Vertex iterators
    vertex_iterator vertex_begin();
    vertex_iterator vertex_end();
    const_vertex_iterator vertex_begin() const;
    const_vertex_iterator vertex_end() const;

    // Edge iterators
    edge_iterator edge_begin();
    edge_iterator edge_end();
    const_edge_iterator edge_begin() const;
    const_edge_iterator edge_end() const;

    // Incident edge iterators
    incident_edge_iterator incident_edges_begin(const VertexType& vertex);
    incident_edge_iterator incident_edges_end(const VertexType& vertex);
    const_incident_edge_iterator incident_edges_begin(const VertexType& vertex) const;
    const_incident_edge_iterator incident_edges_end(const VertexType& vertex) const;

    // Adjacent vertex iterators
    adjacent_vertex_iterator adjacent_vertices_begin(const VertexType& vertex);
    adjacent_vertex_iterator adjacent_vertices_end(const VertexType& vertex);
    const_adjacent_vertex_iterator adjacent_vertices_begin(const VertexType& vertex) const;
    const_adjacent_vertex_iterator adjacent_vertices_end(const VertexType& vertex) const;

    // Reverse iterators (defined inline via std::reverse_iterator)
    std::reverse_iterator<vertex_iterator> vertex_rbegin() { return std::reverse_iterator(vertex_end()); }
    std::reverse_iterator<vertex_iterator> vertex_rend() { return std::reverse_iterator(vertex_begin()); }
    std::reverse_iterator<const_vertex_iterator> vertex_rbegin() const { return std::reverse_iterator(vertex_end()); }
    std::reverse_iterator<const_vertex_iterator> vertex_rend() const { return std::reverse_iterator(vertex_begin()); }

    std::reverse_iterator<edge_iterator> edge_rbegin() { return std::reverse_iterator(edge_end()); }
    std::reverse_iterator<edge_iterator> edge_rend() { return std::reverse_iterator(edge_begin()); }
    std::reverse_iterator<const_edge_iterator> edge_rbegin() const { return std::reverse_iterator(edge_end()); }
    std::reverse_iterator<const_edge_iterator> edge_rend() const { return std::reverse_iterator(edge_begin()); }

    std::reverse_iterator<incident_edge_iterator> incident_edges_rbegin(const VertexType& v) { return std::reverse_iterator(incident_edges_end(v)); }
    std::reverse_iterator<incident_edge_iterator> incident_edges_rend(const VertexType& v) { return std::reverse_iterator(incident_edges_begin(v)); }
    std::reverse_iterator<const_incident_edge_iterator> incident_edges_rbegin(const VertexType& v) const { return std::reverse_iterator(incident_edges_end(v)); }
    std::reverse_iterator<const_incident_edge_iterator> incident_edges_rend(const VertexType& v) const { return std::reverse_iterator(incident_edges_begin(v)); }

    std::reverse_iterator<adjacent_vertex_iterator> adjacent_vertices_rbegin(const VertexType& v) { return std::reverse_iterator(adjacent_vertices_end(v)); }
    std::reverse_iterator<adjacent_vertex_iterator> adjacent_vertices_rend(const VertexType& v) { return std::reverse_iterator(adjacent_vertices_begin(v)); }
    std::reverse_iterator<const_adjacent_vertex_iterator> adjacent_vertices_rbegin(const VertexType& v) const { return std::reverse_iterator(adjacent_vertices_end(v)); }
    std::reverse_iterator<const_adjacent_vertex_iterator> adjacent_vertices_rend(const VertexType& v) const { return std::reverse_iterator(adjacent_vertices_begin(v)); }

    // Iterator-based removal
    vertex_iterator remove_vertex(vertex_iterator it);
    edge_iterator remove_edge(edge_iterator it);
};

// Включаем реализации
#include "UndirectedGraphImpl.tpp"
#include "UndirectedGraphIterators.tpp"
#include "UndirectedGraphIo.tpp"
