# Testat - 2
	
Luis Maier - Matrikelnummer 7096964 |
Christian Reitmeier - Matrikelnummer 2923922

## Aufgabe

In dieser Aufgabe soll ein File-Server für Textdateien entwickelt werden.

Vereinfachend gehen wir davon aus, dass dem Server ein festes, bereits existierendes Basisverzeichnis zugeordnet ist, in dem sich alle verwalteten Dateien befinden und dass er die notwendigen Zugriffsrechte besitzt. Die Textdateien sind dabei zeilenweise organisiert und beginnen mit Zeilennummer 1.

Der Server soll als Worker-Pool-Server auf Port 5999 Auftr age in Form von Strings mit ”READ
filename,line_no” entgegennehmen, wobei line_no eine positive ganze Zahl sein muss. Daraufhin
wird vom Server die Datei filename geöffnet, die Zeile line_no ausgelesen und zurückgeschickt.
Außerdem soll der Server auch das Kommando ”WRITE filename,line_no,data” verstehen, bei
dem die Zeile line_no durch data (kann Kommas und Leerzeichen enthalten) ersetzt werden soll.
Falls sich im Basisverzeichnis des Servers keine solche Datei befindet oder keine entsprechende Zeile vorhanden ist, soll an den Client eine Fehlermeldung zurückgesendet werden.

Achten Sie darauf, dass nebenl aufige Zugriffe konsistente Dateien hinterlassen. Implementieren Sie hierzu
das Zweite Leser-Schreiber-Problem (mit Schreiberpriorität) mit dem Java Monitorkonzept!

Implementieren Sie den Server sowie einen kleinen Test-Client. Verwenden Sie Java und UDP!


### Beispiel 1

Im ersten Beispiel...

### Auswertung - Beispiel 1

Die Auswertung...


### Beispiel 2

Im zweiten Beispiel...

### Auswertung - Beispiel 2

Die Auswertung...


### Beispiel 3

Im dritten Beispiel...

### Auswertung - Beispiel 3

Die Auswertung...
