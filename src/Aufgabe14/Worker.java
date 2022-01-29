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
	
	public Worker(int id, RequestQueue q, DatagramSocket server) {
		this.id = id;
		this.queue = q;
		this.server = server;
	}
	
	@Override
	public void run() {
		System.out.println("Queue Size: " + queue.size());
		DatagramPacket packet = queue.getNext();
		System.out.println("Worker: " + id + " started working");
		buf = packet.getData();
		String content = new String(buf, 0, buf.length);
		System.out.println("Instant nach packet.getData()");
		System.out.println("content: " + content);
		String[] contentArray = content.split(" ", 2);
		String answer = "";
		System.out.println("packet im Worker angekommen");
		if (contentArray[0].equals("READ")) {
			System.out.println("READ Methode erkannt");
			answer = MyFile.read(content);
		} else if (contentArray[0].equals("WRITE")) {
			answer = MyFile.write(content);
		} else {
			answer = "Falscher Befehl";
		}
		
		DatagramPacket sendPacket = new DatagramPacket(answer.getBytes(), answer.length(), packet.getAddress(), packet.getPort());
		try {
			server.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
