import pytest

from src.models.read import Read
from src.models.book import Book
from src.models.time import Time


def test_read_requires_book():
    r = Read(book=None)
    r.set_duration(10)
    r.schedule(Time(9, 0))

    with pytest.raises(ValueError):
        r.execute()


def test_read_increases_pages_read():
    book = Book("Азбука", "Русский", "Автор", pages=100)

    r = Read(book)
    r.set_duration(10)        # 10 минут
    r.schedule(Time(9, 0))

    result = r.execute()

    assert isinstance(result, str)
    assert "прочитано" in result
    assert r.pages_read > 0
    assert r.pages_read <= book.pages