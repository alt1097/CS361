package channel;

import main.ChronoTimer;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public abstract class Sensor{
//	private ChronoTimer timer;
	private boolean state;
	private String type;
	protected Channel whichChannelShouldReceiveEvent;

	public Sensor(Channel channel, String type, boolean state, ChronoTimer timer){
//		this.timer = timer;
		if(type.equalsIgnoreCase("EYE") || type.equalsIgnoreCase("GATE") || type.equalsIgnoreCase("PAD") || type.equalsIgnoreCase("FIRE_BUTTON")){
			this.type = type;
		}else{
			throw new IllegalArgumentException("Error: Incorrect sensor type.");
		}

		this.state = state;
		// do not create reference to Channel
		// it should not work this way!
		this.whichChannelShouldReceiveEvent = channel;
	}

	public Sensor(Channel channel, String type, ChronoTimer timer){
		this(channel, type, false, timer);
	}

	public void setState(boolean state){
		this.state = state;
	}

	public boolean getState(){
		return state;
	}

	public String getType(){
		return type;
	}

//    public abstract void addListener(SensorListener someone);

	public abstract void enable();

	public abstract void disable();

	public void trigger(String whoIsFiringEvent){
		whichChannelShouldReceiveEvent.fire(whoIsFiringEvent);
	};

}