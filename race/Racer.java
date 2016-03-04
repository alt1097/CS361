package race;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/26/2016
 */
import java.util.Date;

import main.ChronoTimer;

public class Racer
{
	/**
	 * An individual racer's identification number
	 */
	private int number;
	
	/**
	 * The time that the racer started a race
	 */
	private Date startTime;
	
	/**
	 * The time that the racer ended a race
	 */
	private Date endTime;

	/**
	 * Creates a racer with a unique number
	 * @param number Racer identification number
	 */
	public Racer(int number)
	{
		this.number = number;
	}

	/**
	 * Gets the unique identification number of a specified racer
	 * @return number Number of a racer
	 */
	public int getNumber()
	{
		return number;
	}

	/**
	 * Sets the number of a racer
	 * @param number Number of a racer
	 */
	public void setNumber(int number)
	{
		this.number = number;
	}

	/**
	 * Gets the start time of a racer
	 * @return startTime The time (in date format) that the racer started a race
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the start time of a racer
	 * @param startTime The time (in date format) that the racer started a race
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Gets the end time of a racer
	 * @return endTime The time (in date format) that the racer completed a race
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * Sets the end time of a racer
	 * @param endTime The time (in date format) that the racer completed a race
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	/**
	 * Computes the racer's final time of a race by subtracting their start
	 * time from their end time.
	 * @return finalTime
	 */
	public Date getFinalTime(ChronoTimer ct)
	{
		if(endTime == null){
			return null;
		}
		else{
			Date finalTime = new Date();
			finalTime.setTime(endTime.getTime() - startTime.getTime() - ct.getNewOffset());
			return finalTime;
		}
	}

	/**
	 * Returns the elapsed time of a racer at any given point during a race
	 * by subtracting their start time from the current system time.
	 * @param ct Reference to the ChronoTimer
	 * @return elapsedTime How long a racer has been running for at any given time during a race
	 */
	public Date getElapsedTime(ChronoTimer ct)
	{
		Date elapsedTime = new Date();
		elapsedTime.setTime(ct.getTime().getTime() - startTime.getTime());
		return elapsedTime;
	}
}