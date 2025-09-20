#include "myset.h"

using namespace std;

MySet::MySet() : valid(true) {}

MySet::MySet(const string& s) {
    valid = check(s);
    if (valid) elements = parse(s);
    else elements.clear();
}

MySet::MySet(const char* cstr) {
    string s(cstr);
    valid = check(s);
    if (valid) elements = parse(s);
    else elements.clear();
}

MySet::MySet(const MySet& other) : elements(other.elements), valid(other.valid) {}

MySet& MySet::operator=(const MySet& other) {
    if (this != &other) {
        elements = other.elements;
        valid = other.valid;
    }
    return *this;
}

MySet::~MySet() = default;

bool MySet::operator==(const MySet& other) const {
    return elements == other.elements;
}

bool MySet::operator!=(const MySet& other) const {
    return !(*this == other);
}

ostream& operator<<(ostream& os, const MySet& s) {
    os << "{";
    for (size_t i = 0; i < s.elements.size(); ++i) {
        os << s.elements[i];
        if (i + 1 < s.elements.size()) os << ", ";
    }
    os << "}";
    return os;
}

istream& operator>>(istream& is, MySet& s) {
    string input;
    getline(is, input);
    s.valid = s.check(input);
    if (s.valid) s.elements = s.parse(input);
    else s.elements.clear();
    return is;
}

bool MySet::check(const string& s) {
    const char* p = s.c_str();
    int depth = 0;
    bool expect_element = true;
    bool last_was_comma = false;

    while (*p) {
        char ch = *p;
        if (ch == ' ') { ++p; continue; }

        if (ch == '{') {
            if (!expect_element) return false;
            depth++;
            expect_element = true;
            last_was_comma = false;
        }
        else if (ch == '}') {
            if (depth <= 0 || last_was_comma) return false;
            depth--;
            expect_element = false;
            last_was_comma = false;
        }
        else if (ch == ',') {
            if (expect_element || last_was_comma) return false;
            expect_element = true;
            last_was_comma = true;
        }
        else {
            if (!expect_element) return false;
            while (*p && *p != ',' && *p != '}' && *p != '{' && *p != ' ') ++p;
            --p;
            expect_element = false;
            last_was_comma = false;
        }
        ++p;
    }

    return depth == 0 && !expect_element && !last_was_comma;
}

string MySet::get_element(const char*& c, bool& valid_ref) {
    string t;
    while (*c && (*c == ' ' || *c == ',' || *c == '}')) ++c;
    if (*c == '\0') return "";
    if (*c == '{') {
        int count = 0;
        do {
            if (*c == '\0') {
                valid_ref = false;
                return "";
            }
            t += *c;
            if (*c == '{') count++;
            if (*c == '}') count--;
            c++;
        } while (count);
    }
    else {
        while (*c != ',' && *c != '}') {
            if (*c == '\0') break;
            if (*c != ' ') t += *c;
            c++;
        }
    }
    return t;
}

vector<string> MySet::parse(const string& s) {
    if (!check(s)) {
        valid = false;
        return {};
    }
    vector<string> result;
    const char* c = s.c_str();
    while (*c && *c != '{') ++c;
    if (*c == '\0') return result;
    ++c;

    string t;
    while ((t = get_element(c, valid)).size()) {
        result.push_back(t);
    }

    while (*c == ' ' || *c == '}') ++c;
    if (*c != '\0') {
        valid = false;
        return {};
    }

    return result;
}

vector<string> MySet::parse(const char* cstr) {
    return parse(string(cstr));
}

vector<string> MySet::get_elements() const {
    return elements;
}

void MySet::print() const {
    cout << *this << endl;
}

bool MySet::is_valid() const {
    return valid;
}