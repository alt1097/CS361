package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Com {
	public Com(String commandLine) {
		isValid = false;
		runNumber = -1;
		String[] cmdArgs = commandLine.split("\\s");
		int nextCmd = 0;
		// see if first argument is one of the legal time formats hh:mm:ss.S,
		if (cmdArgs[0].charAt(0) >= '0' && cmdArgs[0].charAt(0) <= '9') {
			nextCmd = 1;
			cmdTime = cmdArgs[0];
		}
		
		if (cmdArgs.length <= nextCmd) {
			System.out.println("Bad command");
			return;
		}
		
		if (cmdArgs[nextCmd].equalsIgnoreCase("conn"))
		{
			type = CmdType.eConn;
			if (cmdArgs.length == (nextCmd + 3)) {
				sensor = cmdArgs[nextCmd];
				nextCmd++;
				try {
					channel = Integer.parseInt(cmdArgs[nextCmd]);
					isValid = true;
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing CONN");
				}
			}
			if (!isValid)
				System.out.println("Error parsing CONN");
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("on"))
		{
			type = CmdType.eOn;
			isValid = true;
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("off"))
		{
			type = CmdType.eOff;
			isValid = true;
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("event"))
		{
			type = CmdType.eEvent;
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				event = cmdArgs[nextCmd];
				isValid = true;
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("print"))
		{
			type = CmdType.ePrint;
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					runNumber = Integer.parseInt(cmdArgs[nextCmd]);
					isValid = true;
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing PRINT");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("num"))
		{
			type = CmdType.eNum;
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					runNumber = Integer.parseInt(cmdArgs[nextCmd]);
					isValid = true;
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing PRINT");
				}
			}
		}

	}
	
	boolean isValid;
	String cmdTime;
	CmdType type;
	int channel;
	Date timeArg;
	String sensor;
	String event;
	int runNumber;
}
