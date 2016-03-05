package main;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

/**
 * Creates a text file in project root directory.
 * Allows to write a lines in this file.
 *
 * Use example:
 *      Log log = new Log(); - create a Log instance (with no-argument constructor default file name is used)
 log.add("I am line"); - use reference to access .add method to append a single line
 log.add("I am another line"); - append another one
 */

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class DebugLog {
	File file;
	Writer output;
	String fileName;

	/**
	 * Class constructor. Will create a file with a given file name when invoked.
	 * @param fileName - String-type name of a file to create
	 */
	public DebugLog(String fileName){
		this.fileName = fileName;
		file = new File(fileName);
	}


	/**
	 * No-argument constructor. Will create a file with default name "log.txt"
	 */
	public DebugLog(){
		this("log.txt");
		fileName = "log.txt";
	}

	/**
	 * Append a single line to file created with a class constructor.
	 * @param singleLine - String-type line to append
	 */
	public void add(String singleLine){
		try {
			output = new BufferedWriter(new FileWriter(fileName, true));
			output.append(singleLine + "\r\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}