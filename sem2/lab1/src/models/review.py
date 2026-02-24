from typing import List, Optional
from .method import Method

class Review(Method):
    def __init__(self, topics: Optional[List[str]] = None):
        super().__init__("Повторение")
        self.topics : List[str] = topics if topics else []
        self.reviewed_topics : List[str]=[]
                
    def execute(self) -> str:
        minutes = self.get_duration()
        topics_per_minute = 0.3 
        can_review = int(minutes * topics_per_minute)
        
        new_reviews = self.topics[:can_review]
        self.reviewed_topics.extend(new_reviews)
        self.topics = self.topics[can_review:]
        
        if not self.topics:
            self.is_completed = True
        
        time_info = f"{self.start_time}-{self.end_time}: " if self.start_time else ""
        return f"{time_info} Повторено {len(self.reviewed_topics)} тем"