#ifndef LONGINT_H
#define LONGINT_H

#include <iostream>
#include <deque>
#include <string>

using namespace std;

/**
 * @class LongInt
 * @brief Класс для работы с целыми числами произвольной точности
 *
 * Класс LongInt предоставляет функциональность для работы с целыми числами произвольного размера
 * с использованием deque символов для хранения цифр. Поддерживает основные арифметические операции,
 * сравнения и операции ввода/вывода.
 */
class LongInt {
    friend istream& operator>>(istream& in, LongInt& num);
    friend ostream& operator<<(ostream& out, const LongInt& num);

public:
    /**
     * @brief Конструктор по умолчанию
     * @post Создает объект LongInt со значением 0
     */
    LongInt();

    /**
     * @brief Конструктор из строки
     * @param str Строковое представление числа
     * @pre Строка должна содержать валидные цифры, возможно начинаться с '-'
     * @post Создает объект LongInt со значением, представленным в строке
     */
    LongInt(const string& str);

    /**
     * @brief Конструктор копирования
     * @param other Объект LongInt для копирования
     * @post Создает новый объект LongInt с таким же значением как other
     */
    LongInt(const LongInt& other);

    /**
     * @brief Удаляет ведущие нули из числа
     * @post Внутреннее представление не содержит ведущих нулей
     */
    void remove0s();

    /**
     * @brief Деструктор
     * @post Освобождает ресурсы объекта LongInt
     */
    ~LongInt();

    /**
     * @brief Оператор сложения
     * @param other LongInt для сложения
     * @return Новый объект LongInt представляющий сумму
     */
    LongInt operator+(const LongInt& other) const;

    /**
     * @brief Оператор сложения с присваиванием
     * @param other LongInt для сложения
     * @return Ссылка на измененный текущий объект
     */
    LongInt& operator+=(const LongInt& other);

    /**
     * @brief Оператор вычитания
     * @param other LongInt для вычитания
     * @return Новый объект LongInt представляющий разность
     */
    LongInt operator-(const LongInt& other) const;

    /**
     * @brief Оператор вычитания с присваиванием
     * @param other LongInt для вычитания
     * @return Ссылка на измененный текущий объект
     */
    LongInt& operator-=(const LongInt& other);

    /**
     * @brief Оператор умножения
     * @param other LongInt для умножения
     * @return Новый объект LongInt представляющий произведение
     */
    LongInt operator*(const LongInt& other) const;

    /**
     * @brief Оператор умножения с присваиванием
     * @param other LongInt для умножения
     * @return Ссылка на измененный текущий объект
     */
    LongInt& operator*=(const LongInt& other);

    /**
     * @brief Проверяет является ли число нулем
     * @return true если число равно нулю, false иначе
     */
    bool isZero() const;

    /**
     * @brief Оператор деления
     * @param other LongInt для деления
     * @return Новый объект LongInt представляющий частное
     * @pre other не должен быть нулем
     */
    LongInt operator/(const LongInt& other) const;

    /**
     * @brief Оператор деления с присваиванием
     * @param other LongInt для деления
     * @return Ссылка на измененный текущий объект
     * @pre other не должен быть нулем
     */
    LongInt& operator/=(const LongInt& other);

    // Операторы сравнения
    /**
     * @brief Оператор меньше
     * @param num LongInt для сравнения
     * @return true если текущий объект меньше num, false иначе
     */
    bool operator<(const LongInt& num) const;

    /**
     * @brief Оператор меньше или равно
     * @param num LongInt для сравнения
     * @return true если текущий объект меньше или равен num, false иначе
     */
    bool operator<=(const LongInt& num) const;

    /**
     * @brief Оператор больше
     * @param num LongInt для сравнения
     * @return true если текущий объект больше num, false иначе
     */
    bool operator>(const LongInt& num) const;

    /**
     * @brief Оператор больше или равно
     * @param num LongInt для сравнения
     * @return true если текущий объект больше или равен num, false иначе
     */
    bool operator>=(const LongInt& num) const;

    /**
     * @brief Оператор равенства
     * @param num LongInt для сравнения
     * @return true если текущий объект равен num, false иначе
     */
    bool operator==(const LongInt& num) const;

    /**
     * @brief Оператор неравенства
     * @param num LongInt для сравнения
     * @return true если текущий объект не равен num, false иначе
     */
    bool operator!=(const LongInt& num) const;

    /**
     * @brief Оператор присваивания
     * @param other LongInt для присваивания
     * @return Ссылка на текущий объект
     */
    LongInt& operator=(const LongInt& other);

    /**
     * @brief Оператор преобразования в int
     * @return int представление LongInt
     * @note Может потерять точность если значение LongInt превышает диапазон int
     */
    operator int() const;

    /**
     * @brief Префиксный оператор инкремента
     * @return Ссылка на инкрементированный объект
     */
    LongInt& operator++();

    /**
     * @brief Постфиксный оператор инкремента
     * @return Объект LongInt с исходным значением до инкремента
     */
    LongInt operator++(int);

    /**
     * @brief Префиксный оператор декремента
     * @return Ссылка на декрементированный объект
     */
    LongInt& operator--();

    /**
     * @brief Постфиксный оператор декремента
     * @return Объект LongInt с исходным значением до декремента
     */
    LongInt operator--(int);

    /**
     * @brief Сравнивает текущий объект с другим LongInt и выводит результат сравнения
     * @param other LongInt для сравнения
     * @post Результат сравнения выводится в стандартный вывод
     */
    void compareWith(const LongInt& other) const;

    /**
     * @brief Сравнивает текущий объект с int и выводит результат сравнения
     * @param other int для сравнения
     * @post Результат сравнения выводится в стандартный вывод
     */
    void compareWith(int other) const;

private:
    deque<char> digits;    ///< Контейнер для хранения цифр числа
    bool negative;         ///< Флаг отрицательности числа
};

#endif 