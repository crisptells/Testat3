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
		//Auswerten des Packetes und zurückgeben des Strings, welcher den Befehl enthält
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
			//Wenn der mitgegebene Befehl READ ist, wird die Methode zum lesen ausgeführt
			answer = read(packet);
		} else if (contentArray[0].equals("WRITE")) {
			//Wenn der mitgegebene Befehl WRITE ist, wird die Methode zum schreiben ausgeführt
			answer = write(packet);
		} 
		//Das fertige Antwortpacket wird zurückgegeben
		return answer;
	}
	
	public DatagramPacket read(DatagramPacket packet) {
		//Auslesen des Befehls aus dem Packet 
		String content = readPacket(packet);
		//Aufteilen des Befehls in Anweisung (READ oder WRITE) und Inhalt
		String[] contentArray = content.split(" ", 2);
		//Aufteilen des Inhalts in Dateiname und Zeilennummer
		String[] subArray = contentArray[1].split(",", 2);
		//Wenn der aufgeteilte Befehl oder der aufgeteilte Inhalt != zwei Elemente groß sind, stimmt etwas mit dem Anfragebefehl nicht
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 2) {return null;}
		if (contentArray[0].equals("READ")) {
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+subArray[0]+".txt");
	        monitor = monitors.get(subArray[0]);
	        Scanner myReader;
	        //Starten der Monitormethode zum lesen
	        monitor.startRead();
			try {
				//Simulieren einer längeren Bearbeitungszeit
				Thread.sleep(2000);
				//Initialisieren des Scanners, der die Datei auslesen soll
				myReader = new Scanner(myObj);
				String[] data = new String[255];
				int i = 1;
		        while (myReader.hasNextLine()) {
		        	data[i] = myReader.nextLine();
		        	i++;
		        }
		        myReader.close();
		        String line_string = subArray[1];
		        String line = line_string.trim();
		        int line_no = Integer.parseInt(line);
		        System.out.println(data[line_no]);
		        String line_data = data[line_no];
		        monitor.stopRead();
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
		String content = readPacket(packet);
		String answer = "Error";
		String[] contentArray = content.split(" ", 2);
		String[] subArray = contentArray[1].split(",");
		System.out.println("ist im write:)");
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 3) {return null;}
		if (contentArray[0].equals("WRITE")) {
			System.out.println("hat befehl erkannt und syntax richtig");
			String fileName = subArray[0];
			String line_no = subArray[1];
			String contents = subArray[2];
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+fileName+".txt");
	        monitor = monitors.get(fileName);
	        System.out.println("hat monitor gefunden");
	        String[] data = null;
	        Scanner myReader;
	        Scanner lineCounter;
	        System.out.println("kurz vor monitor.startRead");
	        monitor.startWrite();
			try {
				lineCounter = new Scanner(myObj);
				int lines = 0;
				while (lineCounter.hasNextLine()) {
		        	lines++;
		        	lineCounter.nextLine();
		        }
				System.out.println("Lines: " + lines);
				data = new String[lines];
				lineCounter.close();
				
				int i = 0;
				myReader = new Scanner(myObj);
		        while (myReader.hasNextLine()) {
		        	data[i] = myReader.nextLine();
		        	i++;
		        }
		        myReader.close();
		        System.out.println("hat gelesen");
			} catch (FileNotFoundException e) {
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			}
			
			 System.out.println("nach stopRead");
			//Ändern der gegebenen Zeile
			int line_noInt = Integer.parseInt(line_no.trim());
			data[line_noInt-1] = contents.trim();
			
			//Überschreiben der gegebenen Datei
			
			try {
				//Simulieren einer längeren Bearbeitungszeit
				Thread.sleep(2000);
				PrintWriter fileOut = new PrintWriter(myObj);
				for(String s: data) {
					fileOut.println(s);
				}
				fileOut.close();
				answer = "Hat geklappt!";
				monitor.stopWrite();
				 System.out.println("hat geklappt (Server)");
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
	}
}
