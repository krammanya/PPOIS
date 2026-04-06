from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import (
    QAction,
    QFileDialog,
    QHBoxLayout,
    QMainWindow,
    QMessageBox,
    QPushButton,
    QStackedWidget,
    QStatusBar,
    QTableWidget,
    QTableWidgetItem,
    QToolBar,
    QTreeWidget,
    QTreeWidgetItem,
    QVBoxLayout,
    QWidget,
)

from controllers.controller import Controller
from views.add_dialog import AddSportsmanDialog
from views.delete_dialog import DeleteDialog
from views.pagination_widget import PaginationWidget
from views.search_dialog import SearchDialog


class MainWindow(QMainWindow):
    HEADERS = ["ФИО", "Команда", "Позиция", "Титулы", "Вид спорта", "Разряд"]

    def __init__(self):
        super().__init__()
        self.controller = Controller()
        self.current_page = 1
        self.setWindowTitle("Учет спортсменов")
        self.resize(1100, 720)
        self._build_ui()
        self._create_actions()
        self._create_menu()
        self._create_toolbar()
        self.refresh_data()

    def _build_ui(self):
        central_widget = QWidget()
        central_layout = QVBoxLayout(central_widget)

        self.view_stack = QStackedWidget()

        self.table = QTableWidget(0, len(self.HEADERS))
        self.table.setHorizontalHeaderLabels(self.HEADERS)
        self.table.horizontalHeader().setStretchLastSection(True)
        self.table.setEditTriggers(QTableWidget.NoEditTriggers)
        self.table.setSelectionBehavior(QTableWidget.SelectRows)
        self.table.setAlternatingRowColors(True)

        self.tree = QTreeWidget()
        self.tree.setHeaderLabels(["Поле", "Значение"])

        self.view_stack.addWidget(self.table)
        self.view_stack.addWidget(self.tree)

        central_layout.addWidget(self.view_stack)

        self.pagination = PaginationWidget()
        central_layout.addWidget(self.pagination)

        self.setCentralWidget(central_widget)
        self.setStatusBar(QStatusBar())
        self.pagination.page_changed.connect(self._on_page_changed)

    def _create_actions(self):
        self.add_action = QAction("Добавить запись", self)
        self.search_action = QAction("Поиск", self)
        self.delete_action = QAction("Удаление", self)
        self.load_xml_action = QAction("Загрузить XML", self)
        self.save_xml_action = QAction("Сохранить в XML", self)
        self.refresh_db_action = QAction("Загрузить из БД", self)
        self.clear_action = QAction("Очистить БД", self)

        self.add_action.triggered.connect(self.open_add_dialog)
        self.search_action.triggered.connect(self.open_search_dialog)
        self.delete_action.triggered.connect(self.open_delete_dialog)
        self.load_xml_action.triggered.connect(self.load_from_xml)
        self.save_xml_action.triggered.connect(self.save_to_xml)
        self.refresh_db_action.triggered.connect(self.refresh_data)
        self.clear_action.triggered.connect(self.clear_database)

    def _create_menu(self):
        file_menu = self.menuBar().addMenu("Файл")
        records_menu = self.menuBar().addMenu("Записи")

        records_menu.addAction(self.add_action)
        records_menu.addAction(self.search_action)
        records_menu.addAction(self.delete_action)

        file_menu.addAction(self.load_xml_action)
        file_menu.addAction(self.save_xml_action)
        file_menu.addSeparator()
        file_menu.addAction(self.refresh_db_action)
        file_menu.addAction(self.clear_action)

    def _create_toolbar(self):
        toolbar = QToolBar("Основные действия", self)
        toolbar.setMovable(False)
        self.addToolBar(toolbar)

        toolbar.addAction(self.add_action)
        toolbar.addAction(self.search_action)
        toolbar.addAction(self.delete_action)
        toolbar.addSeparator()
        toolbar.addAction(self.load_xml_action)
        toolbar.addAction(self.save_xml_action)

    def open_add_dialog(self):
        dialog = AddSportsmanDialog(ranks=self.controller.get_unique_ranks(), parent=self)
        if dialog.exec_():
            self.controller.add_sportsman(dialog.get_sportsman())
            self.refresh_data(last_page=True)
            self.statusBar().showMessage(
                "Запись добавлена и сохранена в базе данных.",
                5000,
            )

    def open_search_dialog(self):
        SearchDialog(self.controller, self).exec_()

    def open_delete_dialog(self):
        dialog = DeleteDialog(
            ranks=self.controller.get_unique_ranks(),
            sports=self.controller.get_unique_sports(),
            parent=self,
        )
        if not dialog.exec_():
            return

        filters = dialog.get_filters()
        deleted_count = self._delete_by_filters(filters)

        if deleted_count:
            self.refresh_data()
            QMessageBox.information(
                self,
                "Удаление завершено",
                f"Удалено записей: {deleted_count}.",
            )
        else:
            QMessageBox.information(
                self,
                "Удаление завершено",
                "По указанным условиям записи не найдены.",
            )

    def _delete_by_filters(self, filters: dict) -> int:
        mode = filters["mode"]
        if mode == "name_or_sport":
            return self.controller.delete_by_name_or_sport(
                filters["search_text"],
                filters["sport"],
            )
        if mode == "titles_range":
            return self.controller.delete_by_titles_range(
                filters["min_titles"],
                filters["max_titles"],
            )
        return self.controller.delete_by_name_or_rank(
            filters["search_text"],
            filters["rank"],
        )

    def load_from_xml(self):
        replace_existing = self._choose_xml_import_mode()
        if replace_existing is None:
            return

        file_path, _ = QFileDialog.getOpenFileName(
            self,
            "Загрузка XML-файла",
            "",
            "XML files (*.xml)",
        )
        if not file_path:
            return

        loaded_count = self.controller.import_from_xml(
            file_path,
            replace_existing=replace_existing,
        )
        self.refresh_data()

        if replace_existing:
            message = f"Загружено записей из XML: {loaded_count}."
        else:
            message = f"Добавлено новых записей: {loaded_count}. Дубликаты пропущены."

        QMessageBox.information(self, "Импорт XML завершен", message)

    def _choose_xml_import_mode(self):
        message_box = QMessageBox(self)
        message_box.setWindowTitle("Режим импорта XML")
        message_box.setText("Выберите режим загрузки данных из XML.")

        add_button = message_box.addButton("Добавить из XML", QMessageBox.AcceptRole)
        replace_button = message_box.addButton(
            "Заменить данными из XML",
            QMessageBox.DestructiveRole,
        )
        message_box.addButton("Отмена", QMessageBox.RejectRole)

        message_box.exec_()
        clicked_button = message_box.clickedButton()

        if clicked_button == add_button:
            return False
        if clicked_button == replace_button:
            return True
        return None

    def save_to_xml(self):
        file_path, _ = QFileDialog.getSaveFileName(
            self,
            "Сохранение XML-файла",
            "",
            "XML files (*.xml)",
        )
        if not file_path:
            return

        if not file_path.lower().endswith(".xml"):
            file_path += ".xml"

        self.controller.export_to_xml(file_path)
        QMessageBox.information(
            self,
            "Сохранение",
            "Данные успешно сохранены в XML-файл.",
        )

    def clear_database(self):
        result = QMessageBox.question(
            self,
            "Очистка БД",
            "Удалить все записи из базы данных?",
        )
        if result != QMessageBox.Yes:
            return

        self.controller.clear_all()
        self.refresh_data()
        self.statusBar().showMessage("База данных очищена.", 5000)

    def refresh_data(self, last_page: bool = False):
        total = self.controller.get_total_count()
        if last_page and total > 0:
            page_size = self.pagination.page_size
            self.current_page = ((total - 1) // page_size) + 1
        else:
            self.current_page = max(1, min(self.current_page, self.pagination.total_pages))

        self.pagination.update_state(total, self.current_page)
        self.current_page = self.pagination.current_page
        self._load_current_page()

    def _on_page_changed(self, page: int, _page_size: int):
        self.current_page = page
        self._load_current_page()

    def _load_current_page(self):
        page_size = self.pagination.page_size
        total = self.controller.get_total_count()
        self.pagination.update_state(total, self.current_page)
        self.current_page = self.pagination.current_page

        offset = (self.current_page - 1) * page_size
        sportsmen = self.controller.get_sportsmen_paginated(page_size, offset)
        self._fill_table(sportsmen)
        self._fill_tree(sportsmen, offset)
        self.statusBar().showMessage(
            f"Текущая страница: {self.current_page}. Всего записей в БД: {total}.",
            4000,
        )

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

    def _fill_tree(self, sportsmen, offset: int):
        self.tree.clear()
        for index, sportsman in enumerate(sportsmen, start=offset + 1):
            root = QTreeWidgetItem(self.tree, [f"Спортсмен {index}", sportsman.full_name])
            fields = [
                ("ФИО", sportsman.full_name),
                ("Команда", sportsman.team),
                ("Позиция", sportsman.position),
                ("Количество титулов", str(sportsman.titles)),
                ("Вид спорта", sportsman.sport),
                ("Разряд", sportsman.rank),
            ]
            for field_name, value in fields:
                QTreeWidgetItem(root, [field_name, value])

        self.tree.expandAll()

    def closeEvent(self, event):
        self.controller.close()
        super().closeEvent(event)
