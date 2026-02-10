#ifndef BOOK_H
#define BOOK_H

#include <iostream>
#include <string>

using namespace std;

class Book {
private:
    string title;  
    int year;      
    
public:
    Book(string t = "", int y = 0);
    

    bool operator<(const Book& other) const;
    bool operator>(const Book& other) const;
    bool operator==(const Book& other) const;
    bool operator!=(const Book& other) const;

    friend ostream& operator<<(ostream& os, const Book& b);
    
    string getTitle() const;
    int getYear() const;
};

#endif 