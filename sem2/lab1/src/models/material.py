import abc

class StudyMaterial(abc.ABC) :
    def __init__(self, title: str, subject: str):
        if not title.strip():
            raise ValueError("title не может быть пустым")
        if not subject.strip():
            raise ValueError("subject не может быть пустым")
        self.title=title
        self.subject=subject
        self.is_available=True
                
    @abc.abstractmethod 
    def get_info (self) ->str : pass