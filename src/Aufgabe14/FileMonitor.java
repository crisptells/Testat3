package Aufgabe14;

public class FileMonitor {
	
	boolean activeWriter = false;
	int readerCnt = 0;
	int writerCnt = 0;
	
	public synchronized void startRead() {
		while (writerCnt > 0) {
			try {
				System.out.println("Fängt an zu warten");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("fertig mit warten");
		}
		readerCnt++;
		notifyAll();
	}
	
	public synchronized void stopRead() {
		notifyAll();
		readerCnt--;
	}
	
	public synchronized void startWrite() {
		writerCnt++;
		System.out.println("ist im startWrite angekommen ");
		while ((readerCnt > 0) || (activeWriter)) {
			try {
				System.out.println("Fängt an zu warten");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("fertig mit warten");
			
		}
		System.out.println("ist nach der while schleife");
		activeWriter = true;
	}
	
	public synchronized void stopWrite() {
		activeWriter = false;
		writerCnt--;
		notifyAll();
	}
}
