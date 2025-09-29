/**
 * @file myset.h
 * @brief Заголовочный файл класса MySet для работы с математическими множествами
 * @author Крамич Анна
 * @date 2025
 */

#ifndef MYSET_H
#define MYSET_H

#include <string>
#include <vector>
#include <iostream>
#include <cmath>

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
    bool check(const string& s) const;

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
    * @brief Оператор доступа к элементу по значению
    * @param str Строка — элемент, который нужно найти
    * @return true если элемент принадлежит множеству, false в противном случае
    */
    bool operator[](const std::string& str) const;

    /**
     * @brief Оператор объединения множеств
     * @param other Другое множество
     * @return Новое множество — объединение
     */
    MySet operator+(const MySet& other) const;

    /**
     * @brief Оператор объединения с присваиванием
     * @param other Другое множество
     * @return Ссылка на текущее множество
     */
    MySet& operator+=(const MySet& other);

    /**
     * @brief Оператор пересечения множеств
     * @param other Другое множество
     * @return Новое множество — пересечение
     */
    MySet operator*(const MySet& other) const;

    /**
     * @brief Оператор пересечения с присваиванием
     * @param other Другое множество
     * @return Ссылка на текущее множество
     */
    MySet& operator*=(const MySet& other);

    /**
     * @brief Оператор разности множеств
     * @param other Другое множество
     * @return Новое множество — разность
     */
    MySet operator-(const MySet& other) const;

    /**
     * @brief Оператор разности с присваиванием
     * @param other Другое множество
     * @return Ссылка на текущее множество
     */
    MySet& operator-=(const MySet& other);

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

    /**
     * @brief Проверка на пустое множество
     * @return true если множество пустое
     */
    bool is_empty() const;

    /**
     * @brief Добавить элемент в множество
     * @param element Строковое представление элемента
     */
    void add_element(const string& element);

    /**
     * @brief Удалить элемент из множества
     * @param element Строковое представление элемента
     * @return true если элемент был удален, false если элемент не найден
     */
    bool remove_element(const string& element);

    /**
     * @brief Определение мощности множества
     * @return Количество верхнеуровневых элементов
     */
    int size() const;

    /**
     * @brief Проверка принадлежности элемента множеству
     * @param element Строковое представление элемента
     * @return true если элемент принадлежит множеству
     */
    bool contains(const string& element) const;

    /**
     * @brief Построить булеан множества (множество всех подмножеств)
     * @return Вектор, содержащий все подмножества текущего множества
     */
    vector<MySet> powerset() const;

    /**
     * @brief Построить множество Кантора из текущего множества
     * @param iterations Количество итераций процесса построения (по умолчанию 3)
     * @return Новое множество, построенное по принципу Кантора (удаление средней трети на каждом уровне)
     * @details Множество Кантора строится путем удаления средней трети элементов на каждой итерации.
     * Процесс повторяется заданное количество раз или до тех пор, пока в множестве не останется менее 3 элементов.
     */
    MySet cantor_set(int iterations = 3) const;
};

#endif // MYSET_H
