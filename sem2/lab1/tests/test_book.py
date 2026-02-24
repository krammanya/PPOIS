from src.models.book import Book


class TestBook:

    def test_get_info_returns_correct_string(self):
        book = Book("Азбука", "Чтение", "Автор", 50)

        info = book.get_info()

        assert "Азбука" in info
        assert "Автор" in info
        assert "50" in info

    def test_read_page_increments_current_page(self):
        book = Book("Test", "Math", "Author", 3)

        result1 = book.read_page()
        result2 = book.read_page()

        assert book.current_page == 2
        assert "1" in result1
        assert "2" in result2

    def test_read_page_stops_at_last_page(self):
        book = Book("Test", "Math", "Author", 2)

        book.read_page()
        book.read_page()
        result = book.read_page()

        assert book.current_page == 2  # не превышает pages
        assert result == "Книга прочитана!"

    def test_read_page_never_exceeds_total_pages(self):
        book = Book("Test", "Math", "Author", 1)

        book.read_page()
        book.read_page()
        book.read_page()

        assert book.current_page == 1