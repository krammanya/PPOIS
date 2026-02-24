import pytest

from src.models.testing import Testing
from src.models.time import Time


def test_testing_returns_string_and_progress():
    t = Testing(questions=10)

    t.set_duration(5)         
    t.schedule(Time(10, 0))

    result = t.execute()

    assert isinstance(result, str)
    assert "Тест" in result
    assert 0 <= t.correct_answers <= 10  # прогресс в пределах


def test_testing_raises_if_questions_zero_or_negative():
    t = Testing(questions=0)
    t.set_duration(5)
    t.schedule(Time(10, 0))

    with pytest.raises(ValueError):
        t.execute()