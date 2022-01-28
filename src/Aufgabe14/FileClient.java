package Aufgabe14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileClient {
	
	public static final int serverPort = 5999;
	private static byte[] buf;
	private static byte[] buf2 = new byte[256];
	public static void main(String[] args) {
		String hostname = "localhost";
		InetAddress address = null;
		DatagramSocket socket = null;
		DatagramPacket packet1 = new DatagramPacket(buf2, buf2.length);
		try {
		socket = new DatagramSocket();
		address = InetAddress.getByName(hostname);
		}
		catch (Exception e) {}
		
		
		BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			String theLine = "";
			try {
				theLine = userIn.readLine();
				if (theLine.equals(".")) break;
				buf = theLine.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, serverPort);
				socket.send(packet);
				socket.receive(packet1);
				buf = packet.getData();
				String content = new String(buf2, 0, buf2.length);
				System.out.println(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
