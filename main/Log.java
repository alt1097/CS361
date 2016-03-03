package main;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */

///**
// * Creates a text file in project root directory.
// * Allows to write a lines in this file.
// *
// * Use example:
// *      Log log = new Log(); - create a Log instance (with no-argument constructor default file name is used)
// log.add("I am line"); - use reference to access .add method to append a single line
// log.add("I am another line"); - append another one
// */

//import java.io.Writer;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//public class Log {
//	File file;
//	Writer output;
//	String fileName;
//
//	/**
//	 * Class constructor. Will create a file with a given file name when invoked.
//	 * @param fileName - String-type name of a file to create
//	 */
//	public Log(String fileName){
//		this.fileName = fileName;
//		file = new File(fileName);
//	}
//
//
//	/**
//	 * No-argument constructor. Will create a file with default name "log.txt"
//	 */
//	public Log(){
//		this("log.txt");
//		fileName = "log.txt";
//	}
//
//	/**
//	 * Append a single line to file created with a class constructor.
//	 * @param singleLine - String-type line to append
//	 */
//	public void add(String singleLine){
//		try {
//			output = new BufferedWriter(new FileWriter(fileName, true));
//			output.append(singleLine + "\n");
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}

public class Log
{
	/**
	 * A log that stores the contents of each run
	 */
	private String[] runs = new String[100];
	
	/**
	 * A number specifying the desired log for a specific run
	 */
	private int logNumber;
	
	/**
	 * Creates a new log which will start logging for run #1
	 */
	public Log()
	{
		this.logNumber = 1;
	}
	
	/**
	 * Gets all logs for all runs
	 * @return runs Array holding every run's information
	 */
	public String[] getRuns()
	{
		return runs;
	}

	/**
	 * Gets the log number corresponding to the equivalent run number
	 * @return logNumber Location in the array corresponding to the same run number
	 */
	public int getLogNumber()
	{
		return logNumber;
	}

	/**
	 * Sets the log number corresponding to an equivalent run number
	 * @param logNumber Location in the array corresponding to the same run number
	 */
	public void setLogNumber(int logNumber)
	{
		this.logNumber = logNumber;
	}

	/**
	 * Adds one line to the log of a run
	 * @param line Information from a run that we want to store/print for later
	 */
	public void add(String line)
	{
		runs[logNumber] += line + "\n";
	}
	
	/**
	 * Increments the log number, signifying a new log for a new run has begun
	 */
	public void incrementLogNumber()
	{
		logNumber++;
	}
	
	/**
	 * Prints the most recent run's log
	 */
	public void printLatestLog()
	{
		System.out.println(runs[runs.length-1]);
	}
}