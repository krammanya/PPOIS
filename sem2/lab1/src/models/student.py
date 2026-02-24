from typing import List
from .ticket import Ticket

class Student:
    """Класс студент"""
    def __init__(self, name: str, surname: str, group: int):
        self.name=name
        self.surname=surname
        self.group=group
        self.tickets: List[Ticket]=[]
    def get_full_name(self)->str:
        return f"{self.name} {self.surname}"
    def add_ticket(self, ticket):
        self.tickets.append(ticket)
    def __str__(self)-> str:
        return f"{self.get_full_name()}, группа {self.group}"
    
    