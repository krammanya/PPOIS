#include "customset.h"

using namespace std;

CustomSet::CustomSet() : valid(true) {}

CustomSet::CustomSet(const string& s) {
    valid = check(s);
    if (!valid) {
        elements.clear();
        throw invalid_argument("Неправильно ввели множество"); 
    }
    elements = parse(s);
}

CustomSet::CustomSet(const char* cstr) {
    string s(cstr);
    valid = check(s);
    if (!valid){
        elements.clear();
        throw invalid_argument("Неправильно ввели множество");
    }
    elements=parse(s);
}

CustomSet::CustomSet(const CustomSet& other) : elements(other.elements), valid(other.valid) {}

CustomSet& CustomSet::operator=(const CustomSet& other) {
    if (this != &other) {
        elements = other.elements;
        valid = other.valid;
    }
    return *this;
}

CustomSet::~CustomSet() = default;

bool CustomSet::operator==(const CustomSet& other) const {
    return elements == other.elements;
}

bool CustomSet::operator!=(const CustomSet& other) const {
    return !(*this == other);
}

ostream& operator<<(ostream& os, const CustomSet& s) {
    os << "{";
    for (size_t i = 0; i < s.elements.size(); ++i) {
        os << s.elements[i];
        if (i + 1 < s.elements.size()) os << ", ";
    }
    os << "}";
    return os;
}

istream& operator>>(istream& is, CustomSet& s) {
    string input;
    getline(is, input);
    s.valid = s.check(input);
    if (s.valid) s.elements = s.parse(input);
    else s.elements.clear();
    return is;
}

bool CustomSet::check(const string& s) const {
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

string CustomSet::get_element(const char*& c, bool& valid) {
    string t;
    while (*c && (*c == ' ' || *c == ',' || *c == '}')) ++c;
    if (*c == '\0') return "";
    if (*c == '{') {
        int count = 0;
        do {
            if (*c == '\0') {
                valid = false;
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

vector<string> CustomSet::parse(const string& s) {
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
        bool exist = false;
        for (size_t i = 0; i < result.size(); ++i) {
            if (result[i] == t) {
                exist = true;
                break;
            }
        }
        if (!exist)  result.push_back(t);
    }

    while (*c == ' ' || *c == '}') ++c;
    if (*c != '\0') {
        valid = false;
        return {};
    }
    if (result.empty()) {
        valid = false;
        return {};
    }

    return result;
}

static string trim(const string& str) {
    size_t first = 0;
    while (first < str.size() && isspace(str[first])) {
        first++;
    }
    size_t last = str.size();
    while (last > first && isspace(str[last - 1])) last--;
    if (first >= last) return string();
    return str.substr(first, last - first);
}

void CustomSet::add_element(const string& str) {
    string new_elem = trim(str);
    for (auto& element : elements) {
        if (element == new_elem) {
            return;
        }
    }
    if (new_elem.front() == '{' && new_elem.back() == '}') {
        if (!check(new_elem)) {
            return;
        }
    }
    else {
        for (char c : new_elem) {
            if (c == '{' || c == '}' || c == ',') {
                return;
            }
        }
    }
    elements.push_back(new_elem);
}

bool CustomSet::remove_element(const string& str) {
    string target = trim(str);
    if (target.empty()) {
        return false;
    }
    if (target.front() == '{' && target.back() == '}') {
        if (!check(target)) return false;
    }
    else {
        for (char c : target) {
            if (c == '{' || c == '}' || c == ',') return false;
        }
    }
    for (size_t i = 0; i < elements.size(); ++i) {
        if (elements[i] == target) {
            elements.erase(elements.begin() + i);
            return true;
        }
    }
    return false;
}

int  CustomSet::size() const {
    return elements.size();
}

bool CustomSet::operator[](const std::string& str) const {
    string target = trim(str);
    if (target.empty()) return false;
    if (target.front() == '{' && target.back() == '}') {
        if (!check(target))
            return  false;
    }
    else {
        for (char c : target) {
            if (c == '{' || c == '}' || c == ',') return false;
        }
    }
    for (size_t i = 0; i < elements.size(); ++i) {
        if (elements[i] == target) {
            return true;
        }
    }
    return false;
}

CustomSet& CustomSet::operator+=(const CustomSet& other) {
    for (const auto& el : other.elements) {
        if (!(*this)[el]) elements.push_back(el);
    }
    return *this;
}

CustomSet CustomSet::operator+(const CustomSet& other) const {
    CustomSet result = *this;
    result += other;
    return result;
}

CustomSet& CustomSet::operator*=(const CustomSet& other) {
    vector<string> result;
    for (const auto& el : elements) {
        if (other[el]) result.push_back(el);
    }
    elements = move(result);
    return *this;
}

CustomSet CustomSet::operator*(const CustomSet& other) const {
    CustomSet result = *this;
    result *= other;
    return result;
}

CustomSet& CustomSet::operator-=(const CustomSet& other) {
    vector<string> result;
    for (const auto& el : elements) {
        if (!other[el]) result.push_back(el);
    }
    elements = move(result);
    return *this;
}
CustomSet CustomSet::operator-(const CustomSet& other) const {
    CustomSet result = *this;
    result -= other;
    return result;
}
vector<string> CustomSet::parse(const char* cstr) {
    return parse(string(cstr));
}

vector<string> CustomSet::get_elements() const {
    return elements;
}

void CustomSet::print() const {
    cout << *this << endl;
}

bool CustomSet::is_valid() const {
    return valid;
}

vector<CustomSet> CustomSet::powerset() const {
    vector<CustomSet> result;
    if (elements.empty()) {
        result.push_back(CustomSet());
        return result;
    }
    int n = elements.size();
    int subsets = 1 << n;

    for (int mask = 0; mask < subsets; mask++) {
        CustomSet subset;
        for (int i = 0; i < n; i++) {
            if (mask & (1 << i)) {
                subset.add_element(elements[i]);
            }
        }

        result.push_back(subset);
    }

    return result;
}

CustomSet CustomSet::cantor_set(int iterations) const {
    //if (!valid || elements.empty()) return *this;
    vector<string> set = elements;
    for (int i = 0; i < iterations; i++) {
        int n = set.size();
        cout << "Итерация" << (i + 1) << ":" << n << " элементов\n";
        if (n < 3) {
            cout << "Завершение: элементов меньше 3" << endl;
            break;
        }
        int remove_count = n / 3;
        int side = (n - remove_count) / 2;
        int middle_start = side;
        int middle_end = n - side - 1;
        if (middle_start > middle_end) break;
        cout << "Удаляем элементы с " << middle_start + 1 << " по " << middle_end + 1 << endl;

        vector<string> new_set;
        for (int j = 0; j < n; j++) {
            if (j<middle_start || j>middle_end) {
                new_set.push_back(set[j]);
            }
        }
        set = new_set;
        cout << "Множество после удаления: {";
        for (int k = 0; k < set.size(); k++) {
            if (k > 0) cout << ", ";
            cout << set[k];
        }
        cout << "}" << endl << endl;
    }
    CustomSet result;
    for (const string& element : set) {
        result.add_element(element);
    }
    return result;
}