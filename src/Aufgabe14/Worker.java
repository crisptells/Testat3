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

    // Konstruktor für Worker-Klasse
    public Worker(int id, RequestQueue q, DatagramSocket server) {
        this.id = id;
        this.queue = q;
        this.server = server;
    }

    // wird am Anfang gestartet und holt sich solange das nächste Packet aus der Warteschlange
    // bis es keins mehr gibt und bearbeitet diese
    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = queue.getNext();
            System.out.println("Worker: " + id + " started working");
            DatagramPacket sendPacket = MyFile.process(packet);
            try {
                server.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
