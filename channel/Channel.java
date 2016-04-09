package channel;

import main.ChronoTimer;
import race.Racer;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */
public class Channel implements SensorListener{
	private boolean channelWasFired;
	private int number;
	private String type;
	private boolean state;
	private ChannelFireButton fireButton;
	private FieldSensor fieldSensor;

//	// create a channel with name
//	public Channel(int name){
//		this.number = name;
//		state = false;
//		channelWasFired = false;
//		fireButton = new ChannelFireButton(this);
//	}
//
//	// if don't need a name set it to 1 and create channel
//	public Channel(String channelType){
//		this(1);
//	}
//
//	// need to create channel with attached field sensor? use this one
//	public Channel(int name, String fieldSsensorType){
//		this(name);
//		fieldSensor = new FieldSensor(this, fieldSsensorType);
//	}

	public Channel(String channelType, int number, String fieldSsensorType){
		type = channelType;
		this.number = number;
		fireButton = new ChannelFireButton(this);
		if(fieldSsensorType == null){
			fieldSsensorType = null;
		}else{
			fieldSensor = new FieldSensor(this, fieldSsensorType);
		}

	}

	public Channel(String channelType){
		this(channelType, 1, null);
	}

	public Channel(){
		this("ANON", 1, null);
	}

	public Channel(int channelNumber){
		this("ANON", channelNumber, null);
	}

	public void setChanType(String channelType){
		type = channelType;
	}


	public boolean getFireButtonState(){
		if(fireButton != null){
			return fireButton.getState();
		}else{
			ChronoTimer.debugLog.add("Channel # " + number + " fire button is NULL");
			return false;
		}
	}

	public boolean getFieldSensorState(){
		if(fieldSensor != null){
			return fieldSensor.getState();
		}else{
			ChronoTimer.debugLog.add("Channel # " + number + " field sensor is NULL");
			return false;
		}
	}

	public void toggleSensor(){
		if(fieldSensor != null){
			fieldSensor.toggle();
		}else{
			ChronoTimer.debugLog.add("Channel # " + number + " field sensor is NULL");
		}
	}

	public String getChannelType(){
		return type;
	}

	public int getName(){
		return number;
	}

	public boolean isOn(){
		return state;
	}

	public boolean wasFired(){
		return channelWasFired;
	}

	// toggle this channel
	public void toggle(){
		if(state){
			state = false;
		}else{
			state = true;
		}
		ChronoTimer.debugLog.add("Channel " + type + " was toggled, it is " +state + " now");
	}

	public void connect(String fieldSensorType) {

		if(fieldSensorType.equals("FIRE_BUTTON")){
			ChronoTimer.debugLog.add("Error: This method to connect field sensors only, not a buttons");
			return;
		}
		if (fieldSensor == null) {
			fieldSensor = new FieldSensor(this, fieldSensorType);
			ChronoTimer.debugLog.add("Channel # " + number+ " " + type + " was successfully connected to " + fieldSensor.getType() + " sensor.");
		} else {
			System.out.println("Something is already connected to this port, disconnect it firs");
			ChronoTimer.debugLog.add("Error: channel # " + number + " connected to " + fieldSensor.getType() + " sensor. Disconnect old one manually");
		}
	}



	public void disconnect(){
		if(fieldSensor == null){
			ChronoTimer.debugLog.add("Error: channel # " + number + " have no field sensor attached. There is nothing to disconnect");
		}else{
			ChronoTimer.debugLog.add("Channel # " + number + " was disconnected from " + fieldSensor.getType() + " sensor.");
			fieldSensor = null;
		}
	}

	// when some event was fired channel is on, but can not fire more events
	// reset manually over this method
	public void reset(){
		channelWasFired = false;
	}

	// this is manual start/finish control
	public void fireButtonEvent(Racer racer){
		if(state && !channelWasFired){
			fireButton.trigger(racer);
			channelWasFired = true; // comment this out if tired to reset
		}else if(state && channelWasFired){
			ChronoTimer.debugLog.add("Error: channel# " + number + "fired some event before");
		}else if(!state){
			ChronoTimer.debugLog.add("Error: channel# " + number + "is disabled, fire button is useless");
		}

	}



	// this is simulation to fire event from field sensor
	public void fireFieldSensorEvent(Racer racer){
		if(state && !channelWasFired){
			if(fieldSensor == null){
				ChronoTimer.debugLog.add("Error: seems like channel " + number +" have no field sensor attached to it");
			}else{
				fieldSensor.trigger(racer);
				channelWasFired = true; // comment this out if tired to reset
			}
		}else if(state && channelWasFired){
			ChronoTimer.debugLog.add("Error: channel# " + number + " fired some event before, can not fire another event without resetting");
		}else if(!state){
			ChronoTimer.debugLog.add("Error: channel# " + number + " is disabled, field sensor can not be fired from this method");
		}
	}

	public void fireChannel(Racer racer){
		if(fieldSensor == null){
			fireButtonEvent(racer);
		}else{
			fireFieldSensorEvent(racer);
		}

		if(getChannelType().equals("START")){
			if(racer.getStartTime() == null){
				racer.setStartTime(ChronoTimer.getTime());
			}else{
				ChronoTimer.debugLog.add("Error: This racer already have START time");
			}
		}else if(getChannelType().equals("FINISH")){
			if(racer.getEndTime() == null){
				racer.setEndTime(ChronoTimer.getTime());
			}else{
				ChronoTimer.debugLog.add("Error: This racer already have FINISH time");
			}
		}
	}

	@Override
	public void fire(String messageFromEventInitiator) {
		// TODO Auto-generated method stub
		ChronoTimer.debugLog.add("Channel# " + number +" was triggered at: " + messageFromEventInitiator);

	}




}