package race;

import channel.Channel;
import main.ChronoTimer;

import java.util.ArrayList;

/**
 -- ChronoTimer 1009 --
 Author:  The Joker
 Date:  2/28/2016 - 3:49 PM
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
	 Indexes of the last Racers by lane.
	 */
	private ArrayList<Integer> lastIndexes = new ArrayList<>();

	/**
	 Initializes the Parallel Groups components of Race.
	 @param timer Reference to the ChronoTimer.
	 */
	public RacePARGRP(ChronoTimer timer){
		super(timer);
		//  TODO
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerPARGRP(int number, boolean toFront){
		//  TODO
	}

	/**
	 Gets a Racer in a Parallel Groups Race.
	 @param number Number of the desired Racer.
	 @return The Racer object.
	 */
	public Racer getRacerPARGRP(int number){
		for(ArrayList<Racer> lane : lanes){
			for(Racer racer : lane){
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
	public boolean removeRacerPARGRP(int number){
		return false;  //  TODO
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingPARGRP(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstPARGRP(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextPARGRP(Racer racer){
		return false;  //  TODO
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 Verifies that Channels are set up so that a Parallel Group Race can proceed.
	 */
	public void channelVerifyPARGRP(){
		//  TODO
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Group Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerPARGRP(Channel channel){
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
}