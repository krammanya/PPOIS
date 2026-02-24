from src.models.ticket import Ticket

def test_ticket_validation():
    t= Ticket(1)
    
    assert t.number== 1
    assert t.questions ==[]
    assert t.is_learned is False
    
def test_mark_as_learned_true():
    ticket =Ticket(3)
    ticket.mark_as_learned()
    assert ticket.is_learned is True

def test_ticket_with_questions():
    ticket = Ticket(4, ["Вопрос 1, Вопрос 2"])
    assert ticket.questions == ["Вопрос 1, Вопрос 2"]
    assert ticket.is_learned is False