/**
 * @file myset.h
 * @brief Заголовочный файл класса MySet для работы с математическими множествами
 * @author Ваше Имя
 * @date 2024
 */

#ifndef MYSET_H
#define MYSET_H

#include <string>
#include <vector>
#include <iostream>

using namespace std;

/**
 * @class MySet
 * @brief Класс для представления и работы с математическими множествами
 *
 * Класс предоставляет функциональность для создания, парсинга и работы с множествами,
 * включая вложенные множества. Поддерживает проверку валидности синтаксиса.
 */
class MySet {
private:
    vector<string> elements; ///< Вектор элементов множества
    bool valid;              ///< Флаг валидности множества

    /**
     * @brief Проверить валидность синтаксиса строки множества
     * @param s Строка для проверки
     * @return true если синтаксис валиден, false в противном случае
     */
    bool check(const string& s);

    /**
     * @brief Извлечь следующий элемент из строки
     * @param[in,out] c Указатель на текущую позицию в строке
     * @param[out] valid_ref Ссылка на флаг валидности операции
     * @return Извлеченный элемент как строка
     */
    string get_element(const char*& c, bool& valid_ref);

    /**
     * @brief Распарсить строку на элементы множества
     * @param s Строка для парсинга
     * @return Вектор распарсенных элементов
     */
    vector<string> parse(const string& s);

    /**
     * @brief Распарсить C-строку на элементы множества
     * @param cstr C-строка для парсинга
     * @return Вектор распарсенных элементов
     */
    vector<string> parse(const char* cstr);

public:
    /**
     * @brief Конструктор по умолчанию
     * @details Создает пустое валидное множество
     */
    MySet();

    /**
     * @brief Конструктор из строки
     * @param s Строка с представлением множества в формате "{элемент1, элемент2, ...}"
     */
    MySet(const string& s);

    /**
     * @brief Конструктор из C-строки
     * @param cstr C-строка с представлением множества
     */
    MySet(const char* cstr);

    /**
     * @brief Конструктор копирования
     * @param other Другое множество для копирования
     */
    MySet(const MySet& other);

    /**
     * @brief Оператор присваивания
     * @param other Другое множество для присваивания
     * @return Ссылка на текущее множество
     */
    MySet& operator=(const MySet& other);

    /**
     * @brief Деструктор
     */
    ~MySet();

    /**
     * @brief Оператор сравнения равенства
     * @param other Другое множество для сравнения
     * @return true если множества равны, false в противном случае
     */
    bool operator==(const MySet& other) const;

    /**
     * @brief Оператор сравнения неравенства
     * @param other Другое множество для сравнения
     * @return true если множества не равны, false в противном случае
     */
    bool operator!=(const MySet& other) const;

    /**
     * @brief Оператор вывода в поток
     * @param os Выходной поток
     * @param s Множество для вывода
     * @return Ссылка на выходной поток
     */
    friend ostream& operator<<(ostream& os, const MySet& s);

    /**
     * @brief Оператор ввода из потока
     * @param is Входной поток
     * @param s Множество для ввода
     * @return Ссылка на входной поток
     */
    friend istream& operator>>(istream& is, MySet& s);

    /**
     * @brief Получить элементы множества
     * @return Вектор элементов множества
     */
    vector<string> get_elements() const;

    /**
     * @brief Вывести множество на стандартный вывод
     */
    void print() const;

    /**
     * @brief Проверить валидность множества
     * @return true если множество валидно, false в противном случае
     */
    bool is_valid() const;
};

#endif // MYSET_H

