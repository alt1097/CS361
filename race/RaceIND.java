package race;

import channel.Channel;
import main.ChronoTimer;

import java.util.ArrayList;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */
public class RaceIND extends Race{
	/**
	 Reference to the ChonoTimer.
	 */
	private ChronoTimer timer;
	/**
	 Contains the Racer in the race.
	 */
	private ArrayList<Racer> racers = new ArrayList<>();
	/**
	 Index of the first Racer.
	 */
	private int firstIndex = 0;
	/**
	 Index of the last Racer.
	 */
	private int lastIndex = 0;

	/**
	 Initializes the Individual components of Race.
	 @param timer Reference to the ChronoTimer.
	 */
	public RaceIND(ChronoTimer timer){
		super(timer);
		//  TODO
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 */
	public void addRacerIND(int number){
		racers.add(new Racer(number));
	}

	/**
	 Gets a Racer in an Individual Race.
	 @param number Number of the desired Racer.
	 @return The Racer object.
	 */
	public Racer getRacerIND(int number){
		for(Racer racer : racers){
			if(racer.getNumber() == number){
				return racer;
			}
		}
		return null;
	}

	/**
	 If the Racer exists, then remove them from the Race.
	 @param number Number of the Racer to remove.
	 @return If the Racer exists.
	 */
	public boolean removeRacerIND(int number){
		return racers.remove(getRacerIND(number));
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingIND(Racer racer){
		int index = racers.indexOf(racer);
		return !(index > lastIndex || index < firstIndex);
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstIND(Racer racer){
		if(lastIndex - firstIndex > 0){
			racers.remove(racer);
			racers.add(0, racer);
			return true;
		}
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextIND(Racer racer){
		if(lastIndex < racers.size() - 2){
			racers.remove(racer);
			racers.add(lastIndex + 1, racer);
			return true;
		}
		return false;
	}

	/**
	 Moves the Racer in second to the first position.
	 @return True if Racer could be moved.
	 */
	public boolean swap(){
		Racer racer = racers.get(1);
		return racer != null && moveToFirstIND(racer);
	}

	/**
	 The Racer in first will be marked to not finish.
	 */
	public void dnf(){
		firstIndex++;
		//  TODO
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 Verifies that Channels are set up so that an Individual Race can proceed.
	 */
	public void channelVerifyIND(){
		//  TODO
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerIND(Channel channel){
		return "";  //  TODO
	}

	/**
	 Runs the actions to finalize an Individual Race.
	 */
	public void endIND(){
		//  TODO
	}
}