package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;


public class FileServer {
	private RequestQueue queue;
	public final static int DEFAULT_PORT = 5999;
	private byte[] buf = new byte[65535];
	private DatagramSocket server;
	private Worker[] workerPool;
	private Map<String, FileMonitor> monitors = new HashMap<String, FileMonitor>();
	private MyFile fileHandler = new MyFile(monitors);
	
	//Worker Threads erstellen und in den Pool schreiben
	private void startWorkers(int Anzahl) {
		for(int i = 0; i<Anzahl; i++) {
			//Worker wird erstellt und in den Worker Pool geschrieben
			workerPool[i] = new Worker(i, queue, server, fileHandler);
			//Starten des erstellten Worker threads 
			workerPool[i].start();
		}
	}
	
	public void start() {
		//Für jede Datei wird ein eigener Monitor erstellt werden
		monitors.put("Testdokument", new FileMonitor());
		monitors.put("Test", new FileMonitor());
		queue = new RequestQueue();
		workerPool = new Worker[10];
		try {
			//Starten des Servers auf Port 5999
			server = new DatagramSocket(5999);
			//Starten von 10 Workern
			startWorkers(10);
			System.out.println("threads gestartet");
			while (true) {
				//Auf eintreffen eines Packets warten
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				server.receive(packet);
				System.out.println("packet angekommen");
				//Angekommenes Packet in die Warteschlange schreiben
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
		//Initialisieren des Servers
		FileServer server = new FileServer();
		//Starten des Servers
		server.start();
	}
	
}

