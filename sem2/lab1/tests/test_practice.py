from src.models.practice import Practice
from src.models.time import Time


def test_practice_increases_tasks_done():
    p = Practice(task_count=10)

    p.set_duration(10)

    p.schedule(Time(12, 0))

    result = p.execute()

    assert isinstance(result, str)
    assert p.task_done > 0           
    assert p.task_done <= 10        
    assert "Решено" in result or "Все задачи решены" in result


def test_practice_can_complete_all_tasks():
   
    p = Practice(task_count=2)

    p.set_duration(10)
    p.schedule(Time(12, 0))

    result = p.execute()

    assert p.is_completed is True
    assert "Все задачи решены" in result