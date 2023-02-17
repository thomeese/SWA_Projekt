# code-with-quarkus Project

Dieses Projekt verwendet Quarkus als Hauptframework. Zusätzlich werden weitere Frameworks wie
Quarkus Qute, Keycloak, Bootstrap verwendet 


## Clonen des Projekts
Laden sie das Projekt entweder als Zip oder per git clone von Gitlab runter.


## Starten der Anwendung im dev mode
-Starten sie ein Terminal.
-Navigieren sie in das Hauptverzeichnes des Projektes.
-führen sie folgendes Kommando aus:
"./mvnw quarkus dev"

Danach wird die Anwendung gestartet und die API ist unter den Pfad: "http://localhost:8080/virtuellerKleiderschrank/v1/api" und "http://localhost:8080/virtuellerKleiderschrank/v1/web" für die jeweilige Anwendung erreichbar.

## Testen der Anwendung

Um die Anwendung zu Testen führen Sie folgende Schritte aus:
-Starten sie ein Terminal.
-Navigieren sie in das Hauptverzeichnes des Projektes.
-führen sie folgendes Kommando aus:
"./mvnw test"
