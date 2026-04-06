from typing import List, Optional, Tuple

from database.database import Database
from models.sportsman import Sportsman
from models.xml_handler import XMLHandler


class Controller:
    def __init__(self):
        self.db = Database()

    def add_sportsman(self, sportsman: Sportsman):
        self.db.add_sportsman(sportsman)

    def get_sportsmen_paginated(self, limit: int, offset: int) -> List[Sportsman]:
        query = "SELECT * FROM sportsmen LIMIT ? OFFSET ?"
        cursor = self.db.conn.execute(query, (limit, offset))
        rows = cursor.fetchall()

        return [self._row_to_sportsman(row) for row in rows]

    def get_total_count(self) -> int:
        return self.db.get_total_count()

    def search_by_name_or_sport(
        self,
        search_text: Optional[str] = None,
        sport: Optional[str] = None,
        limit: int = 10,
        offset: int = 0,
    ) -> Tuple[List[Sportsman], int]:
        if not search_text and not sport:
            return [], 0

        params = []
        conditions = []

        if search_text:
            conditions.append("full_name LIKE ?")
            params.append(f"%{search_text}%")

        if sport:
            conditions.append("sport = ?")
            params.append(sport)

        where_clause = " OR ".join(conditions)

        query = f"""
        SELECT * FROM sportsmen
        WHERE {where_clause}
        LIMIT ? OFFSET ?
        """
        cursor = self.db.conn.execute(query, (*params, limit, offset))
        rows = cursor.fetchall()

        count_query = f"""
        SELECT COUNT(*) FROM sportsmen
        WHERE {where_clause}
        """
        total = self.db.conn.execute(count_query, params).fetchone()[0]

        return [self._row_to_sportsman(row) for row in rows], total

    def search_by_titles_range(
        self, min_titles: int, max_titles: int, limit: int = 10, offset: int = 0
    ) -> Tuple[List[Sportsman], int]:
        if min_titles > max_titles:
            min_titles, max_titles = max_titles, min_titles

        query = """
        SELECT * FROM sportsmen
        WHERE titles BETWEEN ? AND ?
        LIMIT ? OFFSET ?
        """
        cursor = self.db.conn.execute(query, (min_titles, max_titles, limit, offset))
        rows = cursor.fetchall()

        count_query = """
        SELECT COUNT(*) FROM sportsmen
        WHERE titles BETWEEN ? AND ?
        """
        total = self.db.conn.execute(count_query, (min_titles, max_titles)).fetchone()[0]

        return [self._row_to_sportsman(row) for row in rows], total

    def search_by_name_or_rank(
        self,
        search_text: Optional[str] = None,
        rank: Optional[str] = None,
        limit: int = 10,
        offset: int = 0,
    ) -> Tuple[List[Sportsman], int]:
        if not search_text and not rank:
            return [], 0

        params = []
        conditions = []

        if search_text:
            conditions.append("full_name LIKE ?")
            params.append(f"%{search_text}%")

        if rank:
            conditions.append("rank = ?")
            params.append(rank)

        where_clause = " OR ".join(conditions)

        query = f"""
        SELECT * FROM sportsmen
        WHERE {where_clause}
        LIMIT ? OFFSET ?
        """
        cursor = self.db.conn.execute(query, (*params, limit, offset))
        rows = cursor.fetchall()

        count_query = f"""
        SELECT COUNT(*) FROM sportsmen
        WHERE {where_clause}
        """
        total = self.db.conn.execute(count_query, params).fetchone()[0]

        return [self._row_to_sportsman(row) for row in rows], total

    def delete_by_name_or_sport(
        self, search_text: Optional[str] = None, sport: Optional[str] = None
    ) -> int:
        if not search_text and not sport:
            return 0

        params = []
        conditions = []

        if search_text:
            conditions.append("full_name LIKE ?")
            params.append(f"%{search_text}%")

        if sport:
            conditions.append("sport = ?")
            params.append(sport)

        where_clause = " OR ".join(conditions)

        query = f"DELETE FROM sportsmen WHERE {where_clause}"
        cursor = self.db.conn.execute(query, params)
        self.db.conn.commit()

        return cursor.rowcount

    def delete_by_titles_range(self, min_titles: int, max_titles: int) -> int:
        if min_titles > max_titles:
            min_titles, max_titles = max_titles, min_titles

        query = "DELETE FROM sportsmen WHERE titles BETWEEN ? AND ?"
        cursor = self.db.conn.execute(query, (min_titles, max_titles))
        self.db.conn.commit()

        return cursor.rowcount

    def delete_by_name_or_rank(
        self, search_text: Optional[str] = None, rank: Optional[str] = None
    ) -> int:
        if not search_text and not rank:
            return 0

        params = []
        conditions = []

        if search_text:
            conditions.append("full_name LIKE ?")
            params.append(f"%{search_text}%")

        if rank:
            conditions.append("rank = ?")
            params.append(rank)

        where_clause = " OR ".join(conditions)

        query = f"DELETE FROM sportsmen WHERE {where_clause}"
        cursor = self.db.conn.execute(query, params)
        self.db.conn.commit()

        return cursor.rowcount

    def get_unique_sports(self) -> List[str]:
        query = "SELECT DISTINCT sport FROM sportsmen WHERE sport != ''"
        cursor = self.db.conn.execute(query)
        return [row[0] for row in cursor.fetchall()]

    def get_unique_ranks(self) -> List[str]:
        return [
            "1-й юношеский",
            "2-й разряд",
            "3-й разряд",
            "КМС",
            "Мастер спорта",
        ]

    def clear_all(self):
        self.db.conn.execute("DELETE FROM sportsmen")
        self.db.conn.commit()

    def close(self):
        self.db.close()

    def _row_to_sportsman(self, row) -> Sportsman:
        return Sportsman(
            full_name=row[1],
            team=row[2],
            position=row[3],
            titles=row[4],
            sport=row[5],
            rank=row[6],
        )

    def export_to_xml(self, file_path: str):
        sportsmen = self.db.get_all_sportsmen()
        XMLHandler.save_to_xml(sportsmen, file_path)

    def import_from_xml(self, file_path: str, replace_existing: bool = False) -> int:
        sportsmen = XMLHandler.load_from_xml(file_path)

        if replace_existing:
            self.clear_all()

        existing_full_names = {
            self._extract_name_key(sportsman.full_name)
            for sportsman in self.db.get_all_sportsmen()
            if self._extract_name_key(sportsman.full_name)
        }
        added_count = 0

        for sportsman in sportsmen:
            name_key = self._extract_name_key(sportsman.full_name)

            if name_key and name_key in existing_full_names:
                continue

            self.db.add_sportsman(sportsman)
            added_count += 1

            if name_key:
                existing_full_names.add(name_key)

        return added_count

    def _extract_name_key(self, full_name: str) -> str:
        parts = full_name.strip().split()
        if len(parts) < 2:
            return parts[0].casefold() if parts else ""
        return f"{parts[0].casefold()} {parts[1].casefold()}"
