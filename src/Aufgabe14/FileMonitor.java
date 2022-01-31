package Aufgabe14;

public class FileMonitor {
    boolean activeWriter = false;
    int readerCnt = 0;
    int writerCnt = 0;

    // Methode zum betreten des kritischen Abschnitts read
    public synchronized void startRead() {
        // Wenn ein Writer aktiv ist, wird solange gewartet, bis dieser fertig ist
    	System.out.println("writerCount: " + writerCnt);
    	System.out.println("readerCount: " + readerCnt);
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
    	System.out.println("writerCount: bei stopRead " + writerCnt);
    	System.out.println("readerCount: bei stopRead " + readerCnt);
        // Benachrichtigt alle das er fertig mit lesen ist
    	readerCnt--;
        notifyAll();
    }

    // Methode zum betreten des kritischen Abschnitts write mit Schreiberpriorität
    public synchronized void startWrite() {
        // Zählt Write counter aufgrund der Schreiberpriorität vor Bedingung hoch
        writerCnt++;
        // wenn Reader oder Writer aktiv ist, wartet er bis diese fertig sind
        System.out.println("writerCount: " + writerCnt);
    	System.out.println("readerCount: " + readerCnt);
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
    	System.out.println("writerCount: bei stopWrite " + writerCnt);
    	System.out.println("readerCount: bei stopWrite " + readerCnt);
        // wenn fertig mit schreiben, wird activeWriter false gesetzt
        activeWriter = false;
        writerCnt--;
        // alle werden dahingehend benachrichtigt
        notifyAll();
    }
}
