package Aufgabe14;

public class FileMonitor {
    boolean activeWriter = false;
    int readerCnt = 0;
    int writerCnt = 0;

    // Methode zum betreten des kritischen Abschnitts read
    public synchronized void startRead() {
        // Wenn ein Writer aktiv ist, wird solange gewartet, bis dieser fertig ist
        while (writerCnt > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Read counter wird eins hochgezählt, wenn kein Writer aktiv ist und benachrichtigt alle
        readerCnt++;
        notifyAll();
    }

    // Methode zum verlassen des kritischen Abschnitts read
    public synchronized void stopRead() {
        // Benachrichtigt alle das er fertig mit lesen ist
        notifyAll();
        readerCnt--;
    }

    // Methode zum betreten des kritischen Abschnitts write mit Schreiberpriorität
    public synchronized void startWrite() {
        // Zählt Write counter aufgrund der Schreiberpriorität vor Bedingung hoch
        writerCnt++;
        // wenn Reader oder Writer aktiv ist, wartet er bis diese fertig sind
        while ((readerCnt > 0) || (activeWriter)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // activeWriter wird true gesetzt, wenn er anfängt zu schreiben
        activeWriter = true;
    }

    // Methode zum verlassen des kritischen Abschnitts write mit Schreiberpriorität
    public synchronized void stopWrite() {
        // wenn fertig mit schreiben, wird activeWriter false gesetzt
        activeWriter = false;
        writerCnt--;
        // alle werden dahingehend benachrichtigt
        notifyAll();
    }
}
