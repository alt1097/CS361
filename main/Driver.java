package main;

import java.util.Scanner;

public class Driver {
	public static void main(String[] args)	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("please enter the filename of the test data:");
		Simulator simulator = new Simulator();
		simulator.fileReader(scanner.next());
		scanner.close();
	}
}
