
#include <iostream>
#include "customset.h"

using namespace std;

int main() {
    setlocale(LC_ALL, "ru");

    cout << "Введите 1 множество: ";
    string input1;
    getline(cin, input1);

    CustomSet s1;
    try {
        s1 = CustomSet(input1);
        cout << "Вы ввели: ";
        s1.print();
    }
    catch (const invalid_argument& e) {
        cout << "Ошибка: " << e.what() << endl;
        return 1;
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
            CustomSet s2;
            cout << "Введите 2 множество: ";
            string input2;
            cin >> ws;
            getline(cin, input2);

            try {
                CustomSet s2(input2.c_str());
                cout << "Вы ввели: ";
                s2.print();

                CustomSet s = s1 + s2;
                cout << "Результат объединения: " << s << "\n";
            }
            catch (const invalid_argument& e) {
                cout << "Ошибка: " << e.what() << endl;
            }
            break;
        }
        case 6: {
            cout << "Введите 2 множество: ";
            string input3;
            getline(cin >> ws, input3);

            try {
               CustomSet s3 (input3);
                 cout << "Вы ввели: ";
                 s3.print();
                 CustomSet s = s1 * s3;
                 cout << "Результат пересечения: " << s << "\n";
            
            }
            catch (const invalid_argument& e) {
                cout << "Ошибка: " << e.what() << endl;
                return 1;
            }
            break;
        }
        case 7: {
            cout << "Введите 2 множество: ";
            string input4;
            
            getline(cin >> ws, input4);

            try {
                CustomSet s4 (input4);
                cout << "Вы ввели: ";
                s4.print();
                CustomSet s = s1 - s4;
                cout << "Результат разности: " << s << "\n";

            }
            catch (const invalid_argument& e) {
                cout << "Ошибка: " << e.what() << endl;
                return 1;
            }
            break;
        }
        case 8: {
            vector<CustomSet> boolean = s1.powerset();

            cout << "Булеан множества " << s1 << ":" << endl;
            for (const CustomSet& subset : boolean) {
                cout << subset << endl;
            }
            break;
        }
        case 9: {
            CustomSet result = s1.cantor_set();
            cout << "Результат: ";
            result.print();
            break;
        }
        }
    }


    return 0;

}
