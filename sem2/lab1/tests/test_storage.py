import json


from src.services.study_system import StudySystem
import src.services.storage as storage  


def test_save_creates_file_and_writes_json(tmp_path, monkeypatch):
    
    fake_file = tmp_path / "state.json"

    monkeypatch.setattr(storage, "DATA_FILE", fake_file)

    system = StudySystem()
    system.create_student("Анна", "Иванова", 123)

    storage.save(system)

    assert fake_file.exists()

    data = json.loads(fake_file.read_text(encoding="utf-8"))
    assert data["student"]["name"] == "Анна"
    assert data["student"]["group"] == 123


def test_load_returns_new_system_if_file_missing(tmp_path, monkeypatch):
    
    fake_file = tmp_path / "missing.json"
    monkeypatch.setattr(storage, "DATA_FILE", fake_file)

    system = storage.load()

    assert system.student is None
    assert system.books == []
    assert system.tickets == []


def test_load_returns_new_system_if_json_broken(tmp_path, monkeypatch, capsys):

    fake_file = tmp_path / "state.json"
    fake_file.write_text('{"student": {"name": "Анна", }', encoding="utf-8")  # ошибка JSON

    monkeypatch.setattr(storage, "DATA_FILE", fake_file)

    system = storage.load()

    assert system.student is None
    assert system.books == []
    assert system.tickets == []

    out = capsys.readouterr().out
    assert "поврежд" in out.lower() or "ошибка" in out.lower()