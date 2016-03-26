package main;


/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class Log
{
	/**
	 * Stores the logged contents of each run
	 */
	private String[] runs = new String[500];
	
	/**
	 * Stores the logged contents of each run and stores data to be exported to file/USB
	 */
	private String[] exportData = new String[500];
	
	/**
	 * A number specifying the desired log for a specific run
	 */
	private int logNumber;
	
	/**
	 * Creates a new log which will start logging at run #1
	 */
	public Log()
	{
		this.logNumber = 1;
		runs[logNumber] = "";
		exportData[logNumber] = "";
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
	 * Gets any run by using an input number entered by the user, as long as it exists
	 * @param input User-entered number that corresponds to the desired run
	 * @return exportData[input] Export string for a log
	 */
	public String getRuns(int input)
	{
		if(input <= logNumber)
		{
			return runs[input];
		}
		else
		{
			System.out.println("Run does not exist for this number");
			return null;
		}
	}
	
	/**
	 * Gets all logged info for all runs in the format to be exported to USB/file
	 * @return
	 */
	public String[] getExportData()
	{
		return exportData;
	}

	/**
	 * Gets any export string by using an input number entered by the user,
	 * as long as it exists
	 * @param input User-entered number that corresponds to the desired export string
	 * @return exportData[input] Export string for a log
	 */
	public String getExportData(int input)
	{
		if(input <= logNumber)
		{
			return exportData[input];
		}
		else
		{
			System.out.println("Export data does not exist for this number");
			return null;
		}
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
	 * Adds one line to the file to be used for export
	 * @param line Information from a run that we want to export
	 */
	public void addToExport(String line)
	{
		exportData[logNumber] += line + "\n";
	}
	
	/**
	 * Increments the log number, signifying a new log for a new run has begun
	 * as well as an export capability for the new run
	 */
	public void incrementLogNumber()
	{
		logNumber++;
		runs[logNumber] = "";
		exportData[logNumber] = "";
	}
	
	/**
	 * Prints the most recent run's log
	 */
	public void printLatestLog()
	{
		System.out.println(runs[logNumber]);
	}
}