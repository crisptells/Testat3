package Aufgabe14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.util.Map;
import java.util.Scanner;

public class MyFile {
	private Map<String, FileMonitor> monitors;
	private FileMonitor monitor = null;
	
	public MyFile(Map<String, FileMonitor> monitors) {
		this.monitors = monitors;
	}
	
	public static String readPacket(DatagramPacket packet) {
		//Auswerten des Packetes und zur�ckgeben des Strings, welcher den Befehl enth�lt
		return new String(packet.getData(), packet.getOffset(), packet.getLength());
	}
	
	public DatagramPacket process(DatagramPacket packet) {
		//Initialisieren eines Antwortpackets
		DatagramPacket answer = null;
		//Auslesen des Befehls aus dem Packet
		String content = readPacket(packet);
		//Aufteilen des Befehls in Anweisung (READ oder WRITE) und Inhalt
		String[] contentArray = content.split(" ", 2);
		//Abfrage welcher Befehl mitgegeben wurde
		if (contentArray[0].equals("READ")) {
			//Wenn der mitgegebene Befehl READ ist, wird die Methode zum lesen ausgef�hrt
			answer = read(packet);
		} else if (contentArray[0].equals("WRITE")) {
			//Wenn der mitgegebene Befehl WRITE ist, wird die Methode zum schreiben ausgef�hrt
			answer = write(packet);
		} 
		//Das fertige Antwortpacket wird zur�ckgegeben
		return answer;
	}
	
	public DatagramPacket read(DatagramPacket packet) {
		//Auslesen des Befehls aus dem Packet 
		String content = readPacket(packet);
		//Aufteilen des Befehls in Anweisung (READ oder WRITE) und Inhalt
		String[] contentArray = content.split(" ", 2);
		//Aufteilen des Inhalts in Dateiname und Zeilennummer
		String[] subArray = contentArray[1].split(",", 2);
		//Wenn der aufgeteilte Befehl oder der aufgeteilte Inhalt != zwei Elemente gro� sind, stimmt etwas mit dem Anfragebefehl nicht
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 2) {return null;}
		if (contentArray[0].equals("READ")) {
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+subArray[0]+".txt");
	        monitor = monitors.get(subArray[0]);
	        Scanner myReader;
	        //Starten der Monitormethode lesen
	        monitor.startRead();
			try {
				//Simulieren einer l�ngeren Bearbeitungszeit
				Thread.sleep(2000);
				//Initialisieren des Scanners, der die Datei auslesen soll
				myReader = new Scanner(myObj);
				String[] data = new String[255];
				int i = 1;
				//Auslesen der Datei zeilenweise in die einzelnen Felder des Arrays
		        while (myReader.hasNextLine()) {
		        	data[i] = myReader.nextLine();
		        	i++;
		        }
		        myReader.close();
		        //Umwandeln der Zeilennummer von String zu Int
		        String line_string = subArray[1];
		        String line = line_string.trim();
		        int line_no = Integer.parseInt(line);
		        //Zugriff auf die Zeile mit der entsprechenden Zeilennummer
		        String line_data = data[line_no];
		        //Austritt aus der Monitormethode zum lesen
		        monitor.stopRead();
		        //Erstellen und Zur�ckgeben des Packets mit der Ausgelesenen Zeile
		        DatagramPacket sendPacket = new DatagramPacket(line_data.getBytes(), line_data.getBytes().length, packet.getAddress(), packet.getPort());
		        return sendPacket;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public DatagramPacket write(DatagramPacket packet) {
		//Auslesen des Befehls aus dem Packet
		String content = readPacket(packet);
		String answer = "Error";
		//Aufteilen des Befehls in Anweisung (READ oder WRITE) und Inhalt
		String[] contentArray = content.split(" ", 2);
		//Aufteilen des Inhalts in Dateiname, Zeilennummer und Text
		String[] subArray = contentArray[1].split(",");
		System.out.println("ist im write:)");
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 3) {return null;}
		if (contentArray[0].equals("WRITE")) {
			String fileName = subArray[0];
			String line_no = subArray[1];
			String contents = subArray[2];
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+fileName+".txt");
	        //Den Monitor anhand des Dateinamens auslesen
	        monitor = monitors.get(fileName);
	        String[] data = null;
	        Scanner myReader;
	        Scanner lineCounter;
	        //Starten der Monitormethode lesen, da hier auch ungest�rt gelesen werden kann 
	        monitor.startWrite();
			try {
				//Z�hlen der Zeilen
				lineCounter = new Scanner(myObj);
				int lines = 0;
				while (lineCounter.hasNextLine()) {
		        	lines++;
		        	lineCounter.nextLine();
		        }
				//Initialisieren eines Arrays, das genau so lang ist wie Zeilen gez�hlt wurden
				data = new String[lines];
				lineCounter.close();
				
				int i = 0;
				//Auslesen der Datei in die Zeilen des zuvor initialisierten Arrays
				myReader = new Scanner(myObj);
		        while (myReader.hasNextLine()) {
		        	data[i] = myReader.nextLine();
		        	i++;
		        }
		        myReader.close();
			} catch (FileNotFoundException e) {
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			}
			
			//�ndern der gegebenen Zeile
			int line_noInt = Integer.parseInt(line_no.trim());
			data[line_noInt-1] = contents.trim();
			
			//�berschreiben der gegebenen Datei
			try {
				//Simulieren einer l�ngeren Bearbeitungszeit
				Thread.sleep(2000);
				//�berschreiben der Datei mithilfe eines PrintWriters
				PrintWriter fileOut = new PrintWriter(myObj);
				for(String s: data) {
					fileOut.println(s);
				}
				fileOut.close();
				//Austritt aus der Monitormethode zum schreiben
				monitor.stopWrite();
				//Zur�ckgeben des Antwortpackets
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		//Im Fall, dass der Befehl nicht erkannt wurde, wird ein Packet zur�ckgeschickt, dass Error enth�lt
		return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
	}
}
