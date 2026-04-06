import sqlite3
from typing import List, Tuple
from models.sportsman import Sportsman

class Database:
    def __init__(self, db_name="data/sportsmen.db"):
        self.conn = sqlite3.connect(db_name) #подключение к файлу базы данных 
        self.create_table() #сразу же создаем таблицу

    def create_table(self):
        query = """
        CREATE TABLE IF NOT EXISTS sportsmen(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            full_name TEXT,
            team TEXT,
            position TEXT,
            titles INTEGER,
            sport TEXT,
            rank TEXT
        );
        """
        self.conn.execute(query) # выполняет SQL запрос 
        self.conn.commit() # сохраняет изменения
        
    def add_sportsman(self, sportsman: Sportsman):
        query = """
        INSERT INTO sportsmen (full_name, team, position, titles, sport, rank)
        VALUES (?, ?, ?, ?, ?, ?)
        """
        self.conn.execute(query, (
            sportsman.full_name,
            sportsman.team,
            sportsman.position,
            sportsman.titles,
            sportsman.sport,
            sportsman.rank
        ))
        self.conn.commit()
        
    def get_all_sportsmen(self):
        query = "SELECT * FROM sportsmen"
        cursor = self.conn.execute(query)
        rows = cursor.fetchall()
        
        # Преобразуем строки базы данных в объекты Sportsman
        sportsmen = []
        for row in rows:
            sportsman = Sportsman(
                full_name=row[1],
                team=row[2],
                position=row[3],
                titles=row[4],
                sport=row[5],
                rank=row[6]
            )
            sportsmen.append(sportsman)
        return sportsmen
    
    def get_total_count(self):
        query = "SELECT COUNT(*) FROM sportsmen"
        cursor = self.conn.execute(query)
        return cursor.fetchone()[0]
    
    def close(self):
        self.conn.close()