package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class FileServer {
	
	public final static int DEFAULT_PORT = 5999;
	private byte[] buf = new byte[256];
	private DatagramSocket server;
	public FileServer(int port) {
		try {
			server = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				server.receive(packet);
				System.out.println("received a packet");
				buf = packet.getData();
				String content = new String(buf, 0, buf.length);
				String answer = "";
				String[] contentArray = content.split(" ", 2);
				if (contentArray[0] == "READ") {
					answer = MyFile.read(content);
				} else if (contentArray[0] == "WRITE") {
					answer = MyFile.write(content);
				} else {
					answer = "Falscher Befehl";
				}
				
				buf = answer.getBytes();
				System.out.println(answer);
				DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
				server.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			}
		}
	
	public static void main(String[] args) {
		FileServer server = new FileServer(DEFAULT_PORT);
		server.start();
	}
	
}
