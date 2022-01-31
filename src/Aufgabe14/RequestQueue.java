package Aufgabe14;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class RequestQueue {

    private LinkedList<DatagramPacket> queue = new LinkedList<DatagramPacket>();

    // Fügt der Warteschlange ein Packet hinzu und benachrichtig alle
    synchronized void add(DatagramPacket packet) {
        queue.add(packet);
        this.notify();
    }

    // fordert das nächste Packet in der Warteschlange
    synchronized DatagramPacket getNext() {
        // wird an der Warteschlange gewartet wenn es kein Packet gibt
        DatagramPacket returningPacket = null;
        while(queue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // gibt das Packet an der ersten Stelle aus und löscht es danach
        returningPacket = queue.pop();
        System.out.println("Element gefunden:)");
        return returningPacket;
    }

    // gibt die Größe der Warteschlange
    public int size() {
        return queue.size();
    }

}
