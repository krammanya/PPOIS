from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import (
    QComboBox,
    QDialog,
    QFormLayout,
    QHBoxLayout,
    QLabel,
    QLineEdit,
    QPushButton,
    QSpinBox,
    QStackedWidget,
    QTableWidget,
    QTableWidgetItem,
    QVBoxLayout,
    QWidget,
)

from views.pagination_widget import PaginationWidget


class SearchDialog(QDialog):
    HEADERS = ["ФИО", "Команда", "Позиция", "Титулы", "Вид спорта", "Разряд"]

    def __init__(self, controller, parent=None):
        super().__init__(parent)
        self.controller = controller
        self.current_page = 1
        self.setWindowTitle("Поиск записей")
        self.resize(900, 560)
        self._build_ui()
        self._run_search()

    def _build_ui(self):
        layout = QVBoxLayout(self)

        mode_layout = QHBoxLayout()
        mode_layout.addWidget(QLabel("Условия поиска:"))
        self.mode_combo = QComboBox()
        self.mode_combo.addItems(
            [
                "По ФИО или виду спорта",
                "По количеству титулов",
                "По ФИО или разряду",
            ]
        )
        mode_layout.addWidget(self.mode_combo)
        mode_layout.addStretch()
        layout.addLayout(mode_layout)

        self.filters_stack = QStackedWidget()
        self.filters_stack.addWidget(self._build_name_or_sport_page())
        self.filters_stack.addWidget(self._build_titles_page())
        self.filters_stack.addWidget(self._build_name_or_rank_page())
        layout.addWidget(self.filters_stack)

        actions_layout = QHBoxLayout()
        self.search_button = QPushButton("Найти")
        self.reset_button = QPushButton("Сбросить")
        self.result_label = QLabel("Результаты поиска")
        actions_layout.addWidget(self.search_button)
        actions_layout.addWidget(self.reset_button)
        actions_layout.addStretch()
        actions_layout.addWidget(self.result_label)
        layout.addLayout(actions_layout)

        self.table = QTableWidget(0, len(self.HEADERS))
        self.table.setHorizontalHeaderLabels(self.HEADERS)
        self.table.horizontalHeader().setStretchLastSection(True)
        self.table.setEditTriggers(QTableWidget.NoEditTriggers)
        self.table.setSelectionBehavior(QTableWidget.SelectRows)
        self.table.setSelectionMode(QTableWidget.SingleSelection)
        self.table.setAlternatingRowColors(True)
        layout.addWidget(self.table)

        self.pagination = PaginationWidget()
        layout.addWidget(self.pagination)

        self.mode_combo.currentIndexChanged.connect(self._on_mode_changed)
        self.search_button.clicked.connect(self._on_new_search)
        self.reset_button.clicked.connect(self._reset_filters)
        self.pagination.page_changed.connect(self._on_page_changed)

    def _build_name_or_sport_page(self):
        page = QWidget()
        form = QFormLayout(page)
        self.name_sport_edit = QLineEdit()
        self.sport_combo = QComboBox()
        self.sport_combo.setEditable(True)
        self.sport_combo.addItem("")
        self.sport_combo.addItems(self.controller.get_unique_sports())
        form.addRow("ФИО содержит:", self.name_sport_edit)
        form.addRow("Вид спорта:", self.sport_combo)
        return page

    def _build_titles_page(self):
        page = QWidget()
        form = QFormLayout(page)
        self.min_titles_spin = QSpinBox()
        self.max_titles_spin = QSpinBox()
        self.min_titles_spin.setRange(0, 10000)
        self.max_titles_spin.setRange(0, 10000)
        self.max_titles_spin.setValue(10)
        form.addRow("Нижний предел:", self.min_titles_spin)
        form.addRow("Верхний предел:", self.max_titles_spin)
        return page

    def _build_name_or_rank_page(self):
        page = QWidget()
        form = QFormLayout(page)
        self.name_rank_edit = QLineEdit()
        self.rank_combo = QComboBox()
        self.rank_combo.addItem("")
        self.rank_combo.addItems(self.controller.get_unique_ranks())
        form.addRow("ФИО содержит:", self.name_rank_edit)
        form.addRow("Разряд:", self.rank_combo)
        return page

    def _on_mode_changed(self, index: int):
        self.filters_stack.setCurrentIndex(index)
        self._on_new_search()

    def _reset_filters(self):
        self.name_sport_edit.clear()
        self.sport_combo.setCurrentIndex(0)
        self.min_titles_spin.setValue(0)
        self.max_titles_spin.setValue(10)
        self.name_rank_edit.clear()
        self.rank_combo.setCurrentIndex(0)
        self._on_new_search()

    def _on_new_search(self):
        self.current_page = 1
        self._run_search()

    def _on_page_changed(self, page: int, _page_size: int):
        self.current_page = page
        self._run_search()

    def _run_search(self):
        page_size = self.pagination.page_size
        offset = (self.current_page - 1) * page_size
        mode = self.mode_combo.currentIndex()

        if mode == 0:
            rows, total = self._search_name_or_sport(page_size, offset)
        elif mode == 1:
            rows, total = self._search_titles_range(page_size, offset)
        else:
            rows, total = self._search_name_or_rank(page_size, offset)

        self.pagination.update_state(total, self.current_page)
        self.current_page = self.pagination.current_page
        self._fill_table(rows)
        self.result_label.setText(f"Найдено записей: {total}")

    def _search_name_or_sport(self, page_size: int, offset: int):
        name = self.name_sport_edit.text().strip() or None
        sport = self.sport_combo.currentText().strip() or None
        return self.controller.search_by_name_or_sport(name, sport, page_size, offset)

    def _search_titles_range(self, page_size: int, offset: int):
        return self.controller.search_by_titles_range(
            self.min_titles_spin.value(),
            self.max_titles_spin.value(),
            page_size,
            offset,
        )

    def _search_name_or_rank(self, page_size: int, offset: int):
        name = self.name_rank_edit.text().strip() or None
        rank = self.rank_combo.currentText().strip() or None
        return self.controller.search_by_name_or_rank(name, rank, page_size, offset)

    def _fill_table(self, sportsmen):
        self.table.setRowCount(len(sportsmen))
        for row_index, sportsman in enumerate(sportsmen):
            values = [
                sportsman.full_name,
                sportsman.team,
                sportsman.position,
                str(sportsman.titles),
                sportsman.sport,
                sportsman.rank,
            ]
            for column, value in enumerate(values):
                item = QTableWidgetItem(value)
                item.setTextAlignment(
                    Qt.AlignCenter if column == 3 else Qt.AlignLeft | Qt.AlignVCenter
                )
                self.table.setItem(row_index, column, item)

        self.table.resizeColumnsToContents()
