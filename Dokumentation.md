# Testat - 3
	
Luis Maier - Matrikelnummer 7096964 

Christian Reitmeier - Matrikelnummer 2923922


## Aufgabe

In dieser Aufgabe soll ein File-Server für Textdateien entwickelt werden.

Vereinfachend gehen wir davon aus, dass dem Server ein festes, bereits existierendes Basisverzeichnis zugeordnet ist, in dem sich alle verwalteten Dateien befinden und dass er die notwendigen Zugriffsrechte besitzt. Die Textdateien sind dabei zeilenweise organisiert und beginnen mit Zeilennummer 1.

Der Server soll als Worker-Pool-Server auf Port 5999 Aufträge in Form von Strings mit ”READ
filename,line_no” entgegennehmen, wobei line_no eine positive ganze Zahl sein muss. Daraufhin
wird vom Server die Datei filename geöffnet, die Zeile line_no ausgelesen und zurückgeschickt.
Außerdem soll der Server auch das Kommando ”WRITE filename,line_no,data” verstehen, bei
dem die Zeile line_no durch data (kann Kommas und Leerzeichen enthalten) ersetzt werden soll.
Falls sich im Basisverzeichnis des Servers keine solche Datei befindet oder keine entsprechende Zeile vorhanden ist, soll an den Client eine Fehlermeldung zurückgesendet werden.

Achten Sie darauf, dass nebenl aufige Zugriffe konsistente Dateien hinterlassen. Implementieren Sie hierzu
das Zweite Leser-Schreiber-Problem (mit Schreiberpriorität) mit dem Java Monitorkonzept!

Implementieren Sie den Server sowie einen kleinen Test-Client. Verwenden Sie Java und UDP!

### Vorraussetzung

Der Server nimmt auf Port 5999 Aufträge entgegen. Es existieren zwei Text Dateien auf die zugegriffen werden. Der "READ" Befehl gibt die ausgewählte Zeile der Datei aus. Der "WRITE" Befehl überschreibt die ausgewählte Zeile der Datei. Es gibt einen Worker-Thread-Pool der die Befehle ausführt, dieser wird von dem Monitor überwacht. Der Monitor hat Schreiberpriorität.

In den Beispielen besteht die Ausgabe aus zwei Teilen, dem Teil mit den abgeschickten Befehlen und der Teil mit den Antworten.

### Beispiel 1

Im ersten Beispiel soll das parallele lesen einer Datei möglich sein.

	READ Testdokument,1
	READ Testdokument,2
	Das ist Zeile 1
	Das ist Zeile 2


### Auswertung - Beispiel 1

Die Leser dürfen gleichzeitig auf eine Datei zugreifen und schließen sich nicht gegenseitig aus. Aufgrund dessen ist dieses Beispiel möglich. Die Lese-Anforderungen wurden gleichzeitig abgeschickt und kamen auch gleichzeitig an. Da die Lese-Worker sich nicht gegenseitig ausschließen ist ein paralleles Lesen möglich.

### Beispiel 2

Im zweiten Beispiel soll es möglich sein das gleichzeitig ausgeführte "WRITE" Befehle auf eine Datei ausgeführt werden können.

	WRITE Testdokument,1,Das ist die neue Zeile 1
	WRITE Testdokument,2,Das ist die neue Zeile 2
	Write Executed
	Write Executed
	
überschiebene Datei
	
	Das ist die neue Zeile 1
	Das ist die neue Zeile 2

### Auswertung - Beispiel 2

Das gleichzeitige Ausführen ist mit dem Monitorkonzept möglich. Dieser lässt den Thread warten und gibt ihn erst dann frei wenn kein Leser oder Schreiber mehr Zugriff auf die Datei hat. Aus dem Grund kommen die Rückgaben vom Server versetzt an.

### Beispiel 3

In diesem Beispiel soll die Schreiberpriorität gewährleistet werden, sollte ein Leser- und ein Schreiberauftrg parallel abgeschickt werden.

	WRITE Testdokument,2,Zeile 2
READ Testdokument,2
WRITE Testdokument,2,neue Zeile 2
Write Executed
Write Executed
neue Zeile 2


### Auswertung - Beispiel 3

Die Schreiberpriorität wird eingehalten, da zuerst die Schreiber und dann die Leser abgearbeitet werden.


### Beispiel 4

Im vierten Beispiel soll es möglich sein aus mehreren Dateien lesen zu können.

	READ Testdokument,2
READ Test,2
neue Zeile 2
Zeile 2


### Auswertung - Beispiel 4

Dies wird wie im ersten Beispiel abgearbeitet auch wenn auf mehrere Dateien zugegriffen wird.


### Beispiel 5

In diesem Beispiel soll es möglich sein dass mehrere Schreibzugriffe auf eine Datei abgearbeitet werden.

	[18:33] Maier, Luis
WRITE Test,2,Zeile 2
WRITE Testdokument,2,Neue zeile 2
Write Executed
Write Executed


	
überschrieben Datei:
	
	Das ist die neue Zeile 1 im ersten Dokument
	Das ist die neue Zeile 2 im ersten Dokument
	Das ist die neue Zeile 1 im zweiten Dokument
	Das ist die neue Zeile 2 im zweiten Dokument


### Auswertung - Beispiel 5

Die Abarbeitung aus Beispiel 2 wird auch hier gewährleistet.


### Beispiel 6

Im letzten Beispiel sollen mehrere Lese- und Schreibezugriffe auf mehrere Dateien erfolgreich abgearbeitet werden können.

	WRITE Test,2,Zeile 2
	READ Testdokument,2
	WRITE Testdokument,2,Neue zeile 2
	READ Test,2
	Write Executed
	Neue zeile 2
	Write Executed
	Zeile 2


### Auswertung - Beispiel 6

Wie in Beispiel 3 funktioniert auch dieses Beispiel. Die Schreiberpriorität wird eingehalten.


### Beispiel 7

In diesem Beispiel werden die Fehler behandelt die auftreten können.


### Auswertung - Beispiel 7






