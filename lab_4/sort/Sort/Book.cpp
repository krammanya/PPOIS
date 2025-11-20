#include "Book.h"

using namespace std;

Book::Book(string t, int y) : title(t), year(y) {};

bool Book::operator<(const Book& other) const{
    return year < other.year;
}

bool Book::operator>(const Book& other) const{
    return year > other.year;
}

bool Book::operator==(const Book& other) const {
    return year == other.year && title == other.title;
}

bool Book::operator!=(const Book& other) const {
    return !(*this == other);
}

ostream& operator<<(ostream& os, const Book& b){
    os << "\"" << b.title << "\" (" << b.year << "г.)";
    return os; 
}

string Book::getTitle() const{
    return title;
}

int Book::getYear() const{
    return year;
}