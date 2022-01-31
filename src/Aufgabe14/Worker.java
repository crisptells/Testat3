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
			//Worker ist gestartet und fordert ein Auftrag von der Warteschlange an
			DatagramPacket packet = queue.getNext();
			System.out.println("Worker: " + id + " started working");
			//Worker startet die bearbeitung des Auftrages und speichert das resultierende Packet
			DatagramPacket sendPacket = fileHandler.process(packet);
			try {
				//Worker schickt das Packet zum Client zurück
				server.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
