package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import race.*;
import channel.Channel;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/26/2016
 */
public class ChronoTimer{
	/**
	 //  TODO:  SHOULD INTERPRETER HAVE COPY / ORIGINAL FORMAT?
	 Default format all times will be parsed and printed with.
	 */
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.S");
	/**
	 Exists only if the time has been explicitly forced to a specific time, then is used as base time.
	 */
	private Long systemStartTime;
	/**
	 Offset used to make time format to desired new time.
	 */
	private long newOffset;
	/**
	 Used to immediately use a specified Date instead of getting the current time's.
	 */
	private Date overrideDate;
	/**
	 Type of the race.
	 */
	private String eventType = "IND";
	/**
	 TODO:  CURRENTLY DOES NOT USE UNIT TIME TO MARK RACER TIMES
	 Reference to all the channels on the system.
	 */
	private Channel[] channels = new Channel[8];
	/**
	 Reference to the Race.
	 */
	private Race race = new RaceIND(this);
	/**
	 TODO:  CURRENTLY DOES NOT SATISFY ANY OF THE LOG FUNCTIONALITY
	 Reference to the Log.
	 */
	private Log log = new Log();

	/**
	 Creates the main ChronoTimer unit.
	 Creates  //  TODO
	 */
	public ChronoTimer(){
		for(int i = 0; i < 8; i++){
			channels[i] = new Channel(i);
		}
		//  TODO
	}

	//  ----------  COMMANDS  ----------

	 //  POWER  Handled by Interpreter?
	 //  EXIT  Handled by Interpreter?
	 //  RESET  Handled by Interpreter?

	/**
	 Sets the unit override time.
	 @param time String of desired time to start at.
	 */
	public void time(String time){
		String logOut = " TIME " + time;
		if(race.ongoing()){
			logOut += " - RACE ONGOING";
		}
		else{
			try{
				long parsed = format.parse(time).getTime();
				systemStartTime = System.currentTimeMillis();
				newOffset = parsed;
			}catch(ParseException e){
				logOut += " - TIME COULD NOT BE PARSED";
			}
		}
		log.add(logOut);
	}

	/**
	 ??????????????????????????????????????????????????
	 @param channel ???????????????????????????????????
	 */
	public void tog(int channel){
		//  TODO
	}

	/**
	 If channel exists, toggle the blocking of input.
	 @param channel Channel number to toggle trigger ignoring.
	 */
	public void toggle(int channel){
		Channel channelObj = getChannel(channel);
		String logOut = getTime()+" TOGGLE "+channel;
		if(channelObj == null){
			logOut += " - CHANNEL DOES NOT EXIST";
		}
		else{
			channelObj.toggle();
			race.channelVerify();
		}
		log.add(logOut);
	}

	/**
	 If sensor does not exist, then add the desired sensor type to sensor number.
	 @param sensor Type of the sensor.
	 @param num Sensor number to add.
	 */
	public void conn(String sensor, int num){
		Channel channelObj = getChannel(num);
		String logOut = getTime()+" CONN "+sensor+" "+num;
		if(channelObj == null){
			logOut += " - CHANNEL DOES NOT EXIST";
		}
		else{
			channelObj.connect(sensor);
		}
		log.add(logOut);
	}

	/**
	 If sensor exists, then remove the desired sensor number.
	 @param num Sensor number to remove.
	 */
	public void disc(int num){
		Channel channelObj = getChannel(num);
		String logOut = getTime()+" DISC "+num;
		if(channelObj == null){
			logOut += " - CHANNEL DOES NOT EXIST";
		}
		else{
			channelObj.disconnect();
		}
		log.add(logOut);
	}

	/**
	 If current race type is different, and if one is not ongoing, then change type of event and start new run.
	 @param type New type of race.
	 */
	public void event(String type){
		String logOut = getTime()+" EVENT "+type;
		if(type.equals(eventType)){
			logOut += " - EVENT TYPE SAME";
		}
		else{
			if(race.ongoing()){
				logOut += " - RACE ONGOING";
			}
			else{
				eventType = type;
				newRun(true);
			}
		}
		log.add(logOut);
	}

	/**
	 If there is not a race ongoing, then create the race structure of the set event type.
	 Note:  Only use this one to start new runs!
	 */
	public void newRun(){
		newRun(false);
	}

	/**
	 If there is not a race ongoing, then create the race structure of the set event type.
	 Note:  Only event() will use the 'silent' variable to prevent double messages!
	 @param silent True to stop newRun()'s messages from appearing.
	 */
	public void newRun(boolean silent){
		String logOut = getTime()+" NEWRUN";
		if(race.ongoing()){
			logOut += " - RACE ONGOING";
		}
		else{
			switch(eventType){
				case "IND":
					race = new RaceIND(this);
					break;
				case "PARIND":
					race = new RacePARIND(this);
					break;
				case "GRP":
					race = new RaceGRP(this);
					break;
				case "PARGRP":
					race = new RacePARGRP(this);
					break;
				default:
					logOut += " - EVENT TYPE DOES NOT EXIST";
			}
		}
		if(!silent){
			log.add(logOut);
		}
	}

	/**
	 If there is a race ongoing, then trigger the end of the race.
	 */
	public void endRun(){
		String logOut = getTime()+" ENDRUN";
		if(race.ongoing()){
			race.end();
		}
		else{
			logOut += " - RACE NOT ONGOING";
		}
		log.add(logOut);
	}

	/**
	 Triggers printing the contents of the whole log passed the last time the command was issued.
	 */
	public void print(){
		//  TODO
	}

	/**
	 Triggers printing the contents of the log of a specific run number.
	 @param run Number of the run to print.
	 */
	public void print(int run){
		//  TODO
	}

	/**
	 Exports the log data for a run to an XML file.
 	 @param run Run number to export.
	 */
	public void export(int run){
		//  TODO
	}

	/**
	 If the race has not started, and the racer does not exist, then add them to the race.
	 If the race has started, and the racer exists, and the racer is racing, then move them to first in their lane.
	 If the racer exists, and they are not currently racing, then move them to the position to be the next to race.
	 @param number Number of the racer to add / move.
	 */
	public void num(int number){
		Racer racer = race.getRacer(number);
		String logOut = getTime()+" NUM "+number;
		if(racer == null){
			if(race.ongoing()){
				//  TODO:  DO WE WANT TO BE ABLE TO ADD NEW RACERS IF RACE IS ONGOING?
			}
			else if(race.ended()){
				logOut += " - RACE HAS ENDED";
			}
			else{
				race.addRacer(number);
			}
		}
		else{
			if(race.ongoing()){
				if(race.isRacing(racer)){
					if(!race.moveToFirst(racer)){
						logOut += " - NOT ENOUGH RACERS TO MOVE IN RACE";
					}
				}else{
					if(!race.moveToNext(racer)){
						logOut += " - NOT ENOUGH RACERS TO MOVE IN QUEUE";
					}
				}
			}
			else{
				logOut += " - RACE IS NOT ONGOING";
			}
		}
		log.add(logOut);
	}

	/**
	 TODO:  CHECK IF FUNCTIONALITY IS DESIRED, CURRENT ONE IN PROJECT DESCRIPTION SOUNDS STUPID
	 If a race is not ongoing, and the racer exists, remove them.
	 @param number Number of the racer to remove.
	 */
	public void clr(int number){
		String logOut = getTime()+" CLR "+number;
		if(race.ongoing()){
			logOut += " - RACE IS ONGOING";
		}
		else if(race.ended()){
			logOut += " - RACE HAS ENDED";
		}
		else{
			if(!race.removeRacer(number)){
				logOut += " - RACER DOES NOT EXIST";
			}
		}
		log.add(logOut);
	}

	/**
	 If the event type is IND, a race is ongoing, and there are at least two racers still racing, then swap first and second.
	 */
	public void swap(){
		String logOut = getTime()+" SWAP";
		if(eventType.equals("IND")){
			if(race.ongoing()){
				if(!((RaceIND) race).swap()){
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
		log.add(logOut);
	}

	/**
	 If the event type is IND or GRP, and a race has begun, the racer in first will be marked to not finish.
	 */
	public void dnf(){
		String logOut = getTime()+" DNF";
		boolean pass = false;
		RaceIND raceIND = null;
		RaceGRP raceGRP = null;
		if(eventType.equals("IND")){
			raceIND = (RaceIND) race;
			pass = true;
		}
		else if(eventType.equals("GRP")){
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
		log.add(logOut);
	}

	/**
	 If the channel exists, trigger it.
	 @param channel Number of the channel to trigger.
	 @param silent Used to prevent messages from printing for shortcuts.
	 */
	public String trig(int channel, boolean silent){
		Channel channelObj = getChannel(channel);
		String logOut = getTime()+" TRIG "+channel;
		String retMess = "";
		if(channelObj.isOn()){
			if(!race.ended() || race.ongoing()){
				retMess += race.trigger(channelObj);
			}
			else{
				retMess += " - RACE IS NOT ONGOING";
			}
		}
		else{
			retMess += " - CHANNEL IS NOT ENABLED";
		}
 		if(!silent){
			log.add(logOut+retMess);
		}
		return retMess;
	}

	/**
	 TODO:  CHECK IF INTERPRETER WILL SHORTCUT
	 Shortcut of trig(1)
	 */
	public void start(){
		String logOut = getTime()+" START";
		logOut += trig(1, true);
		log.add(logOut);
	}

	/**
	 TODO:  CHECK IF INTERPRETER WILL SHORTCUT
	 Shortcut of trig(2)
	 */
	public void finish(){
		String logOut = getTime()+" FINISH";
		logOut += trig(2, true);
		log.add(logOut);
	}

	//  ----------  FUNCTIONALITY METHODS  ----------

	/**
	 Gets the type of the race.
	 @return Type of the race.
	 */
	public String getEventType(){
		return eventType;
	}

	/**
	 Gets the desired channel from number.
	 @param number Number of channel to get.
	 @return The Channel object.
	 */
	public Channel getChannel(int number){
		return channels[number];
	}

	/**
	 Sets a time to override getTime().
	 @param time The time to use.
	 */
	public void useTime(String time){
		try{
			overrideDate = format.parse(time);

		}catch(ParseException e){
			e.printStackTrace();  //  TODO
		}
	}

	/**
	 Gets the unit's current time.
	 @return Current unit time as Date.
	 */
	public Date getTime(){
		if(overrideDate != null){
			Date carry = overrideDate;
			overrideDate = null;
			return carry;
		}
		if(systemStartTime == null){
			return new Date(System.currentTimeMillis());
		}
		return new Date(System.currentTimeMillis() - systemStartTime + newOffset);
	}
}