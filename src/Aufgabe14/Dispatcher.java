package Aufgabe14;

import java.net.DatagramPacket;
import java.util.LinkedList;

public class Dispatcher {
	private Worker[] workerPool;
	private LinkedList<DatagramPacket> queue = new LinkedList<DatagramPacket>();
	
	public Dispatcher(Worker[] workerPool) {
		this.workerPool = workerPool;
	}
	
	public boolean putInQueue(DatagramPacket packet) {
		queue.add(packet);
		return true;
	}
	
	public DatagramPacket getNextInQueue() {
		return queue.removeFirst();
	}
	
	public void beginTask() {
		for(Worker w: workerPool) {
			if (w.isWorking() == false) {
				w.setWorking(true);
				Thread t = new Thread(w);
				t.start();
				return;
			}
		}
	}
	
	public void startThreads() {
		for(Worker w: workerPool) {
			Thread t = new Thread(w);
		}
	}
	
}
