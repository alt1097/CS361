package channel;

import main.ChronoTimer;
import race.Racer;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class ChannelFireButton extends Sensor{
	private int name;
	double elapsedTime;

	// need this for test
	public ChannelFireButton(Channel channel, String type, boolean state, int name) {
		super(channel, type, state);
		this.name = name;
	}

	// THIS BUTTON STATE SHOULD ALWAYS BE TRUE!!!

	// use this for regular activities
	public ChannelFireButton(Channel channel) {
		super(channel, "FIRE_BUTTON", true);
		this.name = channel.getName();
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		ChronoTimer.debugLog.add("Error: Channel# " + whichChannelShouldReceiveEvent.getName() +" start/finish button can not be enabled or disabled");

	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		ChronoTimer.debugLog.add("Error: Channel# " + whichChannelShouldReceiveEvent.getName() +" start/finish button can not be enabled or disabled");

	}

	public void trigger(Racer racer) {

		if(whichChannelShouldReceiveEvent.wasFired()){
			ChronoTimer.debugLog.add("Sort of error: channel # " + whichChannelShouldReceiveEvent.getName() + " was fired from somewhere else");
			ChronoTimer.debugLog.add("Reset channel and then use this button");
		}else{
//			racer.addHistory(ChronoTimer.currentEventType + " racer " + racer.getNumber() + " triggered " + super.getType() + " # " + name + " at " +ChronoTimer.format.format(ChronoTimer.pcTime));
//			super.trigger(super.getType() + " " + name + " at " + ChronoTimer.format.format(ChronoTimer.pcTime));
		//	racer.addHistory(timer.getTime() + " racer " + racer.getNumber() + " triggered " + super.getType() + " # " + name + " at " + elapsedTime + "\n");
			super.trigger(super.getType() + " " + name + " at " + ChronoTimer.getTime().getTime());

		}



	}



}