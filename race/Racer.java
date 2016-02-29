package race;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 Date:  2/26/2016
 */
import java.util.Date;

public class Racer
{
	private int number;
	private Date startTime;
	private Date endTime;

	public Racer(int number)
	{
		this.number = number;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Date getElapsedTime()
	{
		Date elapsedTime = new Date();
		elapsedTime.setTime(ChronoTimer.systemTime.getTime() - startTime.getTime());
		return elapsedTime;
	}
}