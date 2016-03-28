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
	 Queue for racers waiting to start.
	 */
	ArrayList<Racer> queue = new ArrayList<>();
	/**
	 List of lanes.
	 */
	ArrayList<Lane> lanes = new ArrayList<Lane>();
	/**
	 Initializes the Parallel Individual components of Race.
	 */
	public RacePARIND(){
		// TODO
		super("PARIND");
		for (int i=0; i < 4; i++) {
			lanes.add(new Lane(i));
		}
		channelVerifyPARIND();
	}

	private class Lane {
		ArrayList<Racer> racers;
		int firstIndex;
		int laneNum;
		Channel startChannel;
		Channel finishChannel;

		public Lane(int laneNum) {
			this.laneNum = laneNum;
			this.startChannel = null;
			this.finishChannel = null;
			this.firstIndex = 0;
			racers = new ArrayList<Racer>();
		}
		
		public ArrayList<Racer> getRacers() {
			return racers;
		}
		

		public Racer getRacer(int number) {
			for (Racer racer : racers) {
				if (racer != null && racer.getNumber() == number) {
					return racer;
				}
			}
			return null;
		}

		public boolean isValid() {
			if (startChannel != null && finishChannel != null)
				return true;
			return false;
		}
	}
	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerPARIND(int number, boolean toFront){
		if (toFront) {
			queue.add(0, new Racer(number));
		}
		else {
			queue.add(new Racer(number));
		}
	}

	/**
	 Gets a Racer in a Parallel Individual Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Parallel Individual Race.
	 @return The Racer object.
	 */
	public Racer getRacerPARIND(int number, boolean byPlace){
		if (byPlace) {
			// TODO I don't understand this in the context of PARIND races.
			return null;
		}
		else {
			// Cycle through queue to see if racer with that number is there
			for (int i=0; i < queue.size(); i++) {
				Racer tmpRacer = queue.get(i);
				if (tmpRacer.getNumber() == number) {
					return tmpRacer;
				}
			}
			// Cycle through racers in each lane to find racer with specified number
			for (Lane lane : lanes) {
				if (lane != null) {
					Racer tmpRacer = lane.getRacer(number);
					if (tmpRacer != null) {
						return tmpRacer;
					}
				}
			}
		}
		return null;
	}

	/**
	 If the Racer exists, then remove them from the Race.
	 Racers can only be removed if they haven't started the race.
	 @param number Number of the Racer to remove.
	 @return If the Racer exists.
	 */
	public boolean removeRacerPARIND(int number){
		for (Racer racer : queue) {
			if (racer.getNumber() == number) {
				queue.remove(racer);
				return true;
			}
		}
		return false;
	}

	private Lane getLane(Racer racer) {
		for (Lane lane : lanes) {
			if (lane != null) {
				for (Racer r : lane.racers) {
					if (r != null && r.equals(racer)) {
						return lane;
					}
				}
			}
		}
		return null;
	}
	/**
	 True if the Racer is able to be moved in the Parallel Individual Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedPARIND(Racer racer){
		if (queue.contains(racer))
			return true;
		for (Lane lane : lanes) {
			if (lane != null) {
				int racerIndex = lane.racers.indexOf(racer);
				if (racerIndex != -1) {
					if (racerIndex > lane.firstIndex)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingPARIND(Racer racer){
		// If the racer is still in the queue, they are not currently racing.
		if (queue.contains(racer))
			return false;
		else {
			for (Lane lane : lanes) {
				if (lane.racers.indexOf(racer) >= lane.firstIndex) {
					return true;
				}
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
		Lane lane = getLane(racer);
		if (lane != null) {
			// Make sure racer is not already in first or finished
			if (lane.racers.indexOf(racer) > lane.firstIndex) {
				lane.racers.remove(racer);
				lane.racers.add(lane.firstIndex, racer);
				return true;
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
		int racerIndex = queue.indexOf(racer);
		if (racerIndex > 0) {
			queue.remove(racer);
			queue.add(0, racer);
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
		boolean startable = true;
		// Must be racers in the queue
		if (queue.size() == 0) {
			startable = false;
		}
		// Must be at least two working lanes
		int validLanes = 0;
		for (Lane lane : lanes) {
			if (lane.isValid())
				++validLanes;
		}
		if (validLanes < 2) {
			startable = false;
		}
		if (startable) {
			canStart = true;
			return true;
		}
		else {
			canStart = false;
			return false;
		}
	}

	/**
	 Verifies that Channels are set up so that a Parallel Individual Race can proceed.
	 */
	public void channelVerifyPARIND(){
		for(int i = 0; i < 8; i += 2){
			// Check start channel
			Channel tempStart = ChronoTimer.getChannel(i);
			int laneNum = i/2;
			if (tempStart != null && tempStart.isOn()) {
				tempStart.setChanType("START");
				lanes.get(laneNum).startChannel = tempStart;
			}
			// If channel is off, remove it from startChannel for that lane
			else {
				lanes.get(laneNum).startChannel = null;
			}
			// Check finish channel
			Channel tempFinish = ChronoTimer.getChannel(i+1);
			if (tempFinish != null && tempFinish.isOn()) {
				tempFinish.setChanType("FINISH");
				lanes.get(laneNum).finishChannel = tempFinish;
			}
			// If channel is off, remove it from finishChannel for that lane
			else {
				lanes.get(laneNum).finishChannel = null;
			}
		}
	}


	/**
	 Verifies Channel's use and triggers the Channel specified for Parallel Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	public String triggerPARIND(Channel channel){
		boolean usedChannel = false;
		String retMes = "";
		for (Lane lane : lanes) {
			if (channel.equals(lane.startChannel)) {
				usedChannel = true;
				if (queue.size() <= 0) {
					retMes += " - NO RACER LEFT IN QUEUE";
				}
				else {
					ongoing = true;
					Racer racer = queue.remove(0);
					lane.racers.add(racer);
					lane.startChannel.fireChannel(racer);
					lane.startChannel.reset();
					ChronoTimer.output(racer.getNumber()+" TRIG "+(lane.startChannel.getName() + 1));
				}
			}
			else if (channel.equals(lane.finishChannel)) {
				usedChannel = true;
				if (lane.firstIndex >= lane.racers.size()) {
					retMes += " - NO RACER CURRENTLY RACING";
				}
				else {
					Racer racer = lane.racers.get(lane.firstIndex);
					lane.finishChannel.fireChannel(racer);
					lane.finishChannel.reset();
					++lane.firstIndex;
					ChronoTimer.output(racer.getNumber()+" TRIG "+(lane.finishChannel.getName() + 1));
					ChronoTimer.output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
				}
			}
		}
		if (!usedChannel)
			retMes += " - CHANNEL IS NOT USED";
		update();
		return retMes; //  TODO
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
		boolean end = true;
		if (queue.size() > 0)
			end = false;
		for (Lane lane : lanes) {
			if (lane.firstIndex != lane.racers.size()) {
				end = false;
			}
		}
		if (end)
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
		String sep = "--------------------";
		String record = "";
		record += sep+"\n";
		record += ": : Run #"+ChronoTimer.log.getLogNumber()+" : : "+eventType+" : : ";
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

		for (Lane lane : lanes) {
			if (lane.racers.size() >0) {
				record += "\t~~~Lane " + (lane.laneNum + 1) + "~~~\n";
				for (Racer racer : lane.racers) {
					record += "#" + racer.getNumber() + "\tStart: ";
					boolean printDif = true;
					Date tempTime = racer.getStartTime();
					if (tempTime == null) {
						record += "DID NOT START";
						printDif = false;
					} else {
						record += ChronoTimer.format.format(tempTime);
					}
					record += "\t\tFinish: ";
					tempTime = racer.getEndTime();
					if (tempTime == null) {
						record += "DID NOT FINISH";
						printDif = false;
					} else {
						record += ChronoTimer.format.format(tempTime);
					}
					record += "\t\tFinal: ";
					if (printDif) {
						record += ChronoTimer.diffFormat.format(racer.getFinalTime());
					} else {
						record += "DNF";
					}
					record += "\n";
				}
			}
		}
		record += "\t~~~Queued~~~\n";
		for (Racer racer : queue) {
			record += "#"+racer.getNumber()+ "\n";
		}
		record += sep;
		return record;
	}

	/**
	 Exports Parallel Individual Race data into JSON format String.
	 @param data Hash table to add to.
	 */
	public void exportMePARIND(Hashtable<String, Serializable> data) {
		data.put("queue", queue);
		Hashtable<String, Serializable> laneHash = new Hashtable<>();
		for (int i = 0; i < 4; i++) {
			Lane lane = lanes.get(i);
			Hashtable<String, Serializable> singleLaneHash = new Hashtable<>();
			if (lane.startChannel != null)
				singleLaneHash.put("startChannel", lane.startChannel.getName());
			if (lane.finishChannel != null)
				singleLaneHash.put("finishChannel", lane.finishChannel.getName());
			singleLaneHash.put("firstIndex", lane.firstIndex);
			singleLaneHash.put("racers", lane.getRacers());
			laneHash.put("lane_" + i, singleLaneHash);
		}
		data.put("lanes", laneHash);
	}
}