package race;

import channel.Channel;
import main.ChronoTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class RacePARGRP extends Race{
	/**
	 Contains the Racers in the race according to which lane they're in.
	 */
	private ArrayList<Racer> racers = new ArrayList<>();
	/**
	 References to the assigned Channels by lane.
	 */
	private ArrayList<Channel> channels = new ArrayList<>();
	/**
	 Start time to use for all Racers.
	 */
	private Long startTime;
	
	/**
	 * Finish places
	 */
	private ArrayList<Racer> places = new ArrayList<>();
	
	/**
	 Initializes the Parallel Groups components of Race.
	 */
	public RacePARGRP(ChronoTimer chrono){
		super("PARGRP", chrono);
		for(int i = 0; i < 8; i++){
			racers.add(null);
			channels.add(null);
		}
		channelVerify();
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 @return String of any messages.
	 */
	@Override
	public String addRacer(int number, boolean toFront){
		String logOut = "";
		int count = 0;
		if(!ongoing)
		{
			for(Racer racer : racers)
			{
				if(racer != null)
				{
					++count;
				}
			}
			if(count < 8)
			{
				for(int i = 0; i < 8; i++){
					if(racers.get(i) == null){
						racers.set(i, new Racer(number));
						channelVerify();
						break;
					}
				}
			}
			else
			{
				logOut += " - 8 RACERS ALREADY ENTERED";
			}
		}
		else
		{
			logOut += " - RACE IS ONGOING";
		}
			return logOut;
	}

	/**
	 Gets a Racer in a Parallel Group Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Parallel Group Race.
	 @return The Racer object.
	 */
	@Override
	public Racer getRacer(int number, boolean byPlace){
		if(byPlace){
			return racers.get(number);
		}
		else{
			for(Racer racer : racers){
				if(racer != null && racer.getNumber() == number){
					return racer;
				}
			}
			for(Racer racer : places)
			{
				if(racer.getNumber() == number)
				{
					return racer;
				}
			}
		}
		return null;
	}

	/**
	 If the Racer exists, then remove them from the Race.
	 @param number Number of the Racer to remove.
	 @return If the Racer exists.
	 */
	@Override
	public boolean removeRacer(int number){
		int index = racers.indexOf(getRacer(number, false));
		if(index != -1){
			racers.set(index, null);
			channelVerify();
			return true;
		}
		return false;
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	@Override
	public boolean isRacing(Racer racer){
		return false;
	}

	/**
	 True if the Racer is able to be moved in the Parallel Group Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	@Override
	public boolean canBeMoved(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToFirst(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToNext(Racer racer){
		return false;
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Parallel Group Race.
	 @return True if Race can start.
	 */
	@Override
	public boolean canStart(){
		if(canStart){
			return true;
		}
		boolean pass = true;
		int count = 0;
		
		
		for(int i = 0; i < 8; ++i)
		{
			if(racers.get(i) != null){
				++count;
				if(channels.get(i) == null){
					
					pass = false;
					break;
				}
			}
		}
		if(count == 0)
		{
			pass = false;
		}
		
		canStart = pass;
		return pass;
	}

	/**
	 Verifies that Channels are set up so that a Parallel Group Race can proceed.
	 */
	@Override
	public void channelVerify(){
		boolean fail = false;
		for(int i = 0; i < 8; ++i)
		{
			if(racers.get(i) != null){
				Channel temp = ChronoTimer.getChannel(i);
				if(temp != null && temp.isOn()){
					temp.setChanType("FINISH");
					channels.set(i, temp);
				}
				else
				{
					fail = true;
					break;
				}
			}
		}	
		if(fail)
		{
			for(int i = 0; i < 8; i++){
				channels.set(i, null);
			}
		}
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Group Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	@Override
	public String trigger(Channel channel){
		String retMes = "";
		if(channels.contains(channel))
		{
			if(channel.getName() == 0 && startTime == null)
			{
				startTime = ChronoTimer.getTime();
				ongoing = true;
				for(Racer racer: racers)
				{
					if(racer != null){
						racer.setStartTime(startTime);
					}
				}
				getChrono().output("TRIG RACE HAS STARTED");
			}
			else
			{
				if(startTime == null)
				{
					retMes += " - RACE HASN'T STARTED";
				}
				else
				{
					Racer racer = racers.get(channel.getName());
					if(racer == null){
						retMes += " - NO RACER IN LANE";
					}
					else{
						racers.set(channel.getName(), null);
						places.add(racer);
						channel.fireChannel(racer);
						channel.reset();
						getChrono().output(racer.getNumber()+" TRIG "+(channel.getName() + 1));
						getChrono().output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
					}
				}
			}
		}
		else
		{
			retMes += " - CHANNEL IS NOT USED";
		}
		
		update();
		return retMes;
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
	boolean hasEnded = true;
		
		for(Racer racer : racers)
		{
			if(racer != null)
			{
				hasEnded = false;
				break;
			}
		}
		
		if(hasEnded)
		{
			end();
		}
	}
	
	/**
	 Prints the current status of all Racers in Parallel Group Race.
	 @return The Racer status printout.
	 */
	@Override
	public String print(){
		String sep = "--------------------";
		String record = "";
		record += sep+"\n";
		record += ": : Run #"+ChronoTimer.log.getLogNumber()+" : : "+eventType+" : : ";
		if(ended()){
			record += "Ended";
		}
		else if(ongoing()){
			record += "Ongoing";
		}
		else{
			record += "Not Started";
		}
		record += " :\n";
		int place = 1;
		for(Racer racer : places){
			String placeStr = place+"";
			for(int i = placeStr.length(); i < 3; i++){
				placeStr += " ";
			}
			record += placeStr+"  #"+racer.getNumber()+"\tFinal: ";
			Long diff = racer.getFinalTime();
			if(diff != null){
				record += ChronoTimer.diffFormat.format(diff);
			}
			else{
				record += "DNF";
			}
			record += "\n";
			place++;
		}
		place = 0;
		String tempStr = "";
		for(Racer racer : racers){
			place++;
			if(racer != null){
				tempStr += "#"+racer.getNumber()+"    "+place+"\n";
			}
		}
		if(place > 0){
			record += "\t~~~Not Finished~~~\n"+tempStr;
		}
		return record;
	}

	/**
	 Exports Parallel Group Race data into JSON format String.
	 */
	@Override
	public String exportMe() {
		Hashtable<String, Serializable> data = new Hashtable<>();
		data.put("eventType", super.eventType);
		data.put("canStart", super.canStart);
		data.put("ongoing", super.ongoing);
		data.put("ended", super.ended);
		data.put("racers", racers);
		for(int i = 0; i < 8; i++){
			Channel channel = channels.get(i);
			if(channel != null){
				data.put("channel_"+i, channel.getName());
			}
		}
		if(startTime != null){
			data.put("startTime", startTime);
		}
		data.put("places", places);
		return ChronoTimer.export.objectToJsonString(data);
	}

	/**
	 Exports Racers for display on Server.
	 @return The JSON data.
	 */
	public String exportServer(){
		Hashtable<String, Serializable> data = new Hashtable<>();
		ArrayList<Racer> collection = new ArrayList<>();
		collection.addAll(places);
		for(Racer racer : racers){
			if(racer != null){
				collection.add(racer);
			}
		}
		data.put("racers", collection);
		return ChronoTimer.export.objectToJsonString(data);
	}

	/**
	 Builds Parallel Group Race text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	public String raceStats(){
		if(endedDisplay == null){
			String output = "";
			if(startTime == null){
				output += "- RACE NOT STARTED -";
			}
			else{
				output += "ELAPSED: "+(ChronoTimer.diffFormat.format(ChronoTimer.getTime() - startTime));
			}
			output += "\n";
			int lane = 0;
			for(Racer racer : racers){
				lane++;
				if(racer != null){
					output += "\n"+racer.getNumber()+"\t"+lane;
				}
			}
			output += "\n";
			for(Racer racer : places){
				if(racer.getEndTime() != null){
					output += "\n"+racer.getNumber()+"\t"+ChronoTimer.diffFormat.format(racer.getFinalTime())+" F";
				}
			}
			return output;
		}
		return endedDisplay;
	}
}