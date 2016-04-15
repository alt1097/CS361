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
	private ArrayList<ArrayList<Racer>> lanes = new ArrayList<>();
	/**
	 Indexes of the first Racers by lane.
	 */
	private ArrayList<Integer> firstIndexes = new ArrayList<>();
	/**
	 Indexes of the first not racing by lane.
	 */
	private ArrayList<Integer> queueIndexes = new ArrayList<>();
	/**
	 References to the assigned start Channels by lane.
	 */
	private ArrayList<Channel> startChannels = new ArrayList<>();
	/**
	 Reference to the assigned finish Channels by lane.
	 */
	private ArrayList<Channel> finishChannels = new ArrayList<>();

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
		return "";  //  TODO
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
			for(ArrayList<Racer> lane : lanes){
				Racer tempRacer = lane.get(number);
				if(tempRacer != null){
					return tempRacer;
				}
			}
		}
		else{
			for(ArrayList<Racer> lane : lanes){
				for(Racer racer : lane){
					if(racer.getNumber() == number){
						return racer;
					}
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
		return false;  //  TODO
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	@Override
	public boolean isRacing(Racer racer){
		return false;  //  TODO
	}

	/**
	 True if the Racer is able to be moved in the Parallel Group Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	@Override
	public boolean canBeMoved(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToFirst(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToNext(Racer racer){
		return false;  //  TODO
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Parallel Group Race.
	 @return True if Race can start.
	 */
	@Override
	public boolean canStart(){
		if(canStart){ // it was like this before canStart || canStartPARGRP();
			return true;
		}
		return false;  //  TODO
	}

	/**
	 Verifies that Channels are set up so that a Parallel Group Race can proceed.
	 */
	@Override
	public void channelVerify(){
		//  TODO
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Group Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	@Override
	public String trigger(Channel channel){
		//  TODO
		update();
		return "";  //  TODO
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
		//  TODO
	}

	/**
	 Runs the actions to finalize a Parallel Groups Race.
	 */
	public void endPARGRP(){
		//  TODO
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
	 @param data Hash table to add to.
	 */
	public void exportMePARGRP(Hashtable<String, Serializable> data){
		//  TODO
	}

	@Override
	public String exportMe() {
		// TODO Implement
		return null;
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		ongoing = false;
		ended = true;
		endedDisplay = raceStats();
		endPARGRP();
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