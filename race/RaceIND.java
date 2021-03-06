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
	 */
	public RaceIND(ChronoTimer chrono){
		super("IND", chrono);
		channelVerify();
	}

	// this is example of how to use export to save object variables as JSON
//	Export export = new Export();
//
//	public void exportMe() {
//		// use this format to put desired items in table:
//		// "variable name":variable itself
//		// keep in mind: "variable":variable not a same as "variable":"variable"
//	    // table.put("key", value);
//	    Hashtable<String, Serializable> table = new Hashtable<String, Serializable>();
//	    table.put("racers", racers);
//	    table.put("firstIndex", firstIndex);
//	    table.put("queueIndex", queueIndex);
//	    table.put("startChannel", startChannel.getName());
//	    table.put("finishChannel", finishChannel.getName());
//		//export.objectToJsonFile(table); // this is for export in file
//	    //return export.objectToJsonString(table); // this is for export in string
//	}

	//  ----------  RACER MANAGEMENT  ----------

	/**
	 If the racer doesn't exist, then add them to the race.
	 @param number Number of the racer to add.
	 @param toFront True if Racer should be added to the front of lane.
	 @return String of any messages.
	 */
	@Override
	public String addRacer(int number, boolean toFront){
		if(toFront){
			racers.add(0, new Racer(number));
		}
		else{
			racers.add(new Racer(number));
		}
		return "";
	}

	/**
	 Gets a Racer in an Individual Race.
	 @param number Number of the desired Racer.
	 @param byPlace True to get a Racer based on position in Individual Race.
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
		return racers.remove(getRacer(number, false));
	}

	/**
	 True if the Racer is currently racing in the Race.
	 @param racer Racer Object to check if racing.
	 @return True if the Racer is racing.
	 */
	@Override
	public boolean isRacing(Racer racer){
		int index = racers.indexOf(racer);
		return !(index >= queueIndex || index < firstIndex);
	}

	/**
	 True if the Racer is able to be moved in the Individual Race.
	 @param racer The Racer to check.
	 @return True if Racer can be moved.
	 */
	@Override
	public boolean canBeMoved(Racer racer){
		return racers.indexOf(racer) > firstIndex;
	}

	/**
	 Moves the Racer to the first position in their lane.
	 @param racer Racer to move.
	 @return True if Racer could be moved.
	 */
	@Override
	public boolean moveToFirst(Racer racer){
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
	@Override
	public boolean moveToNext(Racer racer){
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
		if(firstIndex + 1 < racers.size()){
			Racer racer = racers.get(firstIndex + 1);
			return racer != null && moveToFirst(racer);
		}
		return false;
	}

	/**
	 The Racer in first will be marked to not finish.
	 */
	public void dnf(){
		getChrono().output(getRacer(firstIndex, true).getNumber()+" DNF");
		firstIndex++;
		update();
	}

	//  ----------  EVENT MANAGEMENT  ----------

	/**
	 True if the Race is able to listen to triggers for Individual Race.
	 @return True if Race can start.
	 */
	@Override
	public boolean canStart(){
		if(canStart){
			return true;
		}
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
	 Verifies Channel's use and triggers the Channel specified for Individual Race.
	 @param channel Channel Object.
	 @return String of any messages.
	 */
	@Override
	public String trigger(Channel channel){
		String retMes = "";
		if(channel == startChannel){
			if(queueIndex == racers.size()){
				retMes += " - NO RACER LEFT IN QUEUE";
			}
			else{
				ongoing = true;
				Racer racer = getRacer(queueIndex, true);
				startChannel.fireChannel(racer);
				startChannel.reset();
				queueIndex++;
				getChrono().output(racer.getNumber()+" TRIG "+(startChannel.getName() + 1));
			}
		}
		else if(channel == finishChannel){
			if(firstIndex == queueIndex){
				retMes += " - NO RACER CURRENTLY RACING";
			}
			else{
				Racer racer = getRacer(firstIndex, true);
				finishChannel.fireChannel(racer);
				finishChannel.reset();
				firstIndex++;
				getChrono().output(racer.getNumber()+" TRIG "+(finishChannel.getName() + 1));
				getChrono().output(racer.getNumber()+" ELAPSED "+ChronoTimer.diffFormat.format(racer.getFinalTime()));
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
	 Prints the current status of all Racers in Individual Race.
	 @return The Racer status printout.
	 */
	@Override
	public String print(){
		String sep = "--------------------";
		String record = "";
		record += sep+"\n";
		record += ": Run #"+ChronoTimer.log.getLogNumber()+" : "+eventType+" : ";
		if(ended()){
			record += "Ended";
		}
		else if(ongoing()){
			record += "Ongoing";
		}
		else{
			record += "Not Started";
		}
		record += " :\n";
		for(Racer racer : racers){
			record += "#"+racer.getNumber()+"\tFinal: ";
			Long diff = racer.getFinalTime();
			if(diff != null){
				record += ChronoTimer.diffFormat.format(diff);
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
	 Exports Individual Race data into JSON format String.
	 */
	@Override
	public String exportMe(){
		Hashtable<String, Serializable> data = new Hashtable<>();
		data.put("eventType", super.eventType);
		data.put("canStart", super.canStart);
		data.put("ongoing", super.ongoing);
		data.put("ended", super.ended);
		data.put("racers", racers);
		data.put("firstIndex", firstIndex);
		data.put("queueIndex", queueIndex);
		if(startChannel != null){
			data.put("startChannel", startChannel.getName());
		}
		if(finishChannel != null){
			data.put("finishChannel", finishChannel.getName());
		}
		return ChronoTimer.export.objectToJsonString(data);
	}

	/**
	 Exports Racers for display on Server.
	 @return The JSON data.
	 */
	public String exportServer(){
		Hashtable<String, Serializable> data = new Hashtable<>();
		data.put("racers", racers);
		return ChronoTimer.export.objectToJsonString(data);
	}

	/**
	 Builds Individual Race text to display on center GUI screen.
	 @return The displayed text for the GUI.
	 */
	@Override
	public String raceStats(){
		if(endedDisplay == null){
			String output = "";
			int racersSize = racers.size();
			if(racersSize > 0){
				for(int i = queueIndex; i < queueIndex + 3 && i != racersSize; i++){
					output = racers.get(i).getNumber()+output;
					if(i == queueIndex + 2 || i == racersSize - 1){
						output += "\t>";
					}
					else{
						output = "\n"+output;
					}
				}
				output += "\n\n";
				for(int i = firstIndex; i < queueIndex; i++){
					Racer racer = racers.get(i);
					output += racer.getNumber()+"\t"+ChronoTimer.diffFormat.format(racer.getElapsedTime())+" R\n";
				}
				output += "\n";
				if(queueIndex != 0 && firstIndex - 1 != -1){
					Racer racer = racers.get(firstIndex - 1);
					if(racer.getEndTime() == null){
						output += racer.getNumber()+"\tDNF F";
					}
					else{
						output += racer.getNumber()+"\t"+ChronoTimer.diffFormat.format(racer.getFinalTime())+" F";
					}
				}
			}
			return output;
		}
		return endedDisplay;
	}
}