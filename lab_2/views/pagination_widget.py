from math import ceil

from PyQt5.QtCore import pyqtSignal
from PyQt5.QtWidgets import (
    QComboBox,
    QHBoxLayout,
    QLabel,
    QPushButton,
    QWidget,
)


class PaginationWidget(QWidget):
    page_changed = pyqtSignal(int, int)

    def __init__(self, parent=None):
        super().__init__(parent)
        self.current_page = 1
        self.page_size = 10
        self.total_records = 0
        self.total_pages = 1
        self._build_ui()
        self._update_labels()

    def _build_ui(self):
        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)

        self.first_button = QPushButton("|<")
        self.prev_button = QPushButton("<")
        self.next_button = QPushButton(">")
        self.last_button = QPushButton(">|")

        self.page_size_combo = QComboBox()
        self.page_size_combo.addItems(["5", "10", "20", "50"])
        self.page_size_combo.setCurrentText(str(self.page_size))

        self.page_label = QLabel()
        self.records_label = QLabel()

        for button in [
            self.first_button,
            self.prev_button,
            self.next_button,
            self.last_button,
        ]:
            layout.addWidget(button)

        layout.addSpacing(12)
        layout.addWidget(QLabel("Записей на странице:"))
        layout.addWidget(self.page_size_combo)
        layout.addSpacing(12)
        layout.addWidget(self.page_label)
        layout.addSpacing(12)
        layout.addWidget(self.records_label)
        layout.addStretch()

        self.first_button.clicked.connect(lambda: self.set_page(1))
        self.prev_button.clicked.connect(lambda: self.set_page(self.current_page - 1))
        self.next_button.clicked.connect(lambda: self.set_page(self.current_page + 1))
        self.last_button.clicked.connect(lambda: self.set_page(self.total_pages))
        self.page_size_combo.currentTextChanged.connect(self._on_page_size_changed)

    def _on_page_size_changed(self, value: str):
        self.page_size = int(value)
        self.set_page(1)

    def update_state(self, total_records: int, current_page: int):
        self.total_records = max(0, total_records)
        self.total_pages = max(1, ceil(self.total_records / self.page_size))
        self.current_page = min(max(1, current_page), self.total_pages)
        self._update_labels()

    def set_page(self, page: int):
        page = min(max(1, page), self.total_pages)
        if page != self.current_page:
            self.current_page = page
            self._update_labels()
            self.page_changed.emit(self.current_page, self.page_size)
            return

        self._update_labels()
        self.page_changed.emit(self.current_page, self.page_size)

    def _update_labels(self):
        self.page_label.setText(
            f"Страница: {self.current_page} / {self.total_pages}"
        )
        self.records_label.setText(
            f"Всего записей: {self.total_records}, показано: {self.page_size}"
        )

        is_first = self.current_page <= 1
        is_last = self.current_page >= self.total_pages
        self.first_button.setEnabled(not is_first)
        self.prev_button.setEnabled(not is_first)
        self.next_button.setEnabled(not is_last)
        self.last_button.setEnabled(not is_last)
