package Aufgabe14;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyFile {
	
	
	public static String read(String content) {
		System.out.println("Help");
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
		        	System.out.print(data[i] + "\n");
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
	
	public static void write(String content) {
		
	}
}
