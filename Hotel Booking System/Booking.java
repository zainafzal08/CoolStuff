import java.text.SimpleDateFormat;
import java.util.Calendar;
/* 
 * @author zainafzal
 */
public class Booking {
	//fields
	private int id;
	private Calendar startDate;
	private Calendar endDate;
	private int duration;
	/**
	 * the constructor initilizes the booking id, start date and end date. 
	 * it also calculates the duration of the stay in days and stores the value
	 * @param idIn - booking reservation id
	 * @param startIn - start date as a calendar object
	 * @param endIn - end date as a calendar object
	 */
	Booking(int idIn, Calendar startIn, Calendar endIn){
		id = idIn;
		startDate = startIn;
		endDate = endIn;
		duration = endDate.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);
		duration++;
	
	}
	
	/**
	 * toString returns a string with the booking start date and duration in the format
	 * [Month in 3 letter form] [date in 1 or 2 number form] [duration]
	 * @return a string describing the booking in [MMM] [day] [duration] format 
	 * 
	 * @preconditon startDate has been set, duration has been calculated
	 * @postcondition startDate has not been altered, duration has not been altered
	 */
	public String toString(){
		//returns start date in MM d formatt wih the duration of the stay added to the end
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
		formatter.setCalendar(startDate);
		return formatter.format(startDate.getTime()) + " " + duration;
	}
	
	/**
	 * Gets reservation id
	 * @return reservation id
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Gets start date
	 * @return start date
	 */
	public Calendar getStartDate(){
		return startDate;
	}
	/**
	 * checks if the requested reservation period
	 * clashes with this bookings reservation period
	 * @param reqStart the reqeusted reservation periods start date
	 * @param reqEnd the reqeusted reservation periods end date
	 * @return true if the bookings overlap and thus clash, false otherwise
	 * 
	 * @preconditon startDate and endDate have been set
	 * @postcondition startDate and endDate have not been altered.
	 */
	public boolean isClash(Calendar reqStart, Calendar reqEnd){
		//this functions imagines times as  linear line 
		//and bookings as shorter line segments on the time line
		//via this represernation you can see if two dates 
		//clash by seeing of two line segements touch at all or not
		
		//if req.period starts in est. period
		if(startDate.before(reqStart) && endDate.after(reqStart)) return true;
		//if req.period ends in est. period
		if(startDate.before(reqEnd) && endDate.after(reqEnd)) return true;
		//if req.period envelops est. period
		if(startDate.after(reqStart) && endDate.before(reqEnd)) return true;
		//boundary case
		if(startDate.equals(reqStart) || startDate.equals(reqEnd)) return true;
		if(endDate.equals(reqStart) || endDate.equals(reqEnd)) return true;
		//else everything is ok
		return false;
	}
}
