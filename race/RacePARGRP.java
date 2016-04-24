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
	private Date startTime;
	
	/**
	 Initializes the Parallel Groups components of Race.
	 */
	public RacePARGRP(ChronoTimer chrono){
		super("PARGRP", chrono);
		channelVerify();
		//  TODO
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
		if(!ongoing)
		{
			if(racers.size() < 8)
			{
				racers.add(new Racer(number));
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
				if(racer.getNumber() == number){
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
		return racers.remove(getRacer(number, false));
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	@Override
	public boolean isRacing(Racer racer){
		return true;
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
		if(racers.size() > 0)
		{
			for(int i = 0; i < racers.size(); ++i)
			{
				if(channels.get(i) == null){
					pass = false;
					break;
				}
			}
		}
		else
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
		
		for(int i = 0; i < racers.size(); ++i)
		{
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
		if(fail)
		{
			channels.clear();
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
				for(Racer r: racers)
				{
					r.setStartTime(startTime);
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
					channel.fireChannel(racer);
					channel.reset();
					getChrono().output(racer.getNumber()+" TRIG "+(channel.getName() + 1));
					getChrono().output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
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
		
		for(Racer r : racers)
		{
			if(r.getEndTime() == null)
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
		return "";  //  TODO
	}

	/**
	 Exports Parallel Group Race data into JSON format String.
	 */
	@Override
	public String exportMe() {
		Hashtable<String, Serializable> data = new Hashtable<>();
		data.put("racers", racers);
		return ChronoTimer.export.objectToJsonString(data);
	}

	@Override
	public void end() {
		ongoing = false;
		ended = true;
		endedDisplay = raceStats();
		ChronoTimer.log.add(print());
		ChronoTimer.log.addToExport(exportMe());
	}

	/**
	 Builds Parallel Group Race text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	public String raceStats(){
		return "";  //  TODO
	}
}