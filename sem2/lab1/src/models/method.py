import abc
from typing import Optional
from .time import Time

class Method(abc.ABC):
    def __init__(self, name: str):
        self.name=name
        self.duration=Time(0,30)
        self.is_completed=False
        self.start_time: Optional[Time]=None
        self.end_time: Optional[Time]=None
                
    def set_duration(self, minutes: int):
        if minutes <= 0:
            raise ValueError("Длительность должна быть > 10 минут")
        self.duration=Time.from_minutes(minutes)
        
    def get_duration(self)->int:
        return self.duration.to_minutes()
    
    def schedule(self, start_time: Time)-> str:
        self.start_time=start_time
        total_minutes=start_time.to_minutes()+self.get_duration()
        self.end_time=Time.from_minutes(total_minutes)
        return f"{self.name}: {self.start_time} - {self.end_time}"
        
    @abc.abstractmethod
    def execute (self): pass
    