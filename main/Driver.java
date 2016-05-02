package main;

import java.util.Scanner;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class Driver {	
	
	public static void main(String[] args)	{
		
		Simulator simulator = new Simulator();		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Choose file (f), console (c), or GUI (g) input:  ");
		
		String inputType = scanner.nextLine();

		if (inputType.startsWith("f"))
		{
			System.out.println("Please enter the filename of the test data:");
			simulator.fileReader(scanner.nextLine());
		}
		else if (inputType.startsWith("c"))
		{
			System.out.println("Enter commands into the console:");
			simulator.consoleReader();
		}
		else if (inputType.startsWith("g"))
		{
			System.out.println("Gui starting...");
			simulator.gui();
		}
		scanner.close();
	}
}