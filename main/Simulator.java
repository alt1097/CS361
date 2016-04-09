package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import gui.Gui; //gui example. Place this import in class that will create gui window


/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class Simulator {
	ChronoTimer chrono = new ChronoTimer();
	ComHandler handler = new ComHandler(chrono);

	/**
	 * Simulates the running of the chrono timer with a test file
	 * @param fileName Name of file you'd like to test
	 */
	public void fileReader(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       handler.handleCommand(line);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts reading commands from the console
	 */
	public void consoleReader() {
		Scanner scanner = new Scanner(System.in);
		while(true) {
			handler.handleCommand(scanner.nextLine());
		}
	}

	public void gui() {
		//gui example. Place this code in class that will create gui window
		try {
			Gui window = new Gui(chrono);
			window.getFrame().setVisible(true);
			chrono.addListener(window);
		} catch (Exception e) {
			e.printStackTrace();
		}// end of gui example
	}
}
