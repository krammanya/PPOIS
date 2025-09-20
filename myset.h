#ifndef MYSET_H
#define MYSET_H

#include <string>
#include <vector>
#include <iostream>

using namespace std;

class MySet {
private:
    vector<string> elements;
    bool valid;

    bool check(const string& s);
    string get_element(const char*& c, bool& valid_ref);
    vector<string> parse(const string& s);
    vector<string> parse(const char* cstr);

public:
    MySet();
    MySet(const string& s);
    MySet(const char* cstr);
    MySet(const MySet& other);
    MySet& operator=(const MySet& other);
    ~MySet();

    bool operator==(const MySet& other) const;
    bool operator!=(const MySet& other) const;

    friend ostream& operator<<(ostream& os, const MySet& s);
    friend istream& operator>>(istream& is, MySet& s);

    vector<string> get_elements() const;
    void print() const;
    bool is_valid() const;
};

#endif

