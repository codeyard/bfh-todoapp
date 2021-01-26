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

Die Benutzer und deren Todos werden in der `Data.xml` Datei persistiert, welche unter `WEB-INF/xml` abgelegt ist. Sie
wird dann ausgelesen, wenn eine neue Instanz des `UserManager` erzeugt wird. Beim Anlegen von neuen Benutzern sowie 
beim Anlegen, Aktualisieren und Löschen von Todos wird die Datei aktualisiert, sprich neu geschrieben.

Die mit JavaDoc generierte Dokumentation der Java-Quelltexte ist im Ordner `docs/javadoc` abgelegt.

Es ist bereits ein Benutzer mit einem Todo zum Testen vorhanden. Die Anmeldedaten lauten `Username: default, Password: 1234`.

Die Applikation ist nach erfolgreicher Tomcat-Konfiguration (siehe Kapitel "Inbetriebnahme") unter 
[http://localhost:8080/todoapp](http://localhost:8080/todoapp) verfügbar.

Die REST-Schnittstelle ist unter [http://localhost:8080/todoapp/api](http://localhost:8080/todoapp/api) ansprechbar.

## Design

### Domänenmodell
![Domain Model](docs/DomainModel.png)

* Der `UserManager` ist für die Registrierung und Authentifizierung eines Benutzers zuständig. Er bietet folgende Methoden an:
    * `getInstance()`: instantiiert einen User Manager als Singleton, liest das Set mit den vorhandenen Benutzern inklusive
      deren Todos aus der `Data.xml` Datei aus und setzt die Counter (siehe nächste Methode).
    * `setCounters()`: iteriert durch das obengenannte Set und ermittelt die höchste verwendete userID und todoID. Diese
      Werte werden als statische Klassenvariablen in der `User` bzw. `Todo` Klasse gesetzt. Beim Erfassen von neuen Benutzern 
      oder Todos wird der entsprechende Wert (userCounter bzw. todoCounter) erhöht und als userID bzw. todoID verwendet. So 
      wird quasi ein auto_increment Primärschlüssel einer Datenbank simuliert.
    * `register()`: legt einen neuen Benutzer an. Falls der gewählte Benutzername bereits existiert, wird eine `UserException`
      geworfen.
    * `authenticate()`: autentifiziert einen Benutzer. Nach erfolgreicher Anmeldung wird ein `User` Objekt zurückgegeben, 
      welches die Kundendaten sowie eine Liste aller vorhandenen Todos des angemeldeten Benutzers enthält. Falls der 
      Benutzername nicht existiert oder die Anmeldedaten nicht stimmen, wird eine `UserException` geworfen.
    * `isNotRegistered()`: gibt true zurück, wenn der angegebene Benutzername noch nicht verwendet wird.
    * `getUsers()`: gibt ein Set mit den vorhandenen `User` Objekten zurück.
    * `getUser()`: gibt ein einzelnes `User` Objekt zurück.
    * `loadData()`: liest die `Data.xml` Datei aus.
    * `writeData()`: schreibt die aktuellen Daten in die `Data.xml` Datei.
* Die `UserException` wird geworfen, wenn die Registrierung oder Anmeldung eines Benutzers fehlschlägt.
* Die `User` Klasse implementiert einen Benutzer mit dessen Todo Liste und enthält zb. folgende Methoden:
    * `getTodos()`: retourniert eine Liste mit `Todo` Objekten. Diese Liste kann optional nach einer Kategorie oder einem Status
      (incomplete, complete, overdue, important) gefiltert werden.
    * `getTodosStatistics()`: gibt einen String mit Statistikangaben zurück: wieviele Todos sind vorhanden, wieviele sind
      offen, überfällig oder wichtig. Hierbei kann wiederum nach Kategorie und/oder Status gefiltert werden.
    * `addTodo()`: fügt ein neues Todo der Liste hinzu.
    * `updateTodo()`: aktualisiert ein angegebenes Todo.
    * `deleteTodo()`: entfernt ein Todo aus der Liste.
    * `getTodo()`: gibt ein einzelnes Todo zurück.
    * `getDistinctCategories()`: retourniert ein Set mit allen benutzten Kategorien des Benutzers zurück, welches keine
      Duplikate enthält.
    * `hasCompletedTodos()`: gibt einen boolschen Wert zurück, der besagt, ob der Benutzer bereits erledigte Todos hat.
* Die `Todo` Klasse implementiert ein Todo und verfügt nebst den Settern und Gettern über diese Methoden:
    * `isOverdue()`: gibt true zurück, falls auf dem Todo ein Fälligkeitsdatum gesetzt ist, welches in der Vergangenheit
      liegt.
    * `compareTo()`: ist für die Sortierung der Todos in der ArrayListe zuständig. Die Sortierung erfolgt zuerst nach dem
      Fälligkeitsdatum, dann alphabetisch nach dem Titel und schliesslich nach der todoID.


### Page Flow
![Page Flow](docs/PageFlow.png)
Ein Benutzer landet auf der Login-Seite, wo er auf die Registrierung abspringen kann und anschliessend auf die Login-Seite weitergeleitet wird.
Nach erfolgreichem Login gelangt der Benutzer in die Übersicht der Todos, wo er bestehende Todos bearbeiten oder löschen kann. Hier kann er jedoch auch diverse Filter setzen, um die Ansicht seinen Wünschen entsprechend anzupassen.
Ausserdem kann der User von hier aus zur Todo-Erstellung abspringen, wo ein neues Todo erstellt werden kann.

### Klassendesign

#### MVC-Modell: 
![Page Flow](docs/mvc.png)

##### REST
Ein Client kann eine REST-Anfrage erstellen, welche durch einen Filter - der die Authentifizierung des Benutzers überprüft - geleitet wird.
Je nach Typ des Requests kommen unterschiedliche Servlets zum Einsatz, welche je nach Typ des Request diesen verarbeiten:

* `AuthenticationFilter`:
Sämtliche Requests auf `/api/*` werden durch den AuthenticationFilter bearbeitet. Dieser prüft, ob ein Request durch einen registrierten Benutzer getätigt wird oder nicht.
Bei `/users` sind nicht registrierte Requests erlaubt.
* `CategoriesRestServlet`:
Gibt dem authentifizierten Benutzer eine eindeutige Liste der Kategorien seiner Todos zurück.
* `TodosRestServlet`:
Ein authentifizierter Benutzer kann über dieses Servlet Todos erstellen, aktualisieren, löschen, sowie einzelne oder alle Todos abfragen.
* `UsersRestServet`:
Auf diesem Servlet werden neue User erstellt. 
* `JsonHelper`:
Der JsonHelper übersetzt Requests des Clients von JSON bzw. die Antworten des Servers in JSON.

#### Model
Das Domänenmodell wurde weiter oben detailliert beschrieben.

##### Datenmodel
XmlHelper wurde als Hilfsklasse definiert für den Lese- und Schreibzugriff auf Data.xml, wo die Daten gespeichert werden.

* `XmlHelper`: Schreibt und liest den Klassenbaum, ausgehend vom Domänenmodell, in ein einziges XML-File. 

##### WEBAPP
Die Web-Applikation wird aus dem Browser gestartet. Hier erfolgen Requests an die Servlets, welche mit dem Domänenmodell interagieren und eine Antwort erzeugen.

* `RegisterServlet`: Registriert einen Benutzer und leitet diesen im Erfolgsfall an das LoginServlet weiter. Falls ein User in der Session noch eingeloggt ist, erfolgt eine direkt Weiterleitung an `/todos`.
* `LoginServlet`: Ist zuständig für die Authentifizierung des Benutzers. Im Erfolgsfall erfolgt eine Weiterleitung an `/tods`, ansonsten wird das Fehlschlagen angezeigt. Falls ein User in der Session noch eingeloggt ist, erfolgt eine direkt Weiterleitung an `/todos`.
* `LogoutServlet`: Löscht den aktuell in der Session gespeicherten Benutzer und leitet diesen an `/login` weiter.
* `TodoListServlet`: Ist zuständig für das Anzeigen und Filtern aller erfassten Todos eines Benutzers. Zudem hat der Benutzer die Möglichkeit, alle erledigten Todos auf einmal zu löschen. Bei nicht authentifizierten Zugriff erfolgt eine Weiterleitung an `/login`.
* `TodoServlet`: Ist zuständig für die Anzeige, Erstellung eines Todos und das Aktualisieren oder Löschen von einem bestehenden Todos. Bei nicht authentifizierten Zugriff erfolgt eine Weiterleitung an `/login`.

##### View
Die Views sind die Templates für die Erstellung der serverseitigen Antworten. Vorhanden sind folgende .jsp-Files:

* `errors.jsp`: Ist zuständig für die Anzeige von allfälligen Fehlern (inkl. 404)
* `index.jsp`: Formular für die Authentifizierung des Benutzers.
* `register.jsp`: Formular für die Registrierung eines neuen Benutzers. 
* `todo.jsp`: Formular für die Erstellung und Bearbeitung eines Todos.
* `todoDeletion.jsp`: Anzeige, welche fragt, ob ein Todo wirklich gelöscht werden soll.
* `todos.jsp`: Anzeige, welche für die Auflistung aller Todos zuständig ist. 


## Implementierung
Das Frontend wurde mit Hilfe von [Bulma](https://bulma.io/) implementiert und ist vollständig responsive. 
Im Backend wurden die im CAS gezeigten Technologien (Servlets, JSP, Jackson XMl, Jackson JSON, etc.) verwendet. 

### Zusätzlich implementierte Features

Die Projektgruppe hat zusätzliche Features erfasst:

* Statistiken
* Zusätzliche Filtermethoden
* Löschung aller erledigten Todos
* Weiterleitung von (nicht) eingeloggten Benutzer
* Error-Seite


#### Statistiken
Hat ein Benutzer mehr als ein Todo erfasst, wird unterhalb der Übersichtsliste eine Statistik angezeigt, 
die auf einen Blick zeigt, wie viele Todos vorhanden und wie viele davon offen, wichtig oder gar überfällig sind.


#### Zusätzliche Filtermethoden
Ein Benutzer hat die Möglichkeit, neben der Filterung nach Kategorien, auch nach dem Status (incomplete, complete, 
overdue, important) der Todos zu filtern. Eine Filterkombination aus Kategorie und Status ist auch möglich. Wenn
ein Filter aktiv ist, erscheint rechts daneben ein Papierkorb-Icon über welches die Filter zurückgesetzt werden 
können.


#### Löschung aller erledigten Todos
Der Benutzer hat über den Button `Delete completed todos` die Möglichkeit, mit nur einem Klick alle erledigten 
Todos zu löschen. Dieser Button wird nur angezeigt, wenn der Benutzer tatsächlich erledigte Todos hat und kein
Filter aktiv ist.


#### Weiterleitung von (nicht) eingeloggten Benutzer
Eingeloggte Benutzer werden beim Versuch, auf die Registrier- oder Login-Seite zuzugreifen, direkt auf die Todoliste 
weitergeleitet, solange die Session noch aktiv ist.
Gleichzeitig können nicht eingeloggte Benutzer nicht auf den Userbereich zugreifen und werden zur Loginseite 
weitergeleitet.


#### Error-Seite
Für das Abfangen von möglichen Fehlern und beim Zugriff auf nicht vorhandene Ressourcen (404) wird eine Error-Page 
angezeigt.



## Inbetriebnahme
Die Applikation lässt sich lokal über einen Tomcat Server starten: [http://localhost:8080/todoapp](http://localhost:8080/todoapp).
Wenn noch kein persönliches Login existiert, kann über den Button `Register` ein neuer Benutzer registriert werden.
Nach der Registration kann sich der User einloggen und erhält eine leere Übersichtsliste ohne Todos. Über den Button 
"+New Todo" kann ein neues Todo erfasst werden.
Nach der Eingabe der Pflichtfelder (Titel) kann das Todo mit dem Button `Save` abgespeichert werden, damit es in der 
Übersichtsliste erscheint. Die Todos können über die Buttons in der Spalte "Action" bearbeitet und gelöscht werden.


### Konfiguration des Tomcat-Servers
Damit die Applikation wunschgemäss gestartet werden kann, müssen folgende Einstellungen im Tomcat vorgenommen werden:

![Tomcat Overivew](docs/Tomcat%20overview.png)

![Tomcat Overivew](docs/Tomcat%20deployment.png)
