package race;

import channel.Channel;
import main.ChronoTimer;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */
public class Race{
	/**
	 Reference to the ChonoTimer.
	 */
	private ChronoTimer timer;
	/**
	 Indicates if the race is currently in progress.
	 */
	private boolean ongoing = false;
	/**
	 Indicates if the race is currently over.
	 */
	private boolean ended = false;

	/**
	 Initializes the default Race requirements.
	 @param timer Reference to the ChronoTimer.
	 */
	public Race(ChronoTimer timer){
		this.timer = timer;
		//  TODO
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 */
	public void addRacer(int number){
		switch(timer.getEventType()){
			case "IND":
				((RaceIND) this).addRacerIND(number);
			case "PARIND":
				((RacePARIND) this).addRacerPARIND(number);
			case "GRP":
				((RaceGRP) this).addRacerGRP(number);
			case "PARGRP":
				((RacePARGRP) this).addRacerPARGRP(number);
			default:
				//  TODO?
		}
	}

	/**
	 Gets the desired Racer from number.
	 @param number Number of the Racer.
	 @return The Racer object.
	 */
	public Racer getRacer(int number){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).getRacerIND(number);
			case "PARIND":
				return ((RacePARIND) this).getRacerPARIND(number);
			case "GRP":
				return ((RaceGRP) this).getRacerGRP(number);
			case "PARGRP":
				return ((RacePARGRP) this).getRacerPARGRP(number);
			default:
				return null;
		}
	}

	/**
	 If the Racer exists, then remove them from the Race.
	 @param number Number of the Racer to remove.
	 @return If the Racer exists.
	 */
	public boolean removeRacer(int number){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).removeRacerIND(number);
			case "PARIND":
				return ((RacePARIND) this).removeRacerPARIND(number);
			case "GRP":
				return ((RaceGRP) this).removeRacerGRP(number);
			case "PARGRP":
				return ((RacePARGRP) this).removeRacerPARGRP(number);
			default:
				return false;
		}
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacing(Racer racer){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).isRacingIND(racer);
			case "PARIND":
				return ((RacePARIND) this).isRacingPARIND(racer);
			case "GRP":
				return ((RaceGRP) this).isRacingGRP(racer);
			case "PARGRP":
				return ((RacePARGRP) this).isRacingPARGRP(racer);
			default:
				return false;
		}
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirst(Racer racer){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).moveToFirstIND(racer);
			case "PARIND":
				return ((RacePARIND) this).moveToFirstPARIND(racer);
			case "GRP":
				return ((RaceGRP) this).moveToFirstGRP(racer);
			case "PARGRP":
				return ((RacePARGRP) this).moveToFirstPARGRP(racer);
			default:
				return false;
		}
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNext(Racer racer){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).moveToNextIND(racer);
			case "PARIND":
				return ((RacePARIND) this).moveToNextPARIND(racer);
			case "GRP":
				return ((RaceGRP) this).moveToNextGRP(racer);
			case "PARGRP":
				return ((RacePARGRP) this).moveToNextPARGRP(racer);
			default:
				return false;
		}
	}

	//  ----------  EVENT MANAGEMENT  ----------

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
	 Verifies that Channels are set up so that a Race can proceed.
	 */
	public void channelVerify(){
		switch(timer.getEventType()){
			case "IND":
				((RaceIND) this).channelVerifyIND();
			case "PARIND":
				((RacePARIND) this).channelVerifyPARIND();
			case "GRP":
				((RaceGRP) this).channelVerifyGRP();
			case "PARGRP":
				((RacePARGRP) this).channelVerifyPARGRP();
			default:
				//  TODO?
		}
	}

	/**
	 Triggers the Channel specified.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String trigger(Channel channel){
		switch(timer.getEventType()){
			case "IND":
				return ((RaceIND) this).triggerIND(channel);
			case "PARIND":
				return ((RacePARIND) this).triggerPARIND(channel);
			case "GRP":
				return ((RaceGRP) this).triggerGRP(channel);
			case "PARGRP":
				return ((RacePARGRP) this).triggerPARGRP(channel);
			default:
				//  TODO?
		}
		return " - EVENT TYPE NOT FOUND";
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
		//  TODO
	}

	/**
	 Runs the actions to finalize a Race.
	 */
	public void end(){
		ongoing = false;
		ended = true;
		switch(timer.getEventType()){
			case "IND":
				((RaceIND) this).endIND();
			case "PARIND":
				((RacePARIND) this).endPARIND();
			case "GRP":
				((RaceGRP) this).endGRP();
			case "PARGRP":
				((RacePARGRP) this).endPARGRP();
			default:
				//  TODO?
		}
	}
}