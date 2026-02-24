import pytest
from src.models.time import Time

def test_time_to_minutes():
    t = Time(1, 30)
    assert t.to_minutes() == 90
    
def test_time_error():
    with pytest.raises(ValueError):
        Time(-1, 30)
