
#include <iostream>
#include "customset.h"

using namespace std;

int main() {
    setlocale(LC_ALL, "ru");

    cout << "������� 1 ���������: ";
    string input1;
    getline(cin, input1);

    CustomSet s1;
    try {
        s1 = CustomSet(input1);
        cout << "�� �����: ";
        s1.print();
    }
    catch (const invalid_argument& e) {
        cout << "������: " << e.what() << endl;
        return 1;
    }
    

    while (true) {
        cout << "=== ���� ===\n"
            << " 0. �����\n"
            << " 1. ���������� ��������\n"
            << " 2. �������� ��������\n"
            << " 3. �������� ���������\n"
            << " 4. �������������� ��������\n"
            << " 5. ����������� � ������ ����������\n"
            << " 6. ����������� � ������ ����������\n"
            << " 7. �������� � ������ ����������\n"
            << " 8. ��������� ������\n"
            << " 9.��������� ���������\n"
            << "�������� �������� (0�9): ";
        int choice;
        cin >> choice;
        if (choice == 0) {
            cout << "���������� ���������.\n";
            break;
        }
        switch (choice) {
        case 1: {
            cout << "������� ������� ��� ����������: ";
            string elem;
            cin >> ws;
            getline(cin, elem);
            s1.add_element(elem);
            cout << "��������� ����� ����������: ";
            s1.print();
            break;
        }
        case 2: {
            cout << "������� ������� ��� ��������: ";
            string target;
            cin >> ws;
            getline(cin, target);
            bool ok = s1.remove_element(target);
            if (ok) {
                cout << "������� �����\n";
                s1.print();
            }
            else {
                cout << "������ �������� � ��������� ���\n";
            }
            break;
        }
        case 3: {
            cout << "�������� ���������: " << s1.size() << "\n";
            break;
        }

        case 4: {
            cout << "������� ������� ��� �������� ��������������: ";
            string target;
            cin >> ws;
            getline(cin, target);
            cout << s1[target] << "\n";
            break;
        }
        case 5: {
            CustomSet s2;
            cout << "������� 2 ���������: ";
            string input2;
            cin >> ws;
            getline(cin, input2);

            try {
                CustomSet s2(input2.c_str());
                cout << "�� �����: ";
                s2.print();

                CustomSet s = s1 + s2;
                cout << "��������� �����������: " << s << "\n";
            }
            catch (const invalid_argument& e) {
                cout << "������: " << e.what() << endl;
            }
            break;
        }
        case 6: {
            cout << "������� 2 ���������: ";
            string input3;
            getline(cin >> ws, input3);

            try {
               CustomSet s3 (input3);
                 cout << "�� �����: ";
                 s3.print();
                 CustomSet s = s1 * s3;
                 cout << "��������� �����������: " << s << "\n";
            
            }
            catch (const invalid_argument& e) {
                cout << "������: " << e.what() << endl;
                return 1;
            }
            break;
        }
        case 7: {
            cout << "������� 2 ���������: ";
            string input4;
            
            getline(cin >> ws, input4);

            try {
                CustomSet s4 (input4);
                cout << "�� �����: ";
                s4.print();
                CustomSet s = s1 - s4;
                cout << "��������� ��������: " << s << "\n";

            }
            catch (const invalid_argument& e) {
                cout << "������: " << e.what() << endl;
                return 1;
            }
            break;
        }
        case 8: {
            vector<CustomSet> boolean = s1.powerset();

            cout << "������ ��������� " << s1 << ":" << endl;
            for (const CustomSet& subset : boolean) {
                cout << subset << endl;
            }
            break;
        }
        case 9: {
            CustomSet result = s1.cantor_set();
            cout << "���������: ";
            result.print();
            break;
        }
        }
    }


    return 0;

}
