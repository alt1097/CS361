package race;

import channel.Channel;
import main.ChronoTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
			firstIndexes.add(-1);
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
			for (Racer racer : racers) {
				if (racer != null && racer.getNumber() == number) {
					return racer;
				}
			}
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
		// Check if Racer is still in queue
		Iterator<Racer> racerIterator = racers.iterator();
		while (racerIterator.hasNext()) {
			Racer tmpRacer = racerIterator.next();
			if (tmpRacer != null && tmpRacer.getNumber() == number) {
				racers.remove(tmpRacer);
				return true;
			}
		}
		// Check if Racer has alread started race
		for (int l=0; l <= lanes.size(); l++) {
			ArrayList<Racer> lane = lanes.get(l);
			for (int r=0; r < lane.size(); r++) {
				Racer tmpRacer = lane.get(r);
				if (tmpRacer != null && tmpRacer.getNumber() == number) {
					lane.remove(tmpRacer);
					// Decrement first index tor that lane assuming a check has been made
					// to ensure the racer hasn't already finished
					firstIndexes.set(l, firstIndexes.get(l) - 1);
					return true;
				}
			}
		}
		return false;  //  TODO
	}

	/**
	 True if the Racer is able to be moved in the Parallel Individual Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedPARIND(Racer racer){
		if (racers.contains(racer))
			return true;
		else {
			for (int l=0; l < lanes.size(); l++) {
				ArrayList<Racer> tmpLane = lanes.get(l);
				if (tmpLane.contains(racer)){
					if (tmpLane.indexOf(racer) >= firstIndexes.get(l)) {
						return true;
					}
				}
			}
		}
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
		if (canBeMovedPARIND(racer)) {
			for (int l=0; l < lanes.size(); l++) {
				ArrayList<Racer> tmpLane = lanes.get(l);
				if (tmpLane.contains(racer)){
					// Remove from lane, and add back in at first index
					tmpLane.add(firstIndexes.get(l),tmpLane.remove(tmpLane.indexOf(racer)));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextPARIND(Racer racer){
		if (racers.contains(racer)) {
			racers.add(0,racers.remove(racers.indexOf(racer)));
			return true;
		}
		return false;
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
		for (int i=0; i < 8; i += 2) {
			Channel tempStart = ChronoTimer.getChannel(i);
			if (tempStart.isOn()) {
				if (!startChannels.contains(tempStart)) {
					tempStart.setChanType("START");
					startChannels.add(tempStart);
				}
			}
			Channel tempFinish = ChronoTimer.getChannel(i + 1);
			if (tempFinish.isOn()) {
				if (!finishChannels.contains(tempFinish)) {
					tempFinish.setChanType("FINISH");
					finishChannels.add(tempFinish);
				}
			}
			if (tempStart.isOn() && tempFinish.isOn()) {
				lanes.set((i/2), new ArrayList<Racer>());
			}
		}
	}

	/**
	 *
	 */
	private void syncLanes() {

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
				ChronoTimer.output(racer.getNumber()+" TRIG "+(channel.getName() + 1));
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
		return "EVERYTHING SUCKS";//  TODO
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