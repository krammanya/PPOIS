#include <iostream>
#include "myset.h"

using namespace std;

int main() {
    setlocale(LC_ALL, "ru");

    MySet s1;
    cout << "Введите множество через cin >>: ";
    cin >> s1;

    if (s1.is_valid()) {
        cout << "Вы ввели: ";
        s1.print();
    }
    else {
        cout << "Ошибка при вводе множества через cin.\n";
    }

    string input;
    cout << "Введите множество через getline: ";
    cin.ignore();
    getline(cin, input);

    if (!input.empty() && (input.back() == '\r' || input.back() == '\n'))
        input.pop_back();

    MySet s2(input);
    if (s2.is_valid()) {
        cout << "Вы ввели: ";
        s2.print();
    }
    else {
        cout << "Ошибка при вводе множества через getline.\n";
    }

    return 0;
}
