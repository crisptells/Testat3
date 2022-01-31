package Aufgabe14;

public class FileMonitor {
	
	boolean activeWriter = false;
	int readerCnt = 0;
	int writerCnt = 0;
	
	public synchronized void startRead() {
		while (writerCnt > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		while ((readerCnt > 0) || (activeWriter)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activeWriter = true;
	}
	
	public synchronized void stopWrite() {
		activeWriter = false;
		writerCnt--;
		notifyAll();
	}
}
