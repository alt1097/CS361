package channel;

import main.ChronoTimer;
import race.Racer;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class FieldSensor extends Sensor{
	private ChronoTimer timer;
	private int name;
	double elapsedTime;

	// for tests only
	public FieldSensor(Channel channel, String type, boolean state, int name, ChronoTimer timer) {
		super(channel, type, state, timer);
		this.timer = timer;
		this.name = name;
	}

	// use this for regular activities
	public FieldSensor(Channel channel, String type, ChronoTimer timer) {
		super(channel, type, false, timer);
		this.name = channel.getName();
	}

	@Override
	public void enable() {
		if(super.getState()){
			ChronoTimer.debugLog.add("Error: trying to enable a field sensor that is active already");
		}else{
			super.setState(true);
		}
	}

	@Override
	public void disable() {
		if(super.getState()){
			super.setState(false);
		}else{
			ChronoTimer.debugLog.add("Error: trying to disable a field sensor that is disabled already");
		}
	}

	public void toggle(){
		if(getState()){
			setState(false);
		}else{
			setState(true);
		}
	}

//	public String getType(){
//		return super.getType();
//	}

	public void trigger(Racer racer) {

		// attempt to use System.nanoTime(). Problem: time calculated from arbitrary start point. Can not be converted to valid Date object
//		elapsedTime =  (double) (System.nanoTime() - ChronoTimer.pcTime)/ 1000000000.0;
//		elapsedTime = Math.floor(elapsedTime * 1000000) / 1000000;

		if(whichChannelShouldReceiveEvent.wasFired()){
			ChronoTimer.debugLog.add("Sort of error: channel # " + whichChannelShouldReceiveEvent.getName() + " was fired from somewhere else");
			ChronoTimer.debugLog.add("Reset channel and then use this sensor");
		}else{
//			racer.addHistory(ChronoTimer.currentEventType + " racer " + racer.getNumber() + " triggered " + super.getType() + " # " + name + " at " +ChronoTimer.format.format(ChronoTimer.pcTime) + "\n");
//			super.trigger(super.getType() + " by racer #" + racer.getNumber() + " at " + ChronoTimer.format.format(ChronoTimer.pcTime));
	//		racer.addHistory(timer.getTime() + " racer " + racer.getNumber() + " triggered " + super.getType() + " # " + name + " at " + ChronoTimer.simulatorTime + "\n");
		//	super.trigger(super.getType() + " by racer #" + racer.getNumber() + " at " + timer.getTime());

			// attempt to use current time. Problem: System.currentTimeMillis() returns same time every call
//			if(racer.getStart() == null && racer.getEnd() == null){
//				ChronoTimer.debugLog.add("START TIME SENT " + System.currentTimeMillis());
//				racer.setStart(new Date(System.currentTimeMillis()));
//			}else{
//				ChronoTimer.debugLog.add("Error: This racer already have START time");
//			}
//
//			if(racer.getStart() != null && racer.getEnd() == null){
//
//				ChronoTimer.debugLog.add("END TIME SENT " + System.currentTimeMillis());
//				racer.setEnd(new Date(System.currentTimeMillis()));
//			}else{
//				ChronoTimer.debugLog.add("Error: This racer already have END time");
//			}


		}
	}

}