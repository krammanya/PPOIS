from PyQt5.QtWidgets import (
    QComboBox,
    QDialog,
    QDialogButtonBox,
    QFormLayout,
    QLineEdit,
    QMessageBox,
    QSpinBox,
    QVBoxLayout,
)

from models.sportsman import Sportsman


class AddSportsmanDialog(QDialog):
    def __init__(self, ranks=None, parent=None):
        super().__init__(parent)
        self.ranks = ranks or []
        self.setWindowTitle("Добавление записи")
        self.resize(420, 220)
        self._build_ui()

    def _build_ui(self):
        layout = QVBoxLayout(self)
        form = QFormLayout()

        self.full_name_edit = QLineEdit()
        self.team_edit = QLineEdit()
        self.position_edit = QLineEdit()
        self.titles_spin = QSpinBox()
        self.titles_spin.setRange(0, 10_000)
        self.sport_edit = QLineEdit()
        self.rank_combo = QComboBox()
        self.rank_combo.setEditable(True)
        self.rank_combo.addItems(self.ranks)

        form.addRow("ФИО:", self.full_name_edit)
        form.addRow("Команда:", self.team_edit)
        form.addRow("Позиция:", self.position_edit)
        form.addRow("Количество титулов:", self.titles_spin)
        form.addRow("Вид спорта:", self.sport_edit)
        form.addRow("Разряд:", self.rank_combo)

        buttons = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel)
        buttons.accepted.connect(self._validate_and_accept)
        buttons.rejected.connect(self.reject)

        layout.addLayout(form)
        layout.addWidget(buttons)

    def _validate_and_accept(self):
        if not self.full_name_edit.text().strip():
            QMessageBox.warning(self, "Ошибка", "Введите ФИО спортсмена.")
            return

        if not self.sport_edit.text().strip():
            QMessageBox.warning(self, "Ошибка", "Введите вид спорта.")
            return

        self.accept()

    def get_sportsman(self) -> Sportsman:
        return Sportsman(
            full_name=self.full_name_edit.text().strip(),
            team=self.team_edit.text().strip(),
            position=self.position_edit.text().strip(),
            titles=self.titles_spin.value(),
            sport=self.sport_edit.text().strip(),
            rank=self.rank_combo.currentText().strip(),
        )
