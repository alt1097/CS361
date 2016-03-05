package main;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

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
		runs[logNumber] = "";
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
		runs[logNumber] = "";
	}
	
	/**
	 * Prints the most recent run's log
	 */
	public void printLatestLog()
	{
		System.out.println(runs[logNumber]);
	}
}