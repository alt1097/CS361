package main;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import race.*;
import channel.Channel;
import export.Export;

import javax.swing.*;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class ChronoTimer extends JFrame{
	/**
	 Indicates if the ChronoTimer is currently active.
	 */
	private static boolean powerState;
	/**
	 Default format all times will be parsed and printed with.
	 */
	public static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.S");
	/**
	 Format used specifically for printing race times.
	 */
	public static final SimpleDateFormat diffFormat = new SimpleDateFormat("HH:mm:ss.S");
	/**
	 Exists only if the time has been explicitly forced to a specific time, then is used as base time.
	 */
	private static Long systemStartTime;
	/**
	 Offset used to make time format to desired new time.
	 */
	private static long newOffset;
	/**
	 Used to immediately use a specified Date instead of getting the current time's.
	 */
	private static Date overrideDate;
	/**
	 Reference to all the channels on the system.
	 */
	private static Channel[] channels;
	/**
	 Reference to the Race.
	 */
	private static Race race;
	/**
	 Reference to DebugLog class for debugging.
	 */
	public static DebugLog debugLog;
	/**
	 Reference to Log class for printing.
	 */
	public static Log log;
	/**
	 Reference to Export to export data file.
	 */
	public static Export export;

	/**
	 Initializes the ChronoTimer.
	 */
	public ChronoTimer(){
		powerState = false;
		diffFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		systemStartTime = null;
		newOffset = 0L;
		overrideDate = null;
		channels = new Channel[8];
		for(int i = 0; i < 8; i++){
			channels[i] = new Channel(i);
		}
		race = new RaceIND();
		debugLog = new DebugLog("ChronoTimer Debug.txt");
		debugLog.add("\t\t"+new Date(System.currentTimeMillis()).toString());
		log = new Log();
		export = new Export();
	}

	//  ----------  COMMANDS  ----------

	/**
	 Toggles the system's power state.
	 */
	public void power(){
		power(false);
	}

	/**
	 Toggles the system's power state.
	 @param silent True to silence power()'s messages.
	 */
	private void power(boolean silent){
		if(powerState){
			powerState = false;
		}
		else{
			powerState = true;
			reset(true);
		}
		if(!silent){
			debugLog.add(format.format(getTime())+" POWER");
		}
		String output = "SYSTEM POWERED ";
		if(powerState){
			output += "ON";
		}
		else{
			output += "OFF";
		}
		output(output);
	}

	/**
	 Directly turns the system's power on.
	 */
	public void on(){
		String logOut = format.format(getTime())+" ON";
		if(powerState){
			logOut += " - SYSTEM ALREADY ON";
		}
		else{
			power(true);
		}
		debugLog.add(logOut);
	}

	/**
	 Directly turns the system's power off.
	 */
	public void off(){
		String logOut = format.format(getTime())+" OFF";
		if(powerState){
			power(true);
		}
		else{
			logOut += " - SYSTEM ALREADY OFF";
		}
		debugLog.add(logOut);
	}

	/**
	 Used before system terminates.  //  TODO:  ONLY FOR LOG PURPOSE
	 */
	public void exit(){
		debugLog.add(format.format(getTime())+" EXIT");
		output("SYSTEM TERMINATED");
	}

	/**
	 Returns the machine back to a clean state.
	 */
	public void reset(){
		String logOut = format.format(getTime())+" RESET";
		if(powerState){
			reset(false);
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 Returns the machine back to a clean state.
	 @param silent True to silence reset()'s messages.
	 */
	private void reset(boolean silent){
		for(int i = 0; i < 8; i++){
			if(channels[i].isOn()){
				channels[i].toggle();
			}
		}
		race = new RaceIND();
		log = new Log();
		if(!silent){
			output("SYSTEM RESET");
		}
	}

	/**
	 Sets the unit override time.
	 @param time String of desired time to start at.
	 */
	public void time(String time){
		String logOut = format.format(getTime())+" TIME "+time;
		if(race.ongoing()){
			logOut += " - RACE ONGOING";
		}
		else{
			try{
				long parsed = format.parse(time).getTime();
				systemStartTime = System.currentTimeMillis();
				newOffset = parsed;
				output("SYSTEM TIME SET TO "+format.format(new Date(parsed)));
			}catch(ParseException e){
				logOut += " - TIME COULD NOT BE PARSED";
			}
		}
		debugLog.add(logOut);
	}

	/**
	 If channel exists, toggle the blocking of input.
	 @param channel Channel number to toggle trigger ignoring.
	 */
	public void tog(int channel){
		toggle(channel);
	}

	/**
	 If channel exists, toggle the blocking of input.
	 @param channel Channel number to toggle trigger ignoring.
	 */
	public void toggle(int channel){
		String logOut = format.format(getTime())+" TOGGLE "+channel;
		if(powerState){
			Channel channelObj = getChannel(channel - 1);
			if(channelObj == null){
				logOut += " - CHANNEL DOES NOT EXIST";
			}
			else{
				channelObj.toggle();
				race.channelVerify();
				String output = "CHANNEL "+channel+" TOGGLED ";
				if(channelObj.isOn()){
					output += "ON";
				}
				else{
					output += "OFF";
				}
				output(output);
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If sensor does not exist, then add the desired sensor type to sensor number.
	 @param sensor Type of the sensor.
	 @param num Sensor number to add.
	 */
	public void conn(String sensor, int num){
		String logOut = format.format(getTime())+" CONN "+sensor+" "+num;
		num = num - 1;
		Channel channelObj = getChannel(num);
		if(channelObj == null){
			logOut += " - CHANNEL DOES NOT EXIST";
		}
		else{
			channelObj.connect(sensor);
			output("SENSOR TYPE ("+sensor+") CONNECTED TO CHANNEL "+num);
		}
		debugLog.add(logOut);
	}

	/**
	 If sensor exists, then remove the desired sensor number.
	 @param num Sensor number to remove.
	 */
	public void disc(int num){
		String logOut = format.format(getTime())+" DISC "+num;
		num = num - 1;
		Channel channelObj = getChannel(num);
		if(channelObj == null){
			logOut += " - CHANNEL DOES NOT EXIST";
		}
		else{
			channelObj.disconnect();
			output("SENSOR DISCONNECTED FROM CHANNEL "+num);
		}
		debugLog.add(logOut);
	}

	/**
	 If current race type is different, and if one is not ongoing, then change type of event and start new run.
	 @param type New type of race.
	 */
	public void event(String type){
		type = type.toUpperCase();
		String logOut = format.format(getTime())+" EVENT "+type;
		if(powerState){
			if(type.equals(race.getEventType())){
				logOut += " - EVENT TYPE SAME";
			}
			else{
				if(race.ongoing()){
					logOut += " - RACE ONGOING";
				}
				else{
					output("EVENT TYPE CHANGING TO "+type);
					newRun(type, true);
				}
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If there is not a race ongoing, then create the race structure of the set event type.
	 */
	public void newRun(){
		newRun(race.getEventType(), false);
	}

	/**
	 If there is not a race ongoing, then create the race structure of the set event type.
	 @param type Type that the Race should be.
	 @param silent True to silence newRun()'s messages.
	 */
	private void newRun(String type, boolean silent){
		type = type.toUpperCase();
		String logOut = format.format(getTime())+" NEWRUN";
		if(powerState){
			if(race.ongoing()){
				logOut += " - RACE ONGOING";
			}
			else if(type.equals("IND") || type.equals("PARIND") || type.equals("GRP") || type.equals("PARGRP")){
				if(log.getExportData(log.getLogNumber()).length() != 0){
					log.incrementLogNumber();
				}
				switch(type){
					case "IND":
						race = new RaceIND();
						break;
					case "PARIND":
						race = new RacePARIND();
						break;
					case "GRP":
						race = new RaceGRP();
						break;
					case "PARGRP":
						race = new RacePARGRP();
						break;
					default:
						//  TODO
				}
				output("NEW RUN TYPE ("+type+") STARTED");
			}
			else{
				logOut += " - EVENT TYPE DOES NOT EXIST";
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		if(!silent){
			debugLog.add(logOut);
		}
	}

	/**
	 If there is a race ongoing, then trigger the end of the race.
	 */
	public void endRun(){
		String logOut = format.format(getTime())+" ENDRUN";
		if(powerState){
			if(race.ongoing()){
				race.end();
				output("RUN ENDED");
			}
			else{
				logOut += " - RACE NOT ONGOING";
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 Triggers printing the contents of the whole log passed the last time the command was issued.
	 */
	public void print(){
		String logOut = format.format(getTime())+" PRINT";
		if(powerState){
			output("PRINTING RACE STATUS...");
			System.out.println(race.print());
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 Triggers printing the contents of the log of a specific run number.
	 @param run Number of the run to print.
	 */
	public void print(int run){
		String logOut = format.format(getTime())+" PRINT "+run;
		if(powerState){
			String runLog = log.getRuns(run);
			if(runLog != null){
				output("PRINTING LOG FOR RUN "+run+"...");
				System.out.println(runLog);
			}else{
				logOut += " - RUN DOES NOT EXIST";
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 Exports the log data for a run to an XML file.
 	 @param run Run number to export.
	 */
	public void export(int run){
		String logOut = format.format(getTime())+" EXPORT "+run;
		if(powerState){
			String saveData = null;
			if(log.getLogNumber() == run && !race.ended()){
				output("EXPORTING CURRENT RUN ("+run+") DATA...");
				saveData = race.exportMe();
			}else{
				String runExport = log.getExportData(run);
				if(runExport != null){
					output("EXPORTING RUN "+run+" DATA...");
					saveData = runExport;
				}else{
					logOut += " - RUN DOES NOT EXIST";
				}
			}
			if(saveData != null){
				System.out.println("! ! File chooser is open, look behind window ! !");
				JFileChooser fc = new JFileChooser();
				int check = fc.showSaveDialog(ChronoTimer.this);
				if(check == JFileChooser.APPROVE_OPTION){
					try{
						FileWriter fw = new FileWriter(fc.getSelectedFile() + ".json");
						fw.write(saveData);
						fw.close();
						output("EXPORT SUCCESSFUL...");
					}catch(IOException e){
						output("EXPORT FAILED, FILE COULD NOT BE CREATED...");
					}
				}
				output("EXPORT COMPLETE");
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If the race has not started, and the racer does not exist, then add them to the race.
	 If the race has started, and the racer exists, and the racer is racing, then move them to first in their lane.
	 If the racer exists, and they are not currently racing, then move them to the position to be the next to race.
	 @param number Number of the racer to add / move.
	 */
	public void num(int number){
		String logOut = format.format(getTime())+" NUM "+number;
		if(powerState){
			Racer racer = race.getRacer(number, false);
			if(racer == null){
				if(race.ended()){
					logOut += " - RACE HAS ENDED";
				}
				else{
					race.addRacer(number, false);
					output("RACER "+number+" ADDED");
				}
			}
			else{
				if(race.canBeMoved(racer)){
					if(race.isRacing(racer)){
						if(race.moveToFirst(racer)){
							output("RACER "+number+" MOVED TO FIRST");
						}
						else{
							logOut += " - NOT ENOUGH RACERS TO MOVE IN RACE";
						}
					}
					else{
						if(race.moveToNext(racer)){
							output("RACER "+number+" NEXT TO START");
						}
						else{
							logOut += " - NOT ENOUGH RACERS TO MOVE IN QUEUE";
						}
					}
				}
				else{
					logOut += " - THE RACER CANNOT BE MOVED";
				}
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 TODO:  IMPLMENT FUNCTION TO REMOVE RACER IF NOT RACING AND -1 FROM LAST INDEX
	 If a race is not ongoing, and the racer exists, remove them.
	 @param number Number of the racer to remove.
	 */
	public void clr(int number){
		String logOut = format.format(getTime())+" CLR "+number;
		if(powerState){
			if(race.ongoing()){
				logOut += " - RACE IS ONGOING";
			}
			else if(race.ended()){
				logOut += " - RACE HAS ENDED";
			}
			else{
				if(race.removeRacer(number)){
					output("RACER "+number+" REMOVED");
				}
				else{
					logOut += " - RACER DOES NOT EXIST";
				}
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If the event type is IND, a race is ongoing, and there are at least two racers still racing, then swap first and second.
	 */
	public void swap(){
		String logOut = format.format(getTime())+" SWAP";
		if(powerState){
			if(race.getEventType().equals("IND")){
				if(race.ongoing()){
					if(((RaceIND) race).swap()){
						output("TWO LEAD POSITIONS SWAPPED");
					}
					else{
						logOut += " - NOT ENOUGH RACERS TO SWAP";
					}
				}
				else{
					logOut += " - RACE IS NOT ONGOING";
				}
			}
			else{
				logOut += " - EVENT TYPE IS NOT IND";
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If the event type is IND or GRP, and a race has begun, the racer in first will be marked to not finish.
	 */
	public void dnf(){
		String logOut = format.format(getTime())+" DNF";
		if(powerState){
			boolean pass = false;
			RaceIND raceIND = null;
			RaceGRP raceGRP = null;
			if(race.getEventType().equals("IND")){
				raceIND = (RaceIND) race;
				pass = true;
			}
			else if(race.getEventType().equals("GRP")){
				raceGRP = (RaceGRP) race;
				pass = true;
			}
			if(pass){
				if(race.ongoing()){
					if(raceIND != null){
						raceIND.dnf();
					}
					else{
						raceGRP.dnf();
					}
				}
				else{
					logOut += " - RACE IS NOT ONGOING";
				}
			}
			else{
				logOut += " - EVENT TYPE IS NOT IND OR GRP";
			}
		}
		else{
			logOut += " - SYSTEM NOT ON";
		}
		debugLog.add(logOut);
	}

	/**
	 If the channel exists, trigger it.
	 @param channel Number of the channel to trigger.
	 */
	public void trig(int channel){
		trig(channel, false);
	}

	/**
	 If the channel exists, trigger it.
	 @param channel Number of the channel to trigger.
	 @param silent True to silence trig()'s messages.
	 */
	private String trig(int channel, boolean silent){
		String logOut = format.format(getTime())+" TRIG "+channel;
		String retMess = "";
		if(powerState){
			Channel channelObj = getChannel(channel - 1);
			if(channelObj != null && channelObj.isOn()){
				if(race.canStart()){
					if(!race.ended() || race.ongoing()){
						retMess += race.trigger(channelObj);
					}
					else{
						retMess += " - RACE IS NOT ONGOING";
					}
				}
				else{
					retMess += " - RACE IS NOT SET UP";
				}
			}
			else{
				retMess += " - CHANNEL IS NOT ENABLED";
			}
		}
		else{
			retMess += " - SYSTEM NOT ON";
		}
 		if(!silent){
			debugLog.add(logOut+retMess);
		}
		return retMess;
	}

	/**
	 TODO:  CHECK IF INTERPRETER WILL SHORTCUT
	 Shortcut of trig(1)
	 */
	public void start(){
		String logOut = format.format(getTime())+" START";
		logOut += trig(1, true);
		debugLog.add(logOut);
	}

	/**
	 TODO:  CHECK IF INTERPRETER WILL SHORTCUT
	 Shortcut of trig(2)
	 */
	public void finish(){
		String logOut = format.format(getTime())+" FINISH";
		logOut += trig(2, true);
		debugLog.add(logOut);
	}

	//  ----------  FUNCTIONALITY METHODS  ----------

	/**
	 True if the system is currently powered on.
	 @return True if unit is on.
	 */
	public boolean isOn(){
		return powerState;
	}

	/**
	 Gets the desired channel from number.
	 @param number Number of channel to get.
	 @return The Channel object.
	 */
	public static Channel getChannel(int number){
		if(number > -1 && number < 8){
			return channels[number];
		}
		return null;
	}

	/**
	 Sets a time to override getTime().
	 @param time The time to use.
	 */
	public void useTime(String time){
		try{
			overrideDate = format.parse(time);

		}catch(ParseException e){
			debugLog.add("---- "+format.format(getTime())+" DATE ENTERED WAS IMPROPERLY FORMATTED");
		}
	}

	/**
	 Gets the time offset when an override time is active.
	 @return The time offset.
	 */
	public long getNewOffset(){
		return newOffset;
	}

	/**
	 Gets the unit's current time.
	 @return Current unit time as Date.
	 */
	public static Date getTime(){
		if(overrideDate != null){
			return overrideDate;
		}
		if(systemStartTime == null){
			return new Date(System.currentTimeMillis());
		}
		return new Date(System.currentTimeMillis() - systemStartTime + newOffset);
	}

	/**
	 Prints input to the screen and records it to Log.
	 @param out Text to print and save.
	 */
	public static void output(String out){
		System.out.println(out);
		log.add(out);
	}
}