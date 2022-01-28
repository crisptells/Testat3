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
				packet.setLength(packet.getData().length);
				server.receive(packet);
				String content = "";
				content = new String(packet.getData(), packet.getOffset(), packet.getLength());
				String answer = "";
				String[] contentArray = content.split(" ", 2);
				if (contentArray[0].equals("READ")) {
					answer = MyFile.read(content);
				} else if (contentArray[0].equals("WRITE")) {
					answer = MyFile.write(content);
				} else {
					answer = "Falscher Befehl";
				}
				
				DatagramPacket sendPacket = new DatagramPacket(answer.getBytes(), answer.length(), packet.getAddress(), packet.getPort());
				server.send(sendPacket);
				content = "";
				buf = null;
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
