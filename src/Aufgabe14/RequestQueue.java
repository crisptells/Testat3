package Aufgabe14;

import java.net.DatagramPacket;
import java.util.LinkedList;

public class RequestQueue {
	
	private LinkedList<DatagramPacket> queue = new LinkedList<DatagramPacket>();
	
	synchronized void add(DatagramPacket packet) {
		queue.add(packet);
		this.notify();
	}
	
	synchronized DatagramPacket getNext() {
		while(queue.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.pop();
	}
	
	public int size() {
		return queue.size();
	}

}
