import pytest

from src.models import Time
from src.services.study_system import StudySystem


@pytest.fixture
def system() -> StudySystem:
   
    return StudySystem()


@pytest.fixture
def system_with_student(system: StudySystem) -> StudySystem:
    
    system.create_student("Анна", "Иванова", 12345)
    return system


@pytest.fixture
def system_with_ticket(system_with_student: StudySystem) -> StudySystem:
  
    system_with_student.add_ticket(1)
    system_with_student.add_ticket_question(1, "Вопрос 1")
    system_with_student.add_ticket_question(1, "Вопрос 2")
    return system_with_student


def test_create_student_resets_everything(system: StudySystem) -> None:
    
    system.create_student("Старый", "Студент", 1)
    system.add_ticket(1)
    system.add_ticket_question(1, "Старый вопрос")

    system.create_student("Новый", "Студент", 2)

    assert system.student is not None
    assert system.student.name == "Новый"
    assert system.student.surname == "Студент"
    assert system.student.group == 2

    assert system.books == []
    assert system.tickets == []
    assert len(system.history) == 1
    assert "Создан студент" in system.history[0]


def test_add_ticket_adds_to_system(system_with_student: StudySystem) -> None:
    system_with_student.add_ticket(5)

    assert len(system_with_student.tickets) == 1
    assert system_with_student.tickets[0].number == 5
    assert system_with_student.tickets[0].is_learned is False


def test_add_ticket_question_adds_question(system_with_student: StudySystem) -> None:
    system_with_student.add_ticket(1)
    system_with_student.add_ticket_question(1, "Что такое ООП?")

    ticket = system_with_student.find_ticket(1)
    assert ticket.questions == ["Что такое ООП?"]


def test_show_ticket_questions_returns_text(system_with_ticket: StudySystem) -> None:
    text = system_with_ticket.show_ticket_questions(1)

    assert "Билет №1" in text
    assert "1) Вопрос 1" in text
    assert "2) Вопрос 2" in text


def test_mark_ticket_learned_sets_true(system_with_ticket: StudySystem) -> None:
    system_with_ticket.mark_ticket_learned(1)

    ticket = system_with_ticket.find_ticket(1)
    assert ticket.is_learned is True


def test_run_ticket_review_raises_if_no_questions(system_with_student: StudySystem) -> None:
    system_with_student.add_ticket(1)  

    with pytest.raises(ValueError):
        system_with_student.run_ticket_review(1, minutes=10, start_time=Time(12, 0))


def test_run_ticket_review_returns_string_and_writes_history(system_with_ticket: StudySystem) -> None:
    result = system_with_ticket.run_ticket_review(1, minutes=10, start_time=Time(12, 0))

    assert isinstance(result, str)
    assert "Повторено" in result

    assert any("Билет №1" in h for h in system_with_ticket.history)
    
def test_list_books_empty(system: StudySystem) -> None:
    text = system.list_books()
    assert text == "Книг пока нет."


def test_add_book_and_list(system_with_student: StudySystem) -> None:
    system_with_student.add_book("Математика", "Алгебра", "Автор", 100)

    text = system_with_student.list_books()

    assert "Математика" in text
    assert "100" in text


def test_find_book_raises_if_not_found(system_with_student: StudySystem) -> None:
    with pytest.raises(ValueError):
        system_with_student.find_book("Несуществующая книга")
    
def test_last_actions_empty(system: StudySystem) -> None:
    text = system.last_actions()
    assert "История пуста" in text


def test_last_actions_returns_recent(system_with_student: StudySystem) -> None:
    system_with_student.add_ticket(1)
    system_with_student.add_ticket_question(1, "Вопрос")

    text = system_with_student.last_actions()

    assert "Добавлен билет" in text or "добавлен билет" in text.lower()
    
def test_find_ticket_raises(system_with_student: StudySystem) -> None:
    with pytest.raises(ValueError):
        system_with_student.find_ticket(999)

def test_status_without_student(system: StudySystem) -> None:
    text = system.status()
    assert "Студент не задан" in text


def test_status_with_student(system_with_student: StudySystem) -> None:
    text = system_with_student.status()

    assert "Анна Иванова" in text
    assert "Билетов: 0" in text
    
def test_run_read_raises_if_book_not_found(system_with_student: StudySystem) -> None:
    with pytest.raises(ValueError):
        system_with_student.run_read("Нет книги", 10, Time(10, 0))