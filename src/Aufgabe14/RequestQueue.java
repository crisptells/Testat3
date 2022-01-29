package Aufgabe14;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class RequestQueue {
	
	private LinkedList<DatagramPacket> queue = new LinkedList<DatagramPacket>();
	
	synchronized void add(DatagramPacket packet) {
		queue.add(packet);
		this.notify();
	}
	
	synchronized DatagramPacket getNext() {
		DatagramPacket returningPacket = null;
		while(queue.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		DatagramPacket packet = queue.peek();
		System.out.println(new String(packet.getData(), 0, packet.getLength()));
		try {
			returningPacket = queue.pop();
			System.out.println("Element gefunden:)");
			//return returningPacket;
		} catch (NoSuchElementException e) {
			System.out.println("Kein element in der queue gefunden :(");
			e.printStackTrace();
		}
		return packet;
	}
	
	public int size() {
		return queue.size();
	}

}
