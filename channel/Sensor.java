package channel;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/28/2016
 */
public abstract class Sensor{

	private boolean state;
	private String type;
	protected Channel whichChannelShouldReceiveEvent;

	public Sensor(Channel channel, String type, boolean state){
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

	public Sensor(Channel channel, String type){
		this(channel, type, false);
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