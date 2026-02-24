from .method import Method

class Testing(Method):
    
    def __init__(self, questions: int = 20):
        super().__init__("Тестирование")
        self.questions = questions
        self.correct_answers = 0
            
    def execute(self) -> str:
        if self.questions <= 0:
            raise ValueError("Количество вопросов не может быть ≤ 0")
        minutes = self.get_duration()
        questions_per_minute = 2
        solved = min(minutes * questions_per_minute, self.questions)
        
        self.correct_answers += solved
        self.correct_answers = min(self.correct_answers, self.questions)
        
        
        time_info = f"{self.start_time}-{self.end_time}: " if self.start_time else ""
        return f"{time_info} Тест: {self.correct_answers}/{self.questions} сделано"