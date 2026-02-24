from typing import List, Optional

class Ticket:
    def __init__(self, number: int, questions: Optional[List[str]] = None):
        self.number = number
        self.questions : List[str]=questions if questions else []
        self.is_learned = False
    
    def add_question(self, question: str):
        self.questions.append(question)
    
    def mark_as_learned(self):
        self.is_learned = True