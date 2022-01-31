package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class FileServer {
	private RequestQueue queue;
	public final static int DEFAULT_PORT = 5999;
	private byte[] buf = new byte[65535];
	private DatagramSocket server;
	private Worker[] workerPool;
	
	//Worker Threads erstellen und in den Pool schreiben
	private void startWorkers(int Anzahl) {
		FileMonitor m = new FileMonitor();
		for(int i = 0; i<Anzahl; i++) {
			workerPool[i] = new Worker(i, queue, server, m);
			workerPool[i].start();
		}
	}
	
	public void start() {
		queue = new RequestQueue();
		workerPool = new Worker[10];
		
		try {
			server = new DatagramSocket(5999);
			startWorkers(10);
			System.out.println("threads gestartet");
			while (true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				server.receive(packet);
				System.out.println("packet angekommen");
				//System.out.println(new String(packet.getData(), 0, packet.getLength()));
				queue.add(packet);
				System.out.println("packet in der queue");
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FileServer server = new FileServer();
		server.start();
	}
	
}
