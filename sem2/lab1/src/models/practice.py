from .method import Method


class Practice(Method):
    def __init__(self, task_count: int=10):
        super().__init__("Практика")
        self.task_count=task_count
        self.task_done=0
    
    def execute(self)-> str:
        minutes = self.get_duration()
        task_per_minute = 0.5
        can_do = int (minutes * task_per_minute)
        
        self.task_done = min(self.task_done +can_do, self.task_count)
        
        if self.task_done >= self.task_count:
            self.is_completed = True
            return "Все задачи решены!"
        time_info = f"{self.start_time}-{self.end_time}: " if self.start_time else ""
        return f"{time_info} Решено {self.task_done}/{self.task_count} задач"

