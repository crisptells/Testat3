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
		
//		Test1();
//		Test2();
//		Test3();
//		Test4();
//		Test5();
		Test6();
		
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
			Thread.sleep(1000);
			System.out.println(content);
			socket.send(new DatagramPacket(content.getBytes(), content.getBytes().length, address, serverPort));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void Test1() {
		send("READ Testdokument,2");
		send("READ Testdokument,2");
	}
	
	private static void Test2() {
		send("WRITE Testdokument,2,Zeile 2");
		send("WRITE Testdokument,2,Neue zeile 2");
	}
	
	private static void Test3() {
		send("WRITE Testdokument,2,Zeile 2");
		send("READ Testdokument,2");
		send("WRITE Testdokument,2,neue Zeile 2");
	}
	
	private static void Test4() {
		send("READ Testdokument,2");
		send("READ Test,2");
	}
	
	private static void Test5() {
		send("WRITE Test,2,Zeile 2");
		send("WRITE Testdokument,2,Neue zeile 2");
	}
	
	private static void Test6() {
		send("WRITE Test,2,Zeile 2");
		send("READ Testdokument,2");
		send("WRITE Testdokument,2,Neue zeile 2");
		send("READ Test,2");
	}
}
