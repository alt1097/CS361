package race;

import channel.Channel;
import main.ChronoTimer;

import java.util.ArrayList;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */
public class RacePARIND extends Race{
	/**
	 Contains the Racers in the race according to which lane they're in.
	 */
	ArrayList<ArrayList<Racer>> lanes = new ArrayList<>();
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
	 Initializes the Parallel Individual components of Race.
	 @param timer Reference to the ChronoTimer.
	 */
	public RacePARIND(ChronoTimer timer){
		super(timer);
		//  TODO
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerPARIND(int number, boolean toFront){
		//  TODO
	}

	/**
	 Gets a Racer in a Parallel Individual Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Parallel Individual Race.
	 @return The Racer object.
	 */
	public Racer getRacerPARIND(int number, boolean byPlace){
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
	public boolean removeRacerPARIND(int number){
		return false;  //  TODO
	}

	/**
	 True if the Racer is able to be moved in the Parallel Individual Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedPARIND(Racer racer){
		return false;  //  TODO
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingPARIND(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstPARIND(Racer racer){
		return false;  //  TODO
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextPARIND(Racer racer){
		return false;  //  TODO
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Parallel Individual Race.
	 @return True if Race can start.
	 */
	public boolean canStartPARIND(){
		return false;  //  TODO
	}

	/**
	 Verifies that Channels are set up so that a Parallel Individual Race can proceed.
	 */
	public void channelVerifyPARIND(){
		//  TODO
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerPARIND(Channel channel){
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
	 Runs the actions to finalize a Parallel Individual Race.
	 */
	public void endPARIND(){
		//  TODO
	}
}