package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;

public class Worker extends Thread{
	private int id;
	private byte[] buf;
	private DatagramSocket server;
	private RequestQueue queue;
	private FileMonitor monitor;
	
	public Worker(int id, RequestQueue q, DatagramSocket server, FileMonitor m) {
		this.id = id;
		this.queue = q;
		this.server = server;
	}
	
	@Override
	public void run() {
		while (true) {
			DatagramPacket packet = queue.getNext();
			System.out.println("Worker: " + id + " started working");
			DatagramPacket sendPacket = MyFile.process(packet, m);
			try {
				server.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
