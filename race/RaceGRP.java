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
public class RaceGRP extends Race{
	/**
	 Contains the Racer in the race.
	 */
	private ArrayList<Racer> racers = new ArrayList<>();
	/**
	 Current place number of Racer to finish.
	 */
	private int placeNumber = 1;
	/**
	 Index of Racer who has not been assigned number.
	 */
	private int trackNumber = 0;
	/**
	 Start time to use for all Racers.
	 */
	private Date startTime;
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
	 */
	public RaceGRP(ChronoTimer chrono){
		super("GRP", chrono);
		channelVerify();
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 Changes the number of the next placeholder Racer number.
	 @param number Number of the Racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 @return String of any messages.
	 */
	@Override
	public String addRacer(int number, boolean toFront){
		String logOut = "";
		if(trackNumber < racers.size()){
			racers.get(trackNumber).setNumber(number);
			trackNumber++;
		}
		else{
			logOut += " - NO RACER IS LEFT TO RE-NUMBER";
		}
		return logOut;
	}

	/**
	 Gets a Racer in a Group Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Group Race.
	 @return The Racer object.
	 */
	@Override
	public Racer getRacer(int number, boolean byPlace){
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
	@Override
	public boolean removeRacer(int number){
		return false;
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	@Override
	public boolean isRacing(Racer racer){
		return false;
	}

	/**
	 True if the Racer is able to be moved in the Group Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	@Override
	public boolean canBeMoved(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToFirst(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToNext(Racer racer){
		return false;
	}

	/**
	 The Racer in first will be marked to not finish.
	 */
	public void dnf(){
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Group Race.
	 @return True if Race can start.
	 */
	@Override
	public boolean canStart(){
		if(canStart){
			return true;
		}
		boolean pass = true;
		if(startChannel == null || finishChannel == null){
			pass = false;
		}
		canStart = pass;
		return pass;
	}

	/**
	 Verifies that Channels are set up so that a Group Race can proceed.
	 */
	@Override
	public void channelVerify(){
		boolean fail = true;
		for(int i = 0; i < 8; i += 2){
			Channel tempStart = ChronoTimer.getChannel(i);
			if(tempStart != null && tempStart.isOn()){
				Channel tempFinish = ChronoTimer.getChannel(i + 1);
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
	 Verifies Channel's use and triggers the Channel specified for Group Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	@Override
	public String trigger(Channel channel){
		String retMes = "";
		if(channel == startChannel){
			if(ongoing){
				retMes += " - START TIME ALREADY ESTABLISHED";
			}
			else{
				ongoing = true;
				startTime = ChronoTimer.getTime();
				getChrono().output("TRIG "+(startChannel.getName() + 1));
			}
		}
		else if(channel == finishChannel){
			if(ongoing){
				Racer racer = new Racer(placeNumber);
				racer.setStartTime(startTime);
				finishChannel.fireChannel(racer);
				finishChannel.reset();
				placeNumber++;
				racers.add(racer);
				getChrono().output(racer.getNumber()+" TRIG "+(finishChannel.getName() + 1));
				getChrono().output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
			}
			else{
				retMes += " - START HAS NOT BEEN TRIGGERED";
			}
		}
		else{
			retMes += " - CHANNEL IS NOT USED";
		}
		return retMes;
	}

	/**
	 Runs various checks every time a trigger occurs.
	 */
	private void update(){
	}

	/**
	 Prints the current status of all Racers in Group Race.
	 @return The Racer status printout.
	 */
	@Override
	public String print(){
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
				record += ChronoTimer.diffFormat.format(racer.getFinalTime());
			}
			else{
				record += "DNF";
			}
			record += "\n";
		}
		record += sep;
		return record;
	}

	/**
	 Exports Group Race data into JSON format String.
	 */
	@Override
	public String exportMe() {
		Hashtable<String, Serializable> data = new Hashtable<>();
//		data.put("eventType", super.eventType);
//		data.put("canStart", super.canStart);
//		data.put("ongoing", super.ongoing);
//		data.put("ended", super.ended);
		data.put("racers", racers);
//		data.put("placeNumber", placeNumber);
//		data.put("trackNumber", trackNumber);
//		if(startChannel != null){
//			data.put("startChannel", startChannel.getName());
//		}
//		if(finishChannel != null){
//			data.put("finishChannel", finishChannel.getName());
//		}		
		return ChronoTimer.export.objectToJsonString(data);
	}
	
	/**
	 Runs the actions to finalize a Group Race.
	 */
	@Override
	public void end() {
		ongoing = false;
		ended = true;
		endedDisplay = raceStats();
		ChronoTimer.log.add(print());
		ChronoTimer.log.addToExport(exportMe());
	}

	/**
	 Builds Group Race text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	public String raceStats(){
		if(endedDisplay == null){
			String output = "";
			if(startTime == null){
				output += "- RACE NOT STARTED -";
			}else{
				output += "ELAPSED: "+(ChronoTimer.diffFormat.format(ChronoTimer.getTime().getTime() - startTime.getTime()));
			}
			int racersSize = racers.size();
			if(racersSize > 0){
				Racer lastRacer = racers.get(racersSize - 1);
				output += "\n\n"+lastRacer.getNumber()+"\t"+lastRacer.getFinalTime()+" F";
			}
			return output;
		}
		return endedDisplay;
	}
}