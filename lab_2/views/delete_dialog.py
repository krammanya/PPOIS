from PyQt5.QtWidgets import (
    QComboBox,
    QDialog,
    QDialogButtonBox,
    QFormLayout,
    QHBoxLayout,
    QLabel,
    QLineEdit,
    QMessageBox,
    QSpinBox,
    QStackedWidget,
    QVBoxLayout,
    QWidget,
)


class DeleteDialog(QDialog):
    def __init__(self, ranks=None, sports=None, parent=None):
        super().__init__(parent)
        self.ranks = ranks or []
        self.sports = sports or []
        self.setWindowTitle("Удаление записей")
        self.resize(450, 260)
        self._build_ui()

    def _build_ui(self):
        layout = QVBoxLayout(self)

        mode_layout = QHBoxLayout()
        mode_layout.addWidget(QLabel("Условия удаления:"))
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

        buttons = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel)
        buttons.accepted.connect(self._validate_and_accept)
        buttons.rejected.connect(self.reject)
        layout.addWidget(buttons)

        self.mode_combo.currentIndexChanged.connect(self.filters_stack.setCurrentIndex)

    def _build_name_or_sport_page(self):
        page = QWidget()
        form = QFormLayout(page)
        self.name_sport_edit = QLineEdit()
        self.sport_combo = QComboBox()
        self.sport_combo.setEditable(True)
        self.sport_combo.addItem("")
        self.sport_combo.addItems(self.sports)
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
        self.rank_combo.addItems(self.ranks)
        form.addRow("ФИО содержит:", self.name_rank_edit)
        form.addRow("Разряд:", self.rank_combo)
        return page

    def _validate_and_accept(self):
        mode = self.mode_combo.currentIndex()

        if mode == 0:
            if (
                not self.name_sport_edit.text().strip()
                and not self.sport_combo.currentText().strip()
            ):
                QMessageBox.warning(
                    self,
                    "Ошибка",
                    "Введите ФИО или выберите вид спорта.",
                )
                return
        elif mode == 2:
            if (
                not self.name_rank_edit.text().strip()
                and not self.rank_combo.currentText().strip()
            ):
                QMessageBox.warning(
                    self,
                    "Ошибка",
                    "Введите ФИО или выберите разряд.",
                )
                return

        self.accept()

    def get_filters(self):
        mode = self.mode_combo.currentIndex()

        if mode == 0:
            return {
                "mode": "name_or_sport",
                "search_text": self.name_sport_edit.text().strip() or None,
                "sport": self.sport_combo.currentText().strip() or None,
            }

        if mode == 1:
            return {
                "mode": "titles_range",
                "min_titles": self.min_titles_spin.value(),
                "max_titles": self.max_titles_spin.value(),
            }

        return {
            "mode": "name_or_rank",
            "search_text": self.name_rank_edit.text().strip() or None,
            "rank": self.rank_combo.currentText().strip() or None,
        }
