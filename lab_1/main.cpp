#include <iostream>
#include "myset.h"

using namespace std;

int main() {
    setlocale(LC_ALL, "ru");

    MySet s1;
    cout << "Введите 1 множество: ";
    cin >> s1;

    if (s1.is_valid()) {
        cout << "Вы ввели: ";
        s1.print();
    }
    else {
        cout << "Ошибка при вводе множества через cin.\n";
    }
   
    while (true) {
        cout << "=== Меню ===\n"
            << " 0. Выход\n"
            << " 1. Добавление элемента\n"
            << " 2. Удаление элемента\n"
            << " 3. Мощность множества\n"
            << " 4. Принадлежность элемента\n"
            << " 5. Объединение с другим множеством\n"
            << " 6. Пересечение с другим множеством\n"
            << " 7. Разность с другим множеством\n"
            << " 8. Построить булеан\n"
            << " 9.Канторово множество\n"
            << "Выберите операцию (0–9): ";
        int choice;
        cin >> choice;
        if (choice == 0) {
            cout << "Завершение программы.\n";
            break;
        }
        switch (choice) {
        case 1: {
            cout << "Введите элемент для добавления: ";
            string elem;
            cin >> ws;
            getline(cin, elem);
            s1.add_element(elem);
            cout << "Множество после добавления: ";
            s1.print();
            break;
        }
        case 2: {
            cout << "Введите элемент для удаления: ";
            string target;
            cin >> ws;
            getline(cin, target);
            bool ok = s1.remove_element(target);
            if (ok) {
                cout << "Элемент удалён\n";
                s1.print();
            }
            else {
                cout << "Такого элемента в множестве нет\n";
            }
            break;
        }
        case 3: {
            cout << "Мощность множества: " << s1.size() << "\n";
            break;
        }

        case 4: {
            cout << "Введите элемент для проверки принадлежности: ";
            string target;
            cin >> ws;
            getline(cin, target);
            cout << s1[target] << "\n";
            break;
        }
        case 5: {
            MySet s2;
            cout << "Введите 2 множество: ";
            cin >> ws;
            cin >> s2;

            if (s2.is_valid()) {
                cout << "Вы ввели: ";
                s2.print();
            }
            else {
                cout << "Ошибка при вводе множества через cin.\n";
            }
            MySet s = s1 + s2;
            cout << "Результат: " << s << "\n";
            break;
        }
        case 6: {
            MySet s3;
            cout << "Введите 2 множество: ";
            cin >> ws;
            cin >> s3;

            if (s3.is_valid()) {
                cout << "Вы ввели: ";
                s3.print();
            }
            else {
                cout << "Ошибка при вводе множества через cin.\n";
            }
            MySet s = s1 * s3;
            cout << "Результат: " << s << "\n";
            break;
        }
        case 7: {
            MySet s4;
            cout << "Введите 2 множество: ";
            cin >> ws;
            cin >> s4;

            if (s4.is_valid()) {
                cout << "Вы ввели: ";
                s4.print();
            }
            else {
                cout << "Ошибка при вводе множества через cin.\n";
            }
            MySet s = s1 - s4;
            cout << "Результат: " << s << "\n";
            break;
        }
        case 8: {
            vector<MySet> boolean = s1.powerset();

            cout << "Булеан множества " << s1 << ":" << endl;
            for (const MySet& subset : boolean) {
                cout << subset << endl;
            }
            break;
        }
        case 9: {
            MySet result = s1.cantor_set();
            cout << "Результат: ";
            result.print();
            break;
        }
        }
    }
  

    return 0;

}
