package race;

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
	 Indexes of the last Racers by lane.
	 */
	private ArrayList<Integer> lastIndexes = new ArrayList<>();

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
	 @return If the Racer does not exist.
	 */
	public boolean addRacerPARIND(int number){
		return false;  //  TODO
	}

	/**
	 Gets a Racer in a Parallel Individual Race.
	 @param number Number of the desired Racer.
	 @return The Racer object.
	 */
	public Racer getRacerPARIND(int number){
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
	public boolean removeRacerPARIND(int number){
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
	 Verifies that Channels are set up so that a Parallel Individual Race can proceed.
	 */
	public void channelVerifyPARIND(){
		//  TODO
	}

	/**
	 Runs the actions to finalize a Parallel Individual Race.
	 */
	public void endPARIND(){
		//  TODO
	}
}