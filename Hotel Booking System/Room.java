import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author zainafzal
 * @class_invarient the bookings array is always sorted in ascending order of date
 */
public class Room{
	//fields
	private String name;
	private String size;
	private ArrayList<Booking> bookings;
	
	/**
	 * the contructor assigns the name of the room and size and intilizes the <code>bookings</code> array
	 * @param nameIn - name of room
	 * @param sizeIn - size of room, either "small", "large" or "medium"
	 * 
	 * @preconditon size of room must be either "small", "medium" or "large", name must not be null. 
	 * @postcondition name and size fields are set correctly. <code>bookings</code> array is initialized and empty. 
	 */
	Room(String nameIn, String sizeIn){
		name = nameIn;
		size = sizeIn;
		bookings = new ArrayList<Booking>(); //needs to be sorted
	}
	
	/**
	 * returns size of room
	 * @return either"small", "medium" or "large"
	 */
	public String getSize(){
		return size;
	}
	
	/**
	 * returns name of room
	 * @return name of room
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * processes the room information and creates a string describing the room
	 * string consists of the rooms name and then the bookings, using booking.toString, in order of date.
	 * @return a string in the form of [room name] [booking 1 info] [bookng 2 info] ...
	 * 
	 * @precondition bookings arrayList is in sorted order and initilized. name is set. 
	 * @postcondition bookings arrayList is unalterd, name is unaltered.
	 */
	public String toString(){
		//this grabs the name of the room and then all the bookings for the room
		//bookings are printed out in acsending order of date as the bookings arrayList is sorted
		String output = name;
		for(Booking booking : bookings){
			//construction of string in form of [Room Name] [booking 1] [booking 2] ... [booking n]
			//where Bookings are [Start Date ] [Duration]
			output = output + " " + booking.toString();
		}
		return output;
	}
	
	/**
	 * add's a booking to the <code> bookings </code> arrayList in the correct place to maintain
	 * a ascending order of element by date
	 * @param input - Booking to be attached to room
	 * 
	 * @precondition Bookings arrayList is in ascending order, input is a valid Booking. 
	 * @postcondition Bookings arrayList is in ascending order and constains <code> input </code>
	 */
	public void addBooking(Booking input){
		int index = -1;
		//bookings needs to be in order of date
		if(bookings.size() == 0) bookings.add(input); //if it's empty just chuck the booking in
		else{ 
			//else yah gotta look for the correct place to put it
			for(Booking booking : bookings){
				//assume arrayList is already sorted. find element that should be ahead of toInsert element
				if(input.getStartDate().before(booking.getStartDate())){
					index = bookings.indexOf(booking);
					break;
				}
			}
			if(index == -1) bookings.add(input); //no such point was found, this date is placed on the end
			else bookings.add(index, input); //add the toinsert element into the place of the element that should be in front of it. 
			//the element that shoud be infront of it shifts forward as does the rest of the arrayList to make room. 
		}
		
	}
	
	/**
	 * removes bookings from the room that match the id given
	 * @param id - id of reservation which is to be removed
	 * 
	 * @precondition bookings arrayList is initilized
	 * @postcondition bookings arrayList no longer contains any bookings that have an id matching to the given id
	 */
	public void removeBookings(int id){
		ArrayList<Booking> toRemove = new ArrayList<Booking>(); //keep track of what bookings to remove
		//find what we have to delete
		for(Booking booking : bookings){
			if(booking.getId() == id) toRemove.add(booking); //add toCancel bookings to the toRemove arrayList
		}
		//now remove all the targeted bookings
		for(Booking booking : toRemove){
			bookings.remove(booking);
		}
	}
	/**
	 * available checks if this room is occupied or free over a given time period from. 
	 * <code>startDate</code> to <code>endDate</code>.
	 * returns true if the room is available (free) and false if it is not available (occupied)
	 * @param start - start date as a calendar object
	 * @param end - end date as a calendat object
	 * @return true if available over requested time period,  false otherwise, 
	 * 
	 * @precondition bookings arrayList is intilized
	 * @postcondition bookings arrayList is unaltered, stored bookings are unaltered. 
	 */
	public boolean available(Calendar start, Calendar end){
		//check if any present bookings clash with the requested booking
		//isClash just makes sure the stay periods don't overlap at all. 
		for(Booking booking : bookings){
			if(booking.isClash(start, end)) return false;
		}
		return true;
	}
	
	/**
	 * similar to the available method, changeAvailable checks if this room is occupied or free over a given time period from. 
	 * code>startDate</code> to <code>endDate</code> but will ignore any bookings that match the id given. This function
	 * this does not register a booking to clash with itself and be used to see if a already established reservation can be altered.  
	 * returns true if the room is available (free) or overlaps with only itself and false otherwise. 
	 * @param start - start date as a calendar object
	 * @param end - end date as a calendat object
	 * @param inputId - reservation id to be ignored
	 * @return true if available over requested time period, ignoring bookings with an id matching input id, false otherwise.
	 * 
	 * @precondition bookings arrayList is initialized
	 * @postcondition bookings arrayList is unaltered, individual bookings are inaltered. 
	 */
	public boolean changeAvailable(Calendar start, Calendar end, int inputId){
		//see if any present booking clashses with requested booking
		//but ignore this clash if it is with the toChange reservation
		for(Booking booking : bookings){
			if(booking.isClash(start, end) && booking.getId() != inputId) return false;
		}
		return true;
	}
}
