package Aufgabe14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileClient {

    // Server port wird festgelegt
    public static final int serverPort = 5999;
    private static byte[] buf = new byte[65535];
    public static void main(String[] args) {
        String hostname = "localhost";
        InetAddress address = null;
        DatagramSocket socket = null;
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
                DatagramPacket packetSend = new DatagramPacket(theLine.getBytes(), theLine.getBytes().length, address, serverPort);
                // sendet das Packet an den Server
                socket.send(packetSend);
                DatagramPacket packetReceive = new DatagramPacket(buf, buf.length);
                // empfängt das Packet vom Server
                socket.receive(packetReceive);
                String content = new String(packetReceive.getData(), 0, packetReceive.getLength());
                System.out.println(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

