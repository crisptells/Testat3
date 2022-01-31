package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Worker extends Thread{
	private int id;
	private DatagramSocket server;
	private RequestQueue queue;
	private MyFile fileHandler;
	
	public Worker(int id, RequestQueue q, DatagramSocket server, MyFile fileHandler) {
		this.id = id;
		this.queue = q;
		this.server = server;
		this.fileHandler = fileHandler;
	}
	
	@Override
	public void run() {
		while (true) {
			DatagramPacket packet = queue.getNext();
			System.out.println(new String(packet.getData(), 0, packet.getLength()));
			System.out.println("Worker: " + id + " started working");
			DatagramPacket sendPacket = fileHandler.process(packet);
			try {
				server.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
