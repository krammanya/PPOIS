#ifndef COMBSORT_H
#define COMBSORT_H

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>

using namespace std;

template<typename T>
class CombSort {
public:
    void sort(vector<T>& arr) {
        int n = arr.size();
        int gap = n;
        bool swapped = true;
        
        while (gap > 1 || swapped) {
            gap = max(1, static_cast<int>(gap / 1.3));
            swapped = false;
            
            for (int i = 0; i < n - gap; i++) {
                if (arr[i + gap] < arr[i]) {
                    swap(arr[i], arr[i + gap]);
                    swapped = true;
                }
            }
        }
    }

    template<size_t N>
    void sort(T (&arr)[N]) {
        int gap = N;
        bool swapped = true;
        
        while (gap > 1 || swapped) {
            gap = max(1, static_cast<int>(gap / 1.3));
            swapped = false;
            
            for (int i = 0; i < N - gap; i++) {
                if (arr[i + gap] < arr[i]) {
                    swap(arr[i], arr[i + gap]);
                    swapped = true;
                }
            }
        }
    }

    void printVector(const vector<T>& arr, const string& message) {
        cout << message << ": ";
        for (const auto& elem : arr) {
            cout << elem << " ";
        }
        cout << endl;
    }

    template<size_t N>
    void printArray(const T (&arr)[N], const string& message) {
        cout << message << ": ";
        for (size_t i = 0; i < N; i++) {
            cout << arr[i] << " ";
        }
        cout << endl;
    }
};

#endif // COMBSORT_H