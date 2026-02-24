from .material import StudyMaterial

class Book(StudyMaterial):
    def __init__(self, title:str, subject:str, author:str, pages: int ):
        super().__init__(title, subject)
        self.author=author
        self.pages=pages
        self.current_page=0
    
    def get_info(self)-> str:
        return f"Книга '{self.title}' ({self.author}), {self.pages} стр."
    def read_page(self) ->str:
        if self.current_page<self.pages:
            self.current_page +=1
            return f"Читаю страницу {self.current_page}"
        return "Книга прочитана!"