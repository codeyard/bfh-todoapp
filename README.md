# Todo Application

* CAS: Software Development (SD-HS20)
* Kurs: Web Applikationen
* Gruppe 1:
    * Igor Stojanovic @stoji2
    * Sabina Löffel @loffs2
    * Christophe Leupi @leupc1
    * Raphael Gerber @gerbr19

***

## Allgemeine Beschreibung

Die Todo Applikation der Gruppe 1 bietet die Möglichkeit, Todos pro Benutzer zu erstellen, bearbeiten und zu löschen. Die 
Todos bestehen aus einem Titel (Pflichtfeld), einer optionalen Kategorie und einem Fälligkeitsdatum. Ebenfalls können 
Todos als "important" oder "completed" markiert werden. Die Todos können in der Übersicht nach Kategorie gefiltert 
werden.

Damit jeder Benutzer nur seine eigenen Todos einsehen und bearbeiten kann, sind diese durch ein persönliches Login 
geschützt.

## Design

### Domänenmodell
![Domain Model](docs/DomainModel.png)

### Page Flow
![Page Flow](docs/PageFlow.png)

### TODO: Klassendesign

#### MVC-Modell: 
![Page Flow](docs/mvc.png)


## TODO: Implementierung

### Zusätzlich implementierte Features

Die Projektgruppe hat zusätzliche Features erfasst:

* Statistiken
* Zusätzliche Filtermethoden
* Löschung aller erledigten Todos
* Weiterleitung von (nicht) eingeloggten Benutzer
* Error-Page


#### Statistiken
Hat ein Benutzer mehr als ein Todo erfasst, wird unterhalb der Übersichtsliste eine Statistik angezeigt, 
die auf einen Blick zeigt, wie viele Todos vorhanden und wie viele davon offen, wichtig oder gar überfällig sind.


#### Zusätzliche Filtermethoden
Ein Benutzer hat die Möglichkeit, neben der Filterung nach Kategorien, auch nach dem Status (incomplete, complete, 
overdue, important) der Todos zu filtern. Eine Filterkombination aus Kategorie und Status ist auch möglich. Wenn
ein Filter aktiv ist, erscheint rechts daneben ein Papierkorb-Icon über welches die Filter gelöscht, sprich
bzw. zurückgesetzt werden können.


#### Löschung aller erledigten Todos
Der Benutzer hat über den Button `Delete completed todos` die Möglichkeit, mit nur einem Klick alle erledigten 
Todos zu löschen. Dieser Button wird nur angezeigt, wenn der Benutzer tatsächlich erledigte Todos hat und kein
Filter aktiv ist.


#### Weiterleitung von (nicht) eingeloggten Benutzer
Eingeloggte Benutzer werden beim Versuch, auf die Registrier- oder Loginseite zuzugreifen, direkt auf die Todoliste 
weitergeleitet, solange die Session noch aktiv ist.
Gleichzeitig können nicht eingeloggte Benutzer nicht auf den Userbereich zugreifen und werden zur Loginseite 
weitergeleitet.


#### Error seite
Für das Abfangen von möglichen Fehlern und beim Zugriff auf nicht vorhandene Ressourcen (404) wird eine Error-Page 
angezeigt.


## TODO: Inbetriebnahme
Die Applikation lässt sich lokal über einen Tomcat Server starten: [http://localhost:8080/todoapp](http://localhost:8080/todoapp).
Wenn noch kein persönliches Login existiert, kann über den Button `Register` ein neuer Benutzer registriert werden.
Nach der Registration kann sich der User einloggen und erhält eine leere Übersichtsliste ohne Todos. Über den Button 
"+New Todo" kann ein neues Todo erfasst werden.
Nach der Eingabe der Pflichtfelder (Titel) kann das Todo mit dem Button `Save` abgespeichert werden, damit es in der 
Übersichtsliste erscheint. Die Todos können über die Buttons in der Spalte "Action" bearbeitet und gelöscht werden.

Über die REST-Schnittstelle können die angeforderten Requests gegen die Applikation abgesetzt werden. Die 
Datenpersistenz wurde mit XML implementiert. Für die REST-Schnittstelle wurde eine JSON Helper Klasse erstellt 
und für die Datenhaltung dient die XmlHelper Klasse.

### Konfiguration des Tomcat-Servers
Damit die Applikation wunschgemäss gestartet werden kann, müssen folgende Einstellungen im Tomcat vorgenommen werden:
![Tomcat Overivew](docs/Tomcat%20overview.png)

![Tomcat Overivew](docs/Tomcat%20deployment.png)
