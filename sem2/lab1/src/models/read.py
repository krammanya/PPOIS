from typing import Optional
from .method import Method
from .book import Book

class Read(Method):
    
    def __init__(self, book: Optional[Book] = None):
        super().__init__("Чтение")
        self.book=book
        self.pages_read=0
    
        
    def set_book(self,book:Book):
        self.book=book
    
    def execute(self) -> str:
        if not self.book:
             raise ValueError("Книга не задана! Сначала установите книгу через set_book()")
        
        minutes = self.get_duration()
        pages_per_minute = 2
        can_read = minutes * pages_per_minute
        
        self.pages_read =min(self.pages_read + can_read, self.book.pages)
        time_info = f"{self.start_time}-{self.end_time}: " if self.start_time else ""
        return f"{time_info} {self.book.title}: прочитано {self.pages_read}/{self.book.pages} стр."