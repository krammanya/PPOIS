#include<iostream>
#include"longint.h"

using namespace std;

int main() {
	setlocale(LC_ALL, "ru");
	LongInt userNumber1("0");
	while (true) {
		string input1;
		cout << "������� ������ �����: ";
		cin >> input1;
		
		try {
			userNumber1=LongInt (input1);
			cout << "�� �����: " << userNumber1 << endl;
			break;
		}
		catch (const invalid_argument& e) {
			cout << "������������ ����: " << e.what() << endl;
		}
	}

	while (true) {
		cout << "\n=== ���� ===" << endl;
		cout << " 0. �����" << endl;
		cout << " 1. �������������� �������� ������ � ������" << endl;
		cout << " 2. �������� ���� ������� �����" << endl;
		cout << " 3. �������� �������� ������ � �����" << endl;
		cout << " 4. ��������� ���� ������� �����" << endl;
		cout << " 5. ��������� �� �������� ������ ������" << endl;
		cout << " 6. ������������ ���� ������� �����" << endl;
		cout << " 7. ������������ �������� ������ � ������" << endl;
		cout << " 8. ������� ���� ������� �����" << endl;
		cout << " 9. ������� �������� ������ �� �����" << endl;
		cout << "10. ��������� ���� ������� �����" << endl;
		cout << "11. ��������� �������� ������ � �����" << endl;
		cout << "12. ���- � �������������" << endl;
		cout << "13. ���- � �������������" << endl;
		cout << "�������� �������� (0-13): ";

		int choice;
		cin >> choice;
		if(choice==0) {
			cout << "���������� ���������.\n";
			break;
		}
		
			switch (choice) {
			case 1: {
				int result = (int)userNumber1;
				cout << "��������� �������������� � int: " << result << endl;
				break;
			}
			case 2:
			{
				cout << "������� ������ �����: ";
				string input2;
				cin >> input2;
				try {
					LongInt userNumber2(input2);
					cout << "�� �����: " << userNumber2 << endl;
					LongInt result = userNumber1 + userNumber2;
					cout << userNumber1 << " + " << userNumber2 << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������������ ����: " << e.what() << endl;
				}
				break;
			}
			case 3: {
				cout << "������� ����� �����: ";
				int num;
				cin >> num;
				try {
					LongInt temp(to_string(num));
					LongInt result = userNumber1 + temp;
					cout << userNumber1 << " + " << num << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������: " << e.what() << endl;
				}
				break;
			}
			case 4: {
				cout << "������� ������ �����: ";
				string input2;
				cin >> input2;
				try {
					LongInt userNumber2(input2);
					cout << "�� �����: " << userNumber2 << endl;
					LongInt result = userNumber1 - userNumber2;
					cout << userNumber1 << " - " << userNumber2 << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������������ ����: " << e.what() << endl;
				}
				break;
			}
			case 5: {
				cout << "������� ����� �����: ";
				int num;
				cin >> num;
				try {
					LongInt temp(to_string(num));
					LongInt result = userNumber1 - temp;
					cout << userNumber1 << " - " << num << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������: " << e.what() << endl;
				}
				break;
			}
			case 6: {
				cout << "������� ������ �����: ";
				string input2;
				cin >> input2;
				try {
					LongInt userNumber2(input2);
					cout << "�� �����: " << userNumber2 << endl;
					LongInt result = userNumber1 * userNumber2;
					cout << userNumber1 << " * " << userNumber2 << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������������ ����: " << e.what() << endl;
				}
				break;
			}
			case 7: {
				cout << "������� ����� �����: ";
				int num;
				cin >> num;
				try {
					LongInt temp(to_string(num));
					LongInt result = userNumber1 *temp;
					cout << userNumber1 << " * " << num << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������: " << e.what() << endl;
				}
				break;
			}
			case 8: {
				cout << "������� ������ �����: ";
				string input2;
				cin >> input2;
				try {
					LongInt userNumber2(input2);
					cout << "�� �����: " << userNumber2 << endl;
					LongInt result = userNumber1 / userNumber2;
					cout << userNumber1 << " / " << userNumber2 << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������������ ����: " << e.what() << endl;
				}
				break;
			}
			case 9: {
				cout << "������� ����� �����: ";
				int num;
				cin >> num;
				try {
					LongInt temp(to_string(num));
					LongInt result = userNumber1 / temp;
					cout << userNumber1 << " / " << num << " = " << result << endl;
				}
				catch (const invalid_argument& e) {
					cout << "������: " << e.what() << endl;
				}
				break;
			}
			case 10: {
				cout << "������� ������ �����: ";
				string input2;
				cin >> input2;
				try {
					LongInt userNumber2(input2);
					cout << "�� �����: " << userNumber2 << endl;
					userNumber1.compareWith(userNumber2); 
				}
				catch (const invalid_argument& e) {
					cout << "������������ ����: " << e.what() << endl;
				}
				break;
			}
			case 11: {
				cout << "������� ����� �����: ";
				int num;
				cin >> num;
				try {
					userNumber1.compareWith(num);  
				}
				catch (const invalid_argument& e) {
					cout << "������: " << e.what() << endl;
				}
				break;
			}
			case 12: {
				cout << "�������� �����: " << userNumber1 << endl;
				cout << "++�����: " << ++userNumber1 << endl;
				cout << "����� ����������: " << userNumber1 << endl;
				break;
			}
			case 13: {
				cout << "�������� �����: " << userNumber1 << endl;
				cout << "--�����: " << --userNumber1 << endl;
				cout << "����� ����������: " << userNumber1 << endl;
				break;
			}
			default:
				cout << "�������� ����� ��������!" << endl;
				break;
			}
	}
	return 0;
}