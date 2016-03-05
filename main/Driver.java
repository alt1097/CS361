package main;

import java.util.Scanner;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class Driver {
	public static void main(String[] args)	{
		Scanner scanner = new Scanner(System.in);
		Simulator simulator = new Simulator();
		System.out.print("Choose file (f) or console (c) input:  ");
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
		scanner.close();
	}
}
