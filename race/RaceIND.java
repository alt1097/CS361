package race;

import channel.Channel;
import main.ChronoTimer;
import main.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class RaceIND extends Race{
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
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerIND(int number, boolean toFront){
		if(toFront){
			racers.add(0, new Racer(number));
		}
		else{
			racers.add(new Racer(number));
		}
	}

	/**
	 Gets a Racer in an Individual Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Individual Race.
	 @return The Racer object.
	 */
	public Racer getRacerIND(int number, boolean byPlace){
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
	public boolean removeRacerIND(int number){
		return racers.remove(getRacerIND(number, false));
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingIND(Racer racer){
		int index = racers.indexOf(racer);
		return !(index >= queueIndex || index < firstIndex);
	}

	/**
	 True if the Racer is able to be moved in the Individual Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedIND(Racer racer){
		return racers.indexOf(racer) > firstIndex;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstIND(Racer racer){
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
	public boolean moveToNextIND(Racer racer){
		if(queueIndex < racers.size() - 1){
			racers.remove(racer);
			racers.add(queueIndex, racer);
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
		update();
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Individual Race.
	 @return True if Race can start.
	 */
	public boolean canStartIND(){
		boolean pass = true;
		if(racers.size() == 0){
			pass = false;
		}
		if(startChannel == null || finishChannel == null){
			pass = false;
		}
		canStart = pass;
		return pass;
	}

	/**
	 Verifies that Channels are set up so that an Individual Race can proceed.
	 */
	public void channelVerifyIND(){
		boolean fail = true;
		for(int i = 0; i < 8; i += 2){
			Channel tempStart = timer.getChannel(i);
			if(tempStart != null && tempStart.isOn()){
				Channel tempFinish = timer.getChannel(i + 1);
				if(tempFinish != null && tempFinish.isOn()){
					tempStart.setChanType("START");
					startChannel = tempStart;
					tempFinish.setChanType("FINISH");
					finishChannel = tempFinish;
					fail = false;
					break;
				}
			}
		}
		if(fail){
			startChannel = null;
			finishChannel = null;
		}
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerIND(Channel channel){
		String retMes = "";
		if(channel == startChannel){
			if(queueIndex == racers.size()){
				retMes += " - NO RACER LEFT IN QUEUE";
			}
			else{
				ongoing = true;
				startChannel.fireChannel(getRacerIND(queueIndex, true));
				startChannel.reset();
				queueIndex++;
			}
		}
		else if(channel == finishChannel){
			if(firstIndex == queueIndex){
				retMes += " - NO RACER CURRENTLY RACING";
			}
			else{
				ongoing = true;
				finishChannel.fireChannel(getRacerIND(firstIndex, true));
				finishChannel.reset();
				firstIndex++;
			}
		}
		else{
			retMes += " - CHANNEL IS NOT USED";
		}
		update();
		return retMes;
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
		if(firstIndex == racers.size()){
			end();
		}
	}

	/**
	 Runs the actions to finalize an Individual Race.
	 */
	public void endIND(){
		ChronoTimer.log.add(print());
	}

	/**
	 Prints the current status of all Racers in Individual Race.
	 @return The Racer status printout.
	 */
	public String printIND(){
		String sep = "--------------------";
		String record = "";
		record += sep+"\n";
		record += ": : Run #"+ChronoTimer.log.getLogNumber()+" : : ";
		if(ended()){
			record += "Ended";
		}
		else if(ongoing()){
			record += "Ongoing";
		}
		else{
			record += "Not Started";
		}
		record += " : :\n";
		for(Racer racer : racers){
			record += "#"+racer.getNumber()+"\tStart: ";
			boolean printDif = true;
			Date tempStartTime = racer.getStartTime();
			if(tempStartTime == null){
				record += "DID NOT START";
				printDif = false;
			}
			else{
				record += ChronoTimer.format.format(tempStartTime);
			}
			record += "\t\tFinish: ";
			Date tempEndTime = racer.getEndTime();
			if(tempEndTime == null){
				record += "DID NOT FINISH";
				printDif = false;
			}
			else{
				record += ChronoTimer.format.format(tempEndTime);
			}
			record += "\t\tFinal: ";
			if(printDif){
				record += diffFormat.format(racer.getFinalTime(timer));
			}
			else{
				record += "DNF";
			}
			record += "\n";
		}
		record += sep;
		return record;
	}
}