package Aufgabe14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MyFile {
	
	
	public static String read(String content) {
		String[] contentArray = content.split(" ", 2);
		String[] subArray = contentArray[1].split(",", 2);
		
		if (contentArray.length != 2) {return "Fehler! - Falscher command";}
		if (subArray.length != 2) {return "Fehler! - Falscher command";}
		if (contentArray[0].equals("READ")) {
			//Datei mit dem Namen des Keys lokalisieren
			String userName = System.getProperty("user.name");
	        File myObj = new File("C:/Users/"+userName+"/Desktop/AdvIT14/"+subArray[0]+".txt");

	        Scanner myReader;
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
		        return data[line_no];
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	        
		}
		return "Fehler";
	}
	
	public static String write(String content) {
		String[] contentArray = content.split(" ", 2);
		String[] subArray = contentArray[1].split(",");
		
		if (contentArray.length != 2) {return "Fehler! - Falscher command";}
		if (subArray.length != 3) {return "Fehler! - Falscher command";}
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
				return "Fehler beim lesen der Datei";
			}
			
			//Ändern der gegebenen Zeile
			int line_noInt = Integer.parseInt(line_no.trim());
			data[line_noInt-1] = contents;
			
			//Überschreiben der gegebenen Datei
			try {
				PrintWriter fileOut = new PrintWriter(myObj);
				for(String s: data) {
					fileOut.println(s);
				}
				fileOut.close();
				return "Hat geklappt";
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return "Fehler, kein Befehl erkannt";
	}
}
