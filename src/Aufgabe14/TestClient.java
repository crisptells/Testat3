package Aufgabe14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TestClient {
	public static final int serverPort = 5999;
	static DatagramSocket socket = null;
	static String hostname = "localhost";
	static InetAddress address = null;
	static byte[] buf = new byte[65535];
	
	
	public static void main(String[] args) {
		try {
		socket = new DatagramSocket();
		address = InetAddress.getByName(hostname);
		}
		catch (Exception e) {}
		
		Test1();
		
		while (true) {
			DatagramPacket packetReceive = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packetReceive);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String content = new String(packetReceive.getData(), 0, packetReceive.getLength());
			System.out.println(content);
		}
	}
	
	private static void send(String content) {
		try {
			System.out.println(content);
			socket.send(new DatagramPacket(content.getBytes(), content.getBytes().length, address, serverPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void Test1() {
		send("WRITE Testdokument,2,Das ist Zeile 2");
		send("WRITE Testdokument,2,Das ist Zeile 22");
		send("WRITE Testdokument,2,Das ist Zeile 222");
		send("READ Testdokument,2");
		send("WRITE Testdokument,2,Das ist Zeile 2222");
	}
}
