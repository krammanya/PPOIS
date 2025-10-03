#ifndef LONGINT_H
#define LONGINT_H

#include <iostream>
#include <deque>
#include <string>

using namespace std;

/**
 * @class LongInt
 * @brief ����� ��� ������ � ������ ������� ������������ ��������
 *
 * ����� LongInt ������������� ���������������� ��� ������ � ������ ������� ������������� �������
 * � �������������� deque �������� ��� �������� ����. ������������ �������� �������������� ��������,
 * ��������� � �������� �����/������.
 */
class LongInt {
    friend istream& operator>>(istream& in, LongInt& num);
    friend ostream& operator<<(ostream& out, const LongInt& num);

public:
    /**
     * @brief ����������� �� ���������
     * @post ������� ������ LongInt �� ��������� 0
     */
    LongInt();

    /**
     * @brief ����������� �� ������
     * @param str ��������� ������������� �����
     * @pre ������ ������ ��������� �������� �����, �������� ���������� � '-'
     * @post ������� ������ LongInt �� ���������, �������������� � ������
     */
    LongInt(const string& str);

    /**
     * @brief ����������� �����������
     * @param other ������ LongInt ��� �����������
     * @post ������� ����� ������ LongInt � ����� �� ��������� ��� other
     */
    LongInt(const LongInt& other);

    /**
     * @brief ������� ������� ���� �� �����
     * @post ���������� ������������� �� �������� ������� �����
     */
    void remove0s();

    /**
     * @brief ����������
     * @post ����������� ������� ������� LongInt
     */
    ~LongInt();

    /**
     * @brief �������� ��������
     * @param other LongInt ��� ��������
     * @return ����� ������ LongInt �������������� �����
     */
    LongInt operator+(const LongInt& other) const;

    /**
     * @brief �������� �������� � �������������
     * @param other LongInt ��� ��������
     * @return ������ �� ���������� ������� ������
     */
    LongInt& operator+=(const LongInt& other);

    /**
     * @brief �������� ���������
     * @param other LongInt ��� ���������
     * @return ����� ������ LongInt �������������� ��������
     */
    LongInt operator-(const LongInt& other) const;

    /**
     * @brief �������� ��������� � �������������
     * @param other LongInt ��� ���������
     * @return ������ �� ���������� ������� ������
     */
    LongInt& operator-=(const LongInt& other);

    /**
     * @brief �������� ���������
     * @param other LongInt ��� ���������
     * @return ����� ������ LongInt �������������� ������������
     */
    LongInt operator*(const LongInt& other) const;

    /**
     * @brief �������� ��������� � �������������
     * @param other LongInt ��� ���������
     * @return ������ �� ���������� ������� ������
     */
    LongInt& operator*=(const LongInt& other);

    /**
     * @brief ��������� �������� �� ����� �����
     * @return true ���� ����� ����� ����, false �����
     */
    bool isZero() const;

    /**
     * @brief �������� �������
     * @param other LongInt ��� �������
     * @return ����� ������ LongInt �������������� �������
     * @pre other �� ������ ���� �����
     */
    LongInt operator/(const LongInt& other) const;

    /**
     * @brief �������� ������� � �������������
     * @param other LongInt ��� �������
     * @return ������ �� ���������� ������� ������
     * @pre other �� ������ ���� �����
     */
    LongInt& operator/=(const LongInt& other);

    // ��������� ���������
    /**
     * @brief �������� ������
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ ������ num, false �����
     */
    bool operator<(const LongInt& num) const;

    /**
     * @brief �������� ������ ��� �����
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ ������ ��� ����� num, false �����
     */
    bool operator<=(const LongInt& num) const;

    /**
     * @brief �������� ������
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ ������ num, false �����
     */
    bool operator>(const LongInt& num) const;

    /**
     * @brief �������� ������ ��� �����
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ ������ ��� ����� num, false �����
     */
    bool operator>=(const LongInt& num) const;

    /**
     * @brief �������� ���������
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ ����� num, false �����
     */
    bool operator==(const LongInt& num) const;

    /**
     * @brief �������� �����������
     * @param num LongInt ��� ���������
     * @return true ���� ������� ������ �� ����� num, false �����
     */
    bool operator!=(const LongInt& num) const;

    /**
     * @brief �������� ������������
     * @param other LongInt ��� ������������
     * @return ������ �� ������� ������
     */
    LongInt& operator=(const LongInt& other);

    /**
     * @brief �������� �������������� � int
     * @return int ������������� LongInt
     * @note ����� �������� �������� ���� �������� LongInt ��������� �������� int
     */
    operator int() const;

    /**
     * @brief ���������� �������� ����������
     * @return ������ �� ������������������ ������
     */
    LongInt& operator++();

    /**
     * @brief ����������� �������� ����������
     * @return ������ LongInt � �������� ��������� �� ����������
     */
    LongInt operator++(int);

    /**
     * @brief ���������� �������� ����������
     * @return ������ �� ������������������ ������
     */
    LongInt& operator--();

    /**
     * @brief ����������� �������� ����������
     * @return ������ LongInt � �������� ��������� �� ����������
     */
    LongInt operator--(int);

    /**
     * @brief ���������� ������� ������ � ������ LongInt � ������� ��������� ���������
     * @param other LongInt ��� ���������
     * @post ��������� ��������� ��������� � ����������� �����
     */
    void compareWith(const LongInt& other) const;

    /**
     * @brief ���������� ������� ������ � int � ������� ��������� ���������
     * @param other int ��� ���������
     * @post ��������� ��������� ��������� � ����������� �����
     */
    void compareWith(int other) const;

private:
    deque<char> digits;    ///< ��������� ��� �������� ���� �����
    bool negative;         ///< ���� ��������������� �����
};

#endif 