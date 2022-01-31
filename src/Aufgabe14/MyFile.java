package Aufgabe14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.util.Scanner;

public class MyFile {
	
	public static String readPacket(DatagramPacket packet) {
		byte[] buf;
		buf = packet.getData();
		return new String(buf, 0, buf.length);
	}
	
	public static DatagramPacket process(DatagramPacket packet, FileMonitor m) {
		DatagramPacket answer = null;
		String content = readPacket(packet);
		String[] contentArray = content.split(" ", 2);
		if (contentArray[0].equals("READ")) {
			System.out.println("READ Methode erkannt");
			answer = read(packet, m);
		} else if (contentArray[0].equals("WRITE")) {
			answer = write(packet, m);
		} 
		return answer;
	}
	
	public static DatagramPacket read(DatagramPacket packet, FileMonitor m) {
		String content = readPacket(packet);
		String[] contentArray = content.split(" ", 2);
		String[] subArray = contentArray[1].split(",", 2);
		
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 2) {return null;}
		if (contentArray[0].equals("READ")) {
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+subArray[0]+".txt");
	        
	        Scanner myReader;
	        m.startRead();
			try {
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
		        DatagramPacket sendPacket = new DatagramPacket(line_data.getBytes(), line_data.getBytes().length, packet.getAddress(), packet.getPort());
		        return sendPacket;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			m.stopRead();
		}
		return null;
	}
	
	public static DatagramPacket write(DatagramPacket packet, FileMonitor m) {
		String content = readPacket(packet);
		String answer = "Error";
		String[] contentArray = content.split(" ", 2);
		String[] subArray = contentArray[1].split(",");
		
		if (contentArray.length != 2) {return null;}
		if (subArray.length != 3) {return null;}
		if (contentArray[0].equals("WRITE")) {
			String fileName = subArray[0];
			String line_no = subArray[1];
			String contents = subArray[2];
			
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+subArray[0]+".txt");
	        String[] data = null;
	        Scanner myReader;
	        Scanner lineCounter;
	        m.startRead();
			try {
				lineCounter = new Scanner(myObj);
				int lines = 0;
				while (lineCounter.hasNextLine()) {
		        	lines++;
		        	lineCounter.nextLine();
		        }
				data = new String[lines];
				lineCounter.close();
				
				int i = 0;
				myReader = new Scanner(myObj);
		        while (myReader.hasNextLine()) {
		        	data[i] = myReader.nextLine();
		        	i++;
		        }
		        myReader.close();
			} catch (FileNotFoundException e) {
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			}
			m.stopRead();
			;
			//Ändern der gegebenen Zeile
			int line_noInt = Integer.parseInt(line_no.trim());
			data[line_noInt-1] = contents;
			
			//Überschreiben der gegebenen Datei
			m.startWrite();
			try {
				PrintWriter fileOut = new PrintWriter(myObj);
				for(String s: data) {
					fileOut.println(s);
				}
				fileOut.close();
				answer = "Hat geklappt!";
				return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			m.stopWrite();
			
		}
		return new DatagramPacket(answer.getBytes(), answer.getBytes().length, packet.getAddress(), packet.getPort());
	}
}
