# Boersenapp
Benotetes Projekt für Mobile Anwendungen  (Cao, Schaber, Kett, Weizmann)

Aufgabenverteilung:

Hannes Weizmann:
- Navigationsbar und Layout
- Detailansicht: Design und Funktionalität
- AppIcon
- App dreht sich mit Bildschirm
- Infomessage bei fehlender Internetverbindung
- Neues Feature: SearchView

Philipp Cao:
- Api Anbindung
- Funktionen zum holen der Daten
- RecyclerView
- Neues Feature: Stockchart

Nico Schaber:
- Aktien-News per API
--> Known Issues: 
   - Aktuell werden bei jeder Aktie lediglich die neueste News weltweit und ohne Suchbegriff angezeigt. Die News sind nicht mit Aktien verknüpft, können aber mit Stichwörtern gefiltert werden.
     Ansatz bisher: Den aktuellen Aktiennamen als Suchbegriff/"Ticker" im API Call gleich mitgeben. Beispiel Microsoft: https://api.polygon.io/v2/reference/news?ticker=Microsoft


Levin Kett:
- Kursalarm
--> Known Issues:
    - Emulator lässt in den Einstellungen des Handys die Benachrichtigungen für die App nicht zu 
    - Aktueller Wert einer Aktie kann nicht als Vergleichswert hergeholt werden
