class Time:
    def __init__(self, hours: int = 0, minutes: int = 0):
        if hours < 0 or minutes < 0:
            raise ValueError("Время не может быть отрицательным")
        if minutes >= 60:
            hours += minutes // 60
            minutes = minutes % 60  
        
        self.hours = hours
        self.minutes = minutes
        
    def to_minutes(self) -> int:
        return self.hours * 60 + self.minutes
    
    @classmethod    
    def from_minutes(cls, minutes: int):
        return cls(minutes // 60, minutes % 60)
        
    def add(self, other: 'Time') -> 'Time':
        total = self.to_minutes() + other.to_minutes()
        return Time.from_minutes(total)
    
    def __str__(self) -> str:
        return f"{self.hours:02d}:{self.minutes:02d}"
    
    def __eq__(self, other):
        return self.to_minutes() == other.to_minutes()