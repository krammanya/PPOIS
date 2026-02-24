from typing import List, Optional

from src.models import (
    Student,
    Book,
    Ticket,
    Time,
    Read,
    Practice,
    Testing,
    Review,
)

class StudySystem:
    
        def __init__(self)->None:    
     
            self.student: Optional[Student] = None
            self.books: List[Book] = []
            self.tickets: List[Ticket] = []
        
            self.history : List[str] = []
        
        def create_student (self, name: str, surname: str, group: int) -> None:
            self.student = Student(name, surname, group)
            
            self.books = []
            self.tickets = []
            self.history = []
            
            self.history.append(f"Создан студент: {self.student}")
        
        def add_book(self, title: str, subject: str, author: str, pages: int) -> Book:
            book = Book(title, subject, author, pages)
            self.books.append(book)
            self.history.append(f"Добавлена книга: {book.get_info()}")
            return book
        
        def list_books(self) -> str:
            if not self.books:
                return "Книг пока нет."
            lines = []
            for i, book in enumerate(self.books, start=1):
                lines.append(f"{i}) {book.get_info()} (стр. {book.current_page}/{book.pages})")
            return "\n".join(lines)
        
        def find_book(self, title: str) -> Book:
            for book in self.books:
                if book.title.lower() == title.lower():
                    return book
            raise ValueError(f"Книга с названием '{title}' не найдена")
        
        def add_ticket(self, number: int) -> Ticket:
            ticket = Ticket(number)
            self.tickets.append(ticket)

           
            if self.student is not None:
                self.student.add_ticket(ticket)

            self.history.append(f"Добавлен билет №{number}")
            return ticket
        
        def add_ticket_question(self, number: int, question: str) -> None:
        
            ticket = self.find_ticket(number)
            ticket.add_question(question)
            self.history.append(f"В билет №{number} добавлен вопрос: {question}")

        def find_ticket(self, number : int)->Ticket:
            for t in self.tickets:
                if t.number == number:
                    return t
            raise ValueError (f"Билет №{number} не найден")
        
        def list_tickets(self) -> str:
            if not self.tickets:
                return "Билетов пока нет."
            lines = []
            for t in self.tickets:
                lines.append(f"- Билет №{t.number}: вопросов {len(t.questions)}, выучен={t.is_learned}")
            return "\n".join(lines)
        
        def mark_ticket_learned(self, number: int) -> None:
            ticket = self.find_ticket(number)
            ticket.mark_as_learned()
            self.history.append(f"Билет №{number} отмечен как выученный")
        
        def run_read(self, book_title: str, minutes: int, start_time: Time) -> str:
        
            book = self.find_book(book_title)

            method = Read(book)
            method.set_duration(minutes)
            method.schedule(start_time)

            result = method.execute()
            self.history.append(result)
            return result

        def run_practice(self, task_count: int, minutes: int, start_time: Time) -> str:
            method = Practice(task_count)
            method.set_duration(minutes)
            method.schedule(start_time)

            result = method.execute()
            self.history.append(result)
            return result

        def run_testing(self, questions: int, minutes: int, start_time: Time) -> str:
            method = Testing(questions)
            method.set_duration(minutes)
            method.schedule(start_time)

            result = method.execute()
            self.history.append(result)
            return result

        def run_ticket_review (self, ticket_number : int, minutes: int, start_time: Time)->str:
            ticket = self.find_ticket(ticket_number)
            
            if not ticket.questions:
                raise ValueError (f"В билете №{ticket_number} нет вопросов")
            method = Review(ticket.questions.copy())
            method.set_duration(minutes)
            method.schedule(start_time)
            
            result = method.execute()
            self.history.append(f"Билет №{ticket_number}: {result}")
            return result
        
        def status(self) -> str:
        
            student_text = str(self.student) if self.student else "Студент не задан"

            return (
                "=== Статус ===\n"
                f"Студент: {student_text}\n"
                f"Книг: {len(self.books)}\n"
                f"Билетов: {len(self.tickets)}\n"
            )

        def last_actions(self, n: int = 10) -> str:
        
            if not self.history:
                return "История пуста."
            return "=== История ===\n" + "\n".join(self.history[-n:])
        
        def show_ticket_questions(self, number: int) -> str:
            ticket = self.find_ticket(number)
            if not ticket.questions:
                return f"В билете №{number} нет вопросов."
            lines = [f"Билет №{number}:"]
            for i, q in enumerate(ticket.questions, start=1):
                lines.append(f"{i}) {q}")
            return "\n".join(lines)
        
        def to_dict(self) -> dict:
        
            return {
                "student": {
                    "name": self.student.name,
                    "surname": self.student.surname,
                    "group": self.student.group,
                } if self.student else None,

                "books": [
                    {
                    "title": b.title,
                    "subject": b.subject,
                    "author": b.author,
                    "pages": b.pages,
                    "current_page": b.current_page,
                    }
                    for b in self.books
                ],

                "tickets": [
                    {
                        "number": t.number,
                        "questions": t.questions,
                        "is_learned": t.is_learned,
                    }
                    for t in self.tickets
                ],

                "history": self.history,
            }
            
        @classmethod
        def from_dict(cls, data: dict) -> "StudySystem":
       
            system = cls()

            if data["student"]:
                s = data["student"]
                system.student = Student(s["name"], s["surname"], s["group"])

            for b in data["books"]:
                book = Book(b["title"], b["subject"], b["author"], b["pages"])
                book.current_page = b["current_page"]
                system.books.append(book)

            for t in data["tickets"]:
                ticket = Ticket(t["number"], t["questions"])
                ticket.is_learned = t["is_learned"]
                system.tickets.append(ticket)

            system.history = data["history"]

            return system     

        