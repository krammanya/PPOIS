/**
 * @file customset.h
 * @brief ������������ ���� ������ CustomSet ��� ������ � ��������������� �����������
 * @author ������ ����
 * @date 2025
 */

#ifndef CUSTOMSET_H
#define CUSTOMSET_H

#include <string>
#include <vector>
#include <iostream>
#include <cmath>

using namespace std;

/**
 * @class CustomSet
 * @brief ����� ��� ������������� � ������ � ��������������� �����������
 *
 * ����� ������������� ���������������� ��� ��������, �������� � ������ � �����������,
 * ������� ��������� ���������. ������������ �������� ���������� ����������.
 */
class CustomSet {
private:
    vector<string> elements; ///< ������ ��������� ���������
    bool valid;              ///< ���� ���������� ���������

    /**
     * @brief ��������� ���������� ���������� ������ ���������
     * @param s ������ ��� ��������
     * @return true ���� ��������� �������, false � ��������� ������
     */
    bool check(const string& s) const;

    /**
     * @brief ������� ��������� ������� �� ������
     * @param[in,out] c ��������� �� ������� ������� � ������
     * @param[out] valid_ref ������ �� ���� ���������� ��������
     * @return ����������� ������� ��� ������
     */
    string get_element(const char*& c, bool& valid_ref);

    /**
     * @brief ���������� ������ �� �������� ���������
     * @param s ������ ��� ��������
     * @return ������ ������������ ���������
     */
    vector<string> parse(const string& s);

    /**
     * @brief ���������� C-������ �� �������� ���������
     * @param cstr C-������ ��� ��������
     * @return ������ ������������ ���������
     */
    vector<string> parse(const char* cstr);

public:
    /**
     * @brief ����������� �� ���������
     * @details ������� ������ �������� ���������
     */
    CustomSet();

    /**
     * @brief ����������� �� ������
     * @param s ������ � �������������� ��������� � ������� "{�������1, �������2, ...}"
     */
    CustomSet(const string& s);

    /**
     * @brief ����������� �� C-������
     * @param cstr C-������ � �������������� ���������
     */
    CustomSet(const char* cstr);

    /**
     * @brief ����������� �����������
     * @param other ������ ��������� ��� �����������
     */
    CustomSet(const CustomSet& other);

    /**
     * @brief �������� ������������
     * @param other ������ ��������� ��� ������������
     * @return ������ �� ������� ���������
     */
    CustomSet& operator=(const CustomSet& other);

    /**
     * @brief ����������
     */
    ~CustomSet();

    /**
     * @brief �������� ��������� ���������
     * @param other ������ ��������� ��� ���������
     * @return true ���� ��������� �����, false � ��������� ������
     */
    bool operator==(const CustomSet& other) const;

    /**
     * @brief �������� ��������� �����������
     * @param other ������ ��������� ��� ���������
     * @return true ���� ��������� �� �����, false � ��������� ������
     */
    bool operator!=(const CustomSet& other) const;

    /**
    * @brief �������� ������� � �������� �� ��������
    * @param str ������ � �������, ������� ����� �����
    * @return true ���� ������� ����������� ���������, false � ��������� ������
    */
    bool operator[](const std::string& str) const;

    /**
     * @brief �������� ����������� ��������
     * @param other ������ ���������
     * @return ����� ��������� � �����������
     */
    CustomSet operator+(const CustomSet& other) const;

    /**
     * @brief �������� ����������� � �������������
     * @param other ������ ���������
     * @return ������ �� ������� ���������
     */
    CustomSet& operator+=(const CustomSet& other);

    /**
     * @brief �������� ����������� ��������
     * @param other ������ ���������
     * @return ����� ��������� � �����������
     */
    CustomSet operator*(const CustomSet& other) const;

    /**
     * @brief �������� ����������� � �������������
     * @param other ������ ���������
     * @return ������ �� ������� ���������
     */
    CustomSet& operator*=(const CustomSet& other);

    /**
     * @brief �������� �������� ��������
     * @param other ������ ���������
     * @return ����� ��������� � ��������
     */
    CustomSet operator-(const CustomSet& other) const;

    /**
     * @brief �������� �������� � �������������
     * @param other ������ ���������
     * @return ������ �� ������� ���������
     */
    CustomSet& operator-=(const CustomSet& other);

    /**
     * @brief �������� ������ � �����
     * @param os �������� �����
     * @param s ��������� ��� ������
     * @return ������ �� �������� �����
     */
    friend ostream& operator<<(ostream& os, const CustomSet& s);

    /**
     * @brief �������� ����� �� ������
     * @param is ������� �����
     * @param s ��������� ��� �����
     * @return ������ �� ������� �����
     */
    friend istream& operator>>(istream& is, CustomSet& s);

    /**
     * @brief �������� �������� ���������
     * @return ������ ��������� ���������
     */
    vector<string> get_elements() const;

    /**
     * @brief ������� ��������� �� ����������� �����
     */
    void print() const;

    /**
     * @brief ��������� ���������� ���������
     * @return true ���� ��������� �������, false � ��������� ������
     */
    bool is_valid() const;

    /**
     * @brief �������� �� ������ ���������
     * @return true ���� ��������� ������
     */
    bool is_empty() const;

    /**
     * @brief �������� ������� � ���������
     * @param element ��������� ������������� ��������
     */
    void add_element(const string& element);

    /**
     * @brief ������� ������� �� ���������
     * @param element ��������� ������������� ��������
     * @return true ���� ������� ��� ������, false ���� ������� �� ������
     */
    bool remove_element(const string& element);

    /**
     * @brief ����������� �������� ���������
     * @return ���������� ��������������� ���������
     */
    int size() const;

    /**
     * @brief �������� �������������� �������� ���������
     * @param element ��������� ������������� ��������
     * @return true ���� ������� ����������� ���������
     */
    bool contains(const string& element) const;

    /**
     * @brief ��������� ������ ��������� (��������� ���� �����������)
     * @return ������, ���������� ��� ������������ �������� ���������
     */
    vector<CustomSet> powerset() const;

    /**
     * @brief ��������� ��������� ������� �� �������� ���������
     * @param iterations ���������� �������� �������� ���������� (�� ��������� 3)
     * @return ����� ���������, ����������� �� �������� ������� (�������� ������� ����� �� ������ ������)
     * @details ��������� ������� �������� ����� �������� ������� ����� ��������� �� ������ ��������.
     * ������� ����������� �������� ���������� ��� ��� �� ��� ���, ���� � ��������� �� ��������� ����� 3 ���������.
     */
    CustomSet cantor_set(int iterations = 3) const;
};

#endif // CUSTOMSET_H