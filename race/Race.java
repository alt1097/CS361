package race;

import channel.Channel;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public abstract class Race{
	/**
	 Type of the race.
	 */
	protected String eventType;
	/**
	 Quick override to bypass start check once Race has started.
	 */
	protected boolean canStart = false;
	/**
	 Indicates if the race is currently in progress.
	 */
	protected boolean ongoing = false;
	/**
	 Indicates if the race is currently over.
	 */
	protected boolean ended = false;
	/**
	 Final displayed message once the race has ended.
	 */
	protected String endedDisplay;

	/**
	 Initializes the default Race requirements.
	 @param type Type the Race should be.
	 */
	public Race(String type){
		if(type.matches("IND|PARIND|GRP|PARGRP")){
			eventType = type;
		}else{
			//  TODO - Ignore race instantiation due to incorrect race type
			// add error to log or interrupt program
		}		
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 @return String of any messages.
	 */
	public abstract String addRacer(int number, boolean toFront);

	/**
	 Gets the desired Racer from number.
	 @param number Number of the Racer.
	 @param byPlace True to get a Racer based on position in Race.
	 @return The Racer object.
	 */
	public abstract Racer getRacer(int number, boolean byPlace);

	/**
	 If the Racer exists, then remove them from the Race.
	 @param number Number of the Racer to remove.
	 @return If the Racer exists.
	 */
	public abstract boolean removeRacer(int number);

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public abstract boolean isRacing(Racer racer);

	/**
	 True if the Racer is able to be moved in the Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public abstract boolean canBeMoved(Racer racer);

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public abstract boolean moveToFirst(Racer racer);
	

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public abstract boolean moveToNext(Racer racer);

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers.
	 @return True if Race can start.
	 */
	public abstract boolean canStart();

	/**
	 True if the Race is currently in progress.
	 @return True if ongoing.
	 */
	public boolean ongoing(){
		return ongoing;
	}

	/**
	 True if the Race has ended.
	 @return True if Race has ended.
	 */
	public boolean ended(){
		return ended;
	}

	/**
	 Gets the type of the race.
	 @return Type of the race.
	 */
	public String getEventType(){
		return eventType;
	}

	/**
	 Verifies that Channels are set up so that a Race can proceed.
	 */	
	public abstract void channelVerify();

	/**
	 Triggers the Channel specified.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	// moved "EVENT TYPE NOT FOUND" checking in constructor 
	public abstract String trigger(Channel channel);
	

	/**
	 Runs the actions to finalize a Race.
	 */
	public abstract void end();
	
	/**
	 Prints the current status of all Racers.
	 @return The Racer status printout.
	 */
	public abstract String print();
	

	/**
	 Exports Race data into JSON format String.
	 @return The JSON data.
	 */
	// if need to preserve Race.java object state
//	public String exportMe(){
//		Hashtable<String, Serializable> data = new Hashtable<>();
//		data.put("eventType", eventType);
//		data.put("canStart", canStart);
//		data.put("ongoing", ongoing);
//		data.put("ended", ended);
//		return ChronoTimer.export.objectToJsonString(data);
//	}	
	public abstract String exportMe();

	/**
	 Builds text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	public abstract String raceStats();
}