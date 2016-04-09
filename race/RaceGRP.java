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
	public RaceGRP(){
		super("GRP");
		channelVerifyGRP();
	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 */
	public void addRacerGRP(int number, boolean toFront){
		if(trackNumber < racers.size()){
			racers.get(trackNumber).setNumber(number);
			trackNumber++;
		}
	}

	/**
	 Gets a Racer in a Group Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Group Race.
	 @return The Racer object.
	 */
	public Racer getRacerGRP(int number, boolean byPlace){
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
	public boolean removeRacerGRP(int number){
		return false;
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	public boolean isRacingGRP(Racer racer){
		return false;
	}

	/**
	 True if the Racer is able to be moved in the Group Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	public boolean canBeMovedGRP(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToFirstGRP(Racer racer){
		return false;
	}

	/**
	 Moves the Racer to the next position to start in Race.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	public boolean moveToNextGRP(Racer racer){
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
	public boolean canStartGRP(){
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
	public void channelVerifyGRP(){
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
	public String triggerGRP(Channel channel){
		String retMes = "";
		if(channel == startChannel){
			if(ongoing){
				retMes += " - START TIME ALREADY ESTABLISHED";
			}
			else{
				ongoing = true;
				startTime = ChronoTimer.getTime();
				ChronoTimer.output("TRIG "+(startChannel.getName() + 1));
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
				ChronoTimer.output(racer.getNumber()+" TRIG "+(finishChannel.getName() + 1));
				ChronoTimer.output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
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
	 Runs the actions to finalize a Group Race.
	 */
	public void endGRP(){
		ChronoTimer.log.add(printGRP());
	}
	
	/**
	 Prints the current status of all Racers in Group Race.
	 @return The Racer status printout.
	 */
	public String printGRP(){
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
	 @param data Hash table to add to.
	 */
//	public void exportMeGRP(){

//	}

	@Override
	public String exportMe() {
		Hashtable<String, Serializable> data = new Hashtable<>();
		data.put("eventType", super.eventType);
		data.put("canStart", super.canStart);
		data.put("ongoing", super.ongoing);
		data.put("ended", super.ended);
		data.put("racers", racers);
		data.put("placeNumber", placeNumber);
		data.put("trackNumber", trackNumber);
		if(startChannel != null){
			data.put("startChannel", startChannel.getName());
		}
		if(finishChannel != null){
			data.put("finishChannel", finishChannel.getName());
		}		
		return ChronoTimer.export.objectToJsonString(data);
	}

	@Override
	public String print() {
		// TODO Merge or replace with printGRP
		return printGRP();
	}

	@Override
	public String trigger(Channel channel) {
		// TODO Merge or replace with triggerGRP
		return triggerGRP(channel);
	}

	@Override
	public void channelVerify() {
		// TODO Merge/replace with channelVerifyGRP
		channelVerifyGRP();
		
	}

	@Override
	public boolean moveToNext(Racer racer) {
		// TODO Auto-generated method stub
		return moveToNextGRP(racer);
	}

	@Override
	public boolean moveToFirst(Racer racer) {
		// TODO Auto-generated method stub
		return moveToFirstGRP(racer);
	}

	@Override
	public void addRacer(int number, boolean toFront) {
		// TODO Auto-generated method stub
		addRacerGRP(number, toFront);
	}

	@Override
	public Racer getRacer(int number, boolean byPlace) {
		// TODO Auto-generated method stub
		return getRacerGRP(number, byPlace);
	}

	@Override
	public boolean removeRacer(int number) {
		// TODO Auto-generated method stub
		return removeRacerGRP(number);
	}

	@Override
	public boolean isRacing(Racer racer) {
		// TODO Auto-generated method stub
		return isRacingGRP(racer);
	}

	@Override
	public boolean canBeMoved(Racer racer) {
		// TODO Auto-generated method stub
		return moveToFirstGRP(racer);
	}

	@Override
	public boolean canStart() {
		// TODO Auto-generated method stub
		return canStart || canStartGRP();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		ongoing = false;
		ended = true;
		endGRP();
		ChronoTimer.log.addToExport(exportMe());
	}

	/**
	 Builds Group Race text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	public String raceStats(){
		String output = "";
		if(startTime == null){
			output += "- RACE NOT STARTED -";
		}
		else{
			output += "ELAPSED: "+(ChronoTimer.diffFormat.format(ChronoTimer.getTime().getTime() - startTime.getTime()));
		}
		int racersSize = racers.size();
		if(racersSize > 0){
			Racer lastRacer = racers.get(racersSize - 1);
			output += "\n\n"+lastRacer.getNumber()+"\t"+lastRacer.getFinalTime()+" F";
		}
		return output;
	}
}