from src.models import Time
from src.services.storage import save, load


def print_menu() -> None:
    print("\n=== Система подготовки к экзамену ===")
    print("1. Создать студента")
    print("2. Добавить книгу")
    print("3. Показать список книг")
    print("4. Запустить чтение")
    print("5. Практиковаться")
    print("6. Пройти тестирование")
    print("7. Повторить билет")
    print("8. Добавить билет")
    print("9. Добавить вопрос в билет")
    print("10. Показать билеты")
    print("11. Отметить билет как выученный")
    print("12. Показать статус")
    print("13. Показать историю")
    print("14. Показать вопросы билета")
    print("0. Выход")


def main() -> None:
    system = load()  # Загружаем систему из файла
    print("RUNNING FILE:", __file__, flush=True)

    while True:
        print_menu()
        choice = input("Выберите действие: ").strip()
              

        try:
           
            if choice == "1":
                if system.student is not None:
                    answer = input("Создание нового студента удалит текущие данные. Продолжить? (да/нет)").strip().lower()
                    if answer != "да":
                        continue    
                name = read_text("Имя: ")
                surname = read_text("Фамилия: ")
                group = read_int("Группа: ")
                system.create_student(name, surname, group)
                print("Студент создан.")

            elif choice == "2":
                title = read_text("Название книги: ")
                subject = read_text("Предмет: ")
                author = read_text("Автор: ")
                pages = read_int("Количество страниц: ")
                system.add_book(title, subject, author, pages)
                print("Книга добавлена.")

            elif choice == "3":
                print(system.list_books())
                input("Нажмите Enter, чтобы продолжить...")

            elif choice == "4":
                title = read_text("Название книги: ")
                minutes = read_int("Сколько минут читать: ")
                start_time = read_time()
                result = system.run_read(title, minutes, start_time)
                print(result)

            elif choice == "5":
                task_count = read_int("Сколько задач всего: ")
                minutes = read_int("Сколько минут заниматься: ")
                start_time = read_time()

                result = system.run_practice(task_count, minutes, start_time)
                print(result)

            elif choice == "6":
                questions = read_int("Сколько вопросов в тесте: ")
                minutes = read_int("Сколько минут тест: ")
                start_time = read_time()

                result = system.run_testing(questions, minutes, start_time)
                print(result)

            elif choice == "7":
                ticket_number = read_int("Номер билета для повторения: ")
                minutes = read_int("Сколько минут повторять: ")
                start_time = read_time()

                result = system.run_ticket_review(ticket_number, minutes, start_time)
                input("Нажмите Enter, чтобы продолжить...")

            elif choice == "8":
                number = read_int("Номер билета: ")
                system.add_ticket(number)
                print("Билет добавлен.")

            elif choice == "9":
                number = read_int("Номер билета: ")
                question = read_text("Введите вопрос: ")
                system.add_ticket_question(number, question)
                print("Вопрос добавлен.")

            elif choice == "10":
                print(system.list_tickets())
                input("Нажмите Enter, чтобы продолжить...")

            elif choice == "11":
                number = read_int("Номер билета: ")
                system.mark_ticket_learned(number)
                print("Билет отмечен как выученный.")

            elif choice == "12":
                print(system.status())
                input("Нажмите Enter, чтобы продолжить...")

            elif choice == "13":
                print(system.last_actions())
                input("Нажмите Enter, чтобы продолжить...")
                
            elif choice == "14":
                 number = read_int("Номер билета: ")
                 print(system.show_ticket_questions(number))
                 input("Нажмите Enter, чтобы продолжить...")

            elif choice == "0":
                save(system)
                print("Система сохранена. До свидания!")
                break

            else:
                print("Неверный выбор.")

        except ValueError as e:
            print(f"Ошибка ввода: {e}")
            input("Нажмите Enter, чтобы продолжить...")

        except Exception as e:
            print(f"Неожиданная ошибка: {e}")
            input("Нажмите Enter, чтобы продолжить...")

def read_int(prompt: str) -> int:
    while True:
        value = input(prompt).strip()
        try:
            return int(value)
        except ValueError:
            print("Ошибка: нужно ввести целое число.")


def read_text(prompt: str) -> str:
    while True:
        value = input(prompt).strip()
        if value:
            return value
        print("Ошибка: поле не может быть пустым.")

def read_time() -> Time:
    while True:
        hour =read_int("Час начала (0-23):")
        minute =read_int("Минута начала (0-59):")
        
        if 0 <= hour <= 23 and 0 <= minute <= 59:
            return Time(hour, minute)
        
        print("Ошибка: время введено некорректно. Попробуйте еще раз")

if __name__ == "__main__":
    main()