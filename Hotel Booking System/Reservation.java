import java.util.ArrayList;
/**
 * 
 * @author zainafzal
 */
public class Reservation {
	//fields
	private int id;
	private String venue;
	private boolean isNull; // a null reservation respresents a reservation that could not be constructed. 
	//we need a list of references to the rooms that are involved with this reservation
	private ArrayList<Room> rooms;
	
	/**
	 * this constructor assigns the reservations room, venue and initilizes the rooms arrayList.
	 * it also sets the reservation to be non null. a null reservation respresents a reservation
	 * that could not be constructed. 
	 * @param idIn - reservation id
	 * @param venueIn - venue name
	 * 
	 * @preconditions the reservation id is unique.
	 * @postconditions, isNull is false, is and venue ahve been assigned and rooms arrayList is initilizes
	 */
	Reservation(int idIn, String venueIn){
		isNull = false;
		id = idIn;
		venue = venueIn;
		rooms = new ArrayList<Room>();
	}
	
	/**
	 * processes the reservation information and creates a string describing the reservation
	 * string. Consists of the reservation id, venue name and then the rooms
	 * @return a string in the form of [id] [venuw] [room 1 name] [room 2 name] ...
	 * 
	 * @precondition rooms arrayList is initilized.
	 * @postcondition rooms arrayList is unaltered
	 */
	public String toString(){
		String output = id + " " + venue;
		for(Room room : rooms){
			output = output + " " + room.getName();
		}
		return output;
	}
	
	/**
	 * returns if this reservation is null or not
	 * @return true if reservation is null, false otherwise
	 * 
	 * @preconditon isNull is initilized. 
	 * @postconditions isNull is unaltered
	 */
	public Boolean isNull(){
		return isNull;
	}
	/**
	 * sets the reservation as null or not
	 * @param input - true or false
	 * 
	 * @preconditon isNull is initliazed. 
	 * @postconditions isNull now equals input
	 */
	public void setIsNull(Boolean input){
		isNull = input;
	}
	/**
	 * add room will add a room reference to the reservation
	 * @param input - the reference to be added to the reservation
	 *
	 * @preconditon rooms arrayList is initlaized, input is a reference to a established room in a venue. 
	 * @postcondition rooms contains the same elements in the same order as before but with <code>input</code> appended to the end
	 */
	public void addRoom(Room input){
		rooms.add(input);
	}
	
	/**
	 * returns reservation id
	 * @return reservation id
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * cancel will remove this reservation from the system
	 * it goes to every room involved and asks the room to remove all bookings realted to this reservation
	 * 
	 * @preconditon rooms is initalized and correctly populated with references to the acutal room objects in venue
	 * @postconditon any room with bookings of <code>id</code> has those bookings removed. 
	 */
	public void cancel(){
		for(Room room : rooms){
			room.removeBookings(id);
		}
	}
}
