package race;

import channel.Channel;
import main.ChronoTimer;

import java.util.ArrayList;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */
public class RaceGRP extends Race{
	/**
	 Contains the Racer in the race.
	 */
	private ArrayList<Racer> racers = new ArrayList<>();
	/**
	 Index of the first Racer.
	 */
	private int firstIndex = 0;
	/**
	 Index of the first not racing.
	 */
	private int queueIndex = 0;
	/**
	 Reference to the assigned start Channel.
	 */
	private Channel startChannel;
	/**
	 Reference to the assigned finish Channel.
	 */
	private Channel finishChannel;

	/**
	 Initializes the Group components of Race.
	 @param timer Reference to the ChronoTimer.
	 */
	public RaceGRP(ChronoTimer timer){
		super(timer);
		//  TODO
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerGRP(int number, boolean toFront){
		if(toFront){
			racers.add(0, new Racer(number));
		}
		else{
			racers.add(new Racer(number));
		}
	}

	/**
	 Gets a Racer in a Group Race.
	 @param number Number of the desired Racer.
	 @return The Racer object.
	 */
	public Racer getRacerGRP(int number){
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
	public boolean removeRacerGRP(int number){
		return racers.remove(getRacerGRP(number));
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingGRP(Racer racer){
		int index = racers.indexOf(racer);
		return !(index >= queueIndex || index < firstIndex);
	}

	/**
	 True if the Racer is able to be moved in the Group Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedGRP(Racer racer){
		return racers.indexOf(racer) <= firstIndex;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstGRP(Racer racer){
		if(queueIndex - firstIndex > 1){
			racers.remove(racer);
			racers.add(firstIndex, racer);
			return true;
		}
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextGRP(Racer racer){
		if(queueIndex < racers.size() - 1){
			racers.remove(racer);
			racers.add(lastIndex, racer);
			return true;
		}
		return false;
	}

	/**
	 The Racer in first will be marked to not finish.
	 */
	public void dnf(){
		firstIndex++;
		update();
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Group Race.
	 @return True if Race can start.
	 */
	public boolean canStartGRP(){
		return false;  //  TODO
	}

	/**
	 Verifies that Channels are set up so that a Group Race can proceed.
	 */
	public void channelVerifyGRP(){
		//  TODO
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Group Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerGRP(Channel channel){
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
	 Runs the actions to finalize a Group Race.
	 */
	public void endGRP(){
		//  TODO
	}
}