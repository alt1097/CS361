package main;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class ComHandler {
	ChronoTimer chrono;
	
	public ComHandler(ChronoTimer chrono) {
		this.chrono = chrono;
	}

	/**
	 * Parses the command line into ChronoTimer method calls
	 * @param line Line of test file.
	 */
	public void handleCommand(String line) {
		boolean isValid = false;
		String[] cmdArgs = line.split("\\s");
		int nextCmd = 0;
		// see if first argument is one of the legal time formats hh:mm:ss.S,
		if (cmdArgs[0].charAt(0) >= '0' && cmdArgs[0].charAt(0) <= '9') {
			nextCmd = 1;
			chrono.useTime(cmdArgs[0]);
		}
		
		if (cmdArgs.length <= nextCmd) {
			System.out.println("Bad command");
			return;
		}

		if (cmdArgs[nextCmd].equalsIgnoreCase("exit"))
		{
			chrono.exit();
			System.out.println("The simulator will now exit");
//			System.exit(0);
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("conn"))
		{
			if (cmdArgs.length == (nextCmd + 3)) {
				++nextCmd;
				String sensor = cmdArgs[nextCmd];
				nextCmd++;
				try {
					int channel = Integer.parseInt(cmdArgs[nextCmd]);
					isValid = true;
					chrono.conn(sensor, channel);
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
			chrono.on();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("off"))
		{
			chrono.off();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("print"))
		{
			chrono.print();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("endrun"))
		{
			chrono.endRun();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("newrun"))
		{
			chrono.newRun();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("start"))
		{
			chrono.start();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("finish"))
		{
			chrono.finish();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("dnf"))
		{
			chrono.dnf();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("power"))
		{
			chrono.power();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("swap"))
		{
			chrono.swap();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("reset"))
		{
			chrono.reset();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("event"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				chrono.event(cmdArgs[nextCmd]);
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("time"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				chrono.time(cmdArgs[nextCmd]);
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("print"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.print(Integer.parseInt(cmdArgs[nextCmd]));
					isValid = true;
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing PRINT");
				}
			}
			else
				chrono.print();
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("num"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.num(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing NUM");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("clr"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.clr(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing CLR");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("toggle") || cmdArgs[nextCmd].equalsIgnoreCase("tog"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.toggle(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing TOGGLE");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("trig") || cmdArgs[nextCmd].equalsIgnoreCase("trigger"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.trig(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing TRIG");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("disc"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.disc(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing DISC");
				}
			}
		}
		else if (cmdArgs[nextCmd].equalsIgnoreCase("export"))
		{
			++nextCmd;
			if (cmdArgs.length == (nextCmd+1)) {
				try {
					chrono.export(Integer.parseInt(cmdArgs[nextCmd]));
				}
				catch (NumberFormatException e) {
					System.out.println("Error parsing EXPORT");
				}
			}
		}

	}
}
