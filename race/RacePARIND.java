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
public class RacePARIND extends Race{
	/**
	 Queue for racers before the race has begun.
	 */
	ArrayList<Racer> racers = new ArrayList<>();
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
	 */
	public RacePARIND(){
		super("PARIND");
		for(int i = 0; i < 4; i++){
			lanes.add(new ArrayList<>());
			firstIndexes.add(0);
			startChannels.add(null);
			finishChannels.add(null);
		}
		channelVerifyPARIND();
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerPARIND(int number, boolean toFront){
		//  TODO
		if (ongoing == false) {
			if (toFront)
				racers.add(0, new Racer(number));
			else
				racers.add(new Racer(number));
		}
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
		for (ArrayList l : lanes) {
			for (Racer r : (ArrayList<Racer>)l) {
				if (racer.equals(r))
					return true;
			}
		}
		return false;
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
		boolean pass = true;
		if (racers.size() == 0)
			pass = false;
		if (startChannels.size() < 2 || finishChannels.size() < 2)
			pass = false;
		canStart = pass;
		return pass;
	}

	/**
	 Verifies that Channels are set up so that a Parallel Individual Race can proceed.
	 */
	public void channelVerifyPARIND(){
		//  TODO
		boolean pass = false;
		int numLanes = 0;
		for (int i = 0; i < 8; i += 2) {
			Channel tempStart = ChronoTimer.getChannel(i);
			if (tempStart.isOn()) {
				Channel tempFinish = ChronoTimer.getChannel(i + 1);
				if (tempFinish.isOn()) {
					// Adds the lane at start channel number divided by two.
					lanes.add((i/2), new ArrayList<Racer>());
					tempStart.setChanType("START");
					startChannels.add(tempStart);
					tempFinish.setChanType("FINISH");
					finishChannels.add(tempFinish);
					numLanes ++;
					break;
				}
			}
		}
		if (numLanes >= 2)
			return;
		else {
			// If it doesn't pass as a valid PARIND race, clear start and finish channels.
			startChannels.clear();
			finishChannels.clear();
		}
	}

	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerPARIND(Channel channel){
		//  TODO
		String retMes = "";
		int laneNum = (channel.getName()+1)/2;
		if (startChannels.contains(channel)) {	
			if (racers.size() > 0) {
				Racer racer = racers.remove(0);
				lanes.get(laneNum).add(racer);
				channel.fireChannel(racer);
				channel.reset();
			}
		}
		else if (finishChannels.contains(channel)) {
			if (firstIndexes.get(laneNum) < lanes.get(laneNum).size()) {
				channel.fireChannel(lanes.get(laneNum).get(firstIndexes.get(laneNum)));
				channel.reset();
				// Increment first index
				firstIndexes.set(laneNum, (firstIndexes.get(laneNum) + 1));
			}
			else {
				retMes += " - NO RACER CURRENTLY RACING";
			}
		}
		else {
			retMes += " - CHANNEL IS NOT USED";
		}
		update();
		return retMes;
		
		
//		if (startChannels.contains(channel)) {
//			int laneNum = 0;
//			for (int i=0; i <= startChannels.size(); i++) {
//				if (channel == startChannels.get(i))
//					laneNum = i;
//			}
//			if (queueIndexes.get(laneNum) == lanes.get(laneNum).size())
//				retMes += " - NO RACER LEFT IN QUEUE";
//			else {
//				ongoing = true;
//				startChannels.get(laneNum).fireChannel(getRacerPARIND(queueIndexes.get(laneNum),true));
//				startChannels.get(laneNum).reset();
//				queueIndexes.set(laneNum, queueIndexes.get(laneNum) + 1);
//			}
//		}
//		else if (finishChannels.contains(channel)) {
//			int laneNum = 0;
//			for (int i=0; i <= finishChannels.size(); i++) {
//				if (channel == finishChannels.get(i))
//					laneNum = i;
//			}
//			if (queueIndexes.get(laneNum) == lanes.get(laneNum).size())
//				retMes += " - NO RACER LEFT IN QUEUE";
//			else {
//				ongoing = true;
//				finishChannels.get(laneNum).fireChannel(getRacerPARIND(queueIndexes.get(laneNum),true));
//				finishChannels.get(laneNum).reset();
//				queueIndexes.set(laneNum, queueIndexes.get(laneNum) + 1);
//			}
//		}
//		else {
//			retMes += " - CHANNEL IS NOT USED";
//		}
//		update();
//		return retMes;
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
		//  TODO
		// use end not endPARIND
		boolean end = false;
		for (int i=0; i < lanes.size(); i++) {
			if (firstIndexes.get(i) == lanes.get(i).size())
				end = true;
		}
		if (end == true)
			end();
	}

	/**
	 Runs the actions to finalize a Parallel Individual Race.
	 */
	public void endPARIND(){
		ChronoTimer.log.add(printPARIND());
	}
	
	/**
	 Prints the current status of all Racers in Parallel Individual Race.
	 @return The Racer status printout.
	 */
	public String printPARIND(){
		return "";//  TODO
	}

	/**
	 Exports Parallel Individual Race data into JSON format String.
	 @param data Hash table to add to.
	 */
	public void exportMePARIND(Hashtable<String, Serializable> data){
		data.put("racers", racers);
		data.put("lanes", lanes);
		data.put("firstIndexes", firstIndexes);
		for(int i = 0; i < 4; i++){
			if(startChannels.get(i) != null){
				data.put("startChannel_"+i, startChannels.get(i).getName());
			}
			if(finishChannels.get(i) != null){
				data.put("finishChannel_"+i, finishChannels.get(i).getName());
			}
		}
	}
}