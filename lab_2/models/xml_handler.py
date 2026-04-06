import xml.dom.minidom
import xml.sax
from typing import List

from models.sportsman import Sportsman


class XMLHandler:
    """Работа с XML-файлами через DOM для записи и SAX для чтения."""

    @staticmethod
    def save_to_xml(sportsmen: List[Sportsman], filename: str):
        doc = xml.dom.minidom.Document()
        root = doc.createElement("sportsmen")
        doc.appendChild(root)

        for sportsman in sportsmen:
            sportsman_elem = doc.createElement("sportsman")
            XMLHandler._append_text_element(doc, sportsman_elem, "full_name", sportsman.full_name)
            XMLHandler._append_text_element(doc, sportsman_elem, "team", sportsman.team)
            XMLHandler._append_text_element(doc, sportsman_elem, "position", sportsman.position)
            XMLHandler._append_text_element(doc, sportsman_elem, "titles", str(sportsman.titles))
            XMLHandler._append_text_element(doc, sportsman_elem, "sport", sportsman.sport)
            XMLHandler._append_text_element(doc, sportsman_elem, "rank", sportsman.rank)
            root.appendChild(sportsman_elem)

        with open(filename, "w", encoding="utf-8") as file:
            file.write(doc.toprettyxml(indent="  ", encoding="utf-8").decode("utf-8"))

    @staticmethod
    def load_from_xml(filename: str) -> List[Sportsman]:
        handler = SportsmanHandler()
        parser = xml.sax.make_parser()
        parser.setContentHandler(handler)
        parser.parse(filename)
        return handler.get_sportsmen()

    @staticmethod
    def _append_text_element(doc, parent, tag_name: str, value: str):
        element = doc.createElement(tag_name)
        element.appendChild(doc.createTextNode(value))
        parent.appendChild(element)


class SportsmanHandler(xml.sax.ContentHandler):
    def __init__(self):
        super().__init__()
        self.sportsmen: List[Sportsman] = []
        self.current_sportsman = None
        self.current_tag = ""
        self.current_value_parts: List[str] = []

    def startElement(self, name, attrs):
        self.current_tag = name
        if name == "sportsman":
            self.current_sportsman = {}
        self.current_value_parts = []

    def characters(self, content):
        if self.current_tag:
            self.current_value_parts.append(content)

    def endElement(self, name):
        value = "".join(self.current_value_parts).strip()

        if name == "sportsman" and self.current_sportsman is not None:
            sportsman = Sportsman(
                full_name=self.current_sportsman.get("full_name", ""),
                team=self.current_sportsman.get("team", ""),
                position=self.current_sportsman.get("position", ""),
                titles=int(self.current_sportsman.get("titles", 0) or 0),
                sport=self.current_sportsman.get("sport", ""),
                rank=self.current_sportsman.get("rank", ""),
            )
            self.sportsmen.append(sportsman)
            self.current_sportsman = None
        elif name in ["full_name", "team", "position", "titles", "sport", "rank"]:
            if self.current_sportsman is not None:
                self.current_sportsman[name] = value

        self.current_tag = ""
        self.current_value_parts = []

    def get_sportsmen(self) -> List[Sportsman]:
        return self.sportsmen
