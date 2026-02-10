#ifndef TOURNAMENTSORT_H
#define TOURNAMENTSORT_H

#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>
#include <climits>

using namespace std;

template<typename T>
class TombSort {
private:
    function<bool(const T&, const T&)> comp;
    
public:
    TombSort(function<bool(const T&, const T&)> comp = less<T>()) : comp(comp) {}
    
    void sort(vector<T>& arr) {
        int n = arr.size();
        if (n <= 1) return;
        
        int treeSize = 1;
        while (treeSize < n) treeSize <<= 1;
        
        vector<int> tree(2 * treeSize, -1);
        
        for (int i = 0; i < n; i++) {
            tree[treeSize + i] = i;
        }
        for (int i = n; i < treeSize; i++) {
            tree[treeSize + i] = -1; 
        }

        auto compare = [&](int a, int b) -> int {
            if (a == -1) return b;
            if (b == -1) return a;
            return comp(arr[a], arr[b]) ? a : b;
        };
        
        for (int i = treeSize - 1; i > 0; i--) {
            tree[i] = compare(tree[2 * i], tree[2 * i + 1]);
        }
        
        vector<T> result;
        result.reserve(n);
        
        for (int i = 0; i < n; i++) {
            int winnerIdx = tree[1];
            if (winnerIdx == -1) break;
            
            result.push_back(arr[winnerIdx]);
            
            int idx = treeSize + winnerIdx;
            tree[idx] = -1;
            
            idx /= 2;
            while (idx >= 1) {
                tree[idx] = compare(tree[2 * idx], tree[2 * idx + 1]);
                idx /= 2;
            }
        }
        
        arr = result;
    }
    
    template<size_t N>
    void sort(T (&arr)[N]) {
        vector<T> vec(arr, arr + N);
        sort(vec);
        
        for (size_t i = 0; i < N; i++) {
            arr[i] = vec[i];
        }
    }
};

#endif