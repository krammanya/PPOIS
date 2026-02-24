import json
from pathlib import Path

from src.services.study_system import StudySystem


DATA_FILE = Path("data/state.json")


def save(system: StudySystem) -> None:
    DATA_FILE.parent.mkdir(exist_ok=True)

    with open(DATA_FILE, "w", encoding="utf-8") as f:
        json.dump(system.to_dict(), f, indent=4, ensure_ascii=False)
   

def load() -> StudySystem:
    if not DATA_FILE.exists():
        return StudySystem()

    try:
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            data = json.load(f)
        return StudySystem.from_dict(data)

    except json.JSONDecodeError:
        print("Ошибка: файл состояния повреждён.")
        print("Создаётся новая система.")
        return StudySystem()