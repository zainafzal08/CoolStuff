
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author zainafzal
 */
public class Venue {
	//fields
	private ArrayList<Room> rooms;
	private String name;
	
	/**
	 * the constructor initilizes the venue name and the rooms arrayList
	 * @param nameIn - venue name
	 * 
	 * @preconditon 
	 * @postcondition name is assigned and rooms is initilized
	 */
	public Venue(String nameIn){
		name = nameIn;
		rooms = new ArrayList<Room>();
	}
	
	/**
	 * addRoom will add a room to the venue with the specifed name and size
	 * @param nameIn - name of room
	 * @param sizeIn - size of room, "small" "large" or "medium"
	 * 
	 * @precondition size must be either "small" "large" or "medium", rooms arrayList is initilized
	 * @postcondition rooms arrayList has same elements in same order has before but with a new room appended
	 * to the end that has a name and size matching the inputs. 
	 * 
	 */
	public void addRoom(String nameIn, String sizeIn){
		Room newRoom = new Room(nameIn, sizeIn);
		rooms.add(newRoom);
	}
	
	/**
	 * returns name of venue
	 * @return name of venue
	 */
	public String getName(){
		return name;
	}

	/**
	 * processes the venue information and prints out a description of the venue
	 * outpit consists of a series of lines. Each line describes a room in the venue and is 
	 * printf out in order of room decleration. 
	 * each line has the venue name, room name, and all bookings for that room in order of date
	 * @return a series of lines in the format of [venue name] [Room name] [Booking 1 info] [Booking 2 info]
	 * 
	 * @precondition rooms arrayList is initilized. 
	 * @postcondition rooms arrayList is unaltered, output in the format specified is sent to system output
	 */
	public void printOccupancy(){
		//print out all rooms occupancy
		for(Room room : rooms){
			//this prints out the venue name followed by a room and it's bookings
			System.out.println(name + " " + room.toString());
		}
	}
	
	/**
	 * if possible, adds a reservation to the venue and returns a completed reservation object. 
	 * if possible, adds a relevent booking to every requested room and gives a reference to these rooms to
	 * the reservatin object. This reservatio object is then returned. 
	 * if the reservation can not be met, a null reservation us returned
	 * @param id - reservation id
	 * @param start- start date as a calendar object
	 * @param end - end state as a calendar object
	 * @param smallRooms - number of small rooms
	 * @param mediumRooms - number of medium rooms
	 * @param largeRooms - number of large rooms
	 * @return if possible a complete reservation object, otherwise a null reservation object. 
	 * 
	 * @precondition smallRooms, mediumRooms anre largeRooms are >=0, input dates are valid, rooms arrayList is initilized. 
	 * @postconditon if failed, a null reservation is returned, if successful, a reservation object with references to
	 * all related rooms, an identical venue name and an identical id to that requested is created. All rooms under the reservation have a booking
	 * object attached, which has identical start date, end date and id as requested. The number of small rooms, medium rooms
	 * and large rooms requested have been assigned, no more or less. 
	 */
	public Reservation addReservation(int id, Calendar start, Calendar end, int smallRooms, int mediumRooms, int largeRooms){
		//check if reservation is possible
		int neededSmall = smallRooms;
		int neededMedium = mediumRooms;
		int neededLarge = largeRooms;
		Reservation reservation = new Reservation(id, name);
		for(Room room : rooms){
			if(room.getSize().equals("large") && room.available(start, end)) neededLarge--;
			if(room.getSize().equals("medium") && room.available(start, end)) neededMedium--;
			if(room.getSize().equals("small") && room.available(start, end)) neededSmall--;
		}
		if(neededLarge > 0 || neededMedium > 0 || neededSmall > 0){
			reservation.setIsNull(true);
			return reservation;
		}

		//assign rooms and make reservation
		neededSmall = smallRooms;
		neededMedium = mediumRooms;
		neededLarge = largeRooms;
		for(Room room : rooms){
			if(room.getSize().equals("large") && room.available(start, end) && neededLarge > 0){
				neededLarge--;
				room.addBooking(new Booking(id, start, end));
				Room roomReference = room; 
				reservation.addRoom(roomReference);
			}
			if(room.getSize().equals("medium") && room.available(start, end) && neededMedium > 0){
				neededMedium--;
				room.addBooking(new Booking(id, start, end));
				Room roomReference = room; 
				reservation.addRoom(roomReference);
			}
			if(room.getSize().equals("small") && room.available(start, end) && neededSmall > 0){
				neededSmall--;
				room.addBooking(new Booking(id, start, end));
				Room roomReference = room; 
				reservation.addRoom(roomReference);
			}
		}
		return reservation;
	}	
	
	/**
	 * is change possible returns true if a requested change is possible and false otherwise. 
	 * checks if the current requested reservation, referenced by id, could be altered to meet the new criteria
	 * 
	 * @param id - reservation to be changed id
	 * @param start - start date of changed reservation as a calendar object
	 * @param end - end date of changed resservation as a calendar object
	 * @param smallRooms - number of small rooms
	 * @param mediumRooms - number of medium rooms
	 * @param largeRooms - number of large rooms
	 * 
	 * @return true if change is possible, false otherwise
	 * 
	 * @precondition smallRooms, mediumRooms anre largeRooms are >=0, input dates are valid, id must
	 * be assigned to a preexisting reservation
	 * @postcondition room arrayList remains unaltered. 
	 */
	public boolean isChangePossible(int id, Calendar start, Calendar end, int smallRooms, int mediumRooms, int largeRooms){
		int neededSmall = smallRooms;
		int neededMedium = mediumRooms;
		int neededLarge = largeRooms;
		for(Room room : rooms){
			if(room.getSize().equals("large") && room.changeAvailable(start, end, id)) neededLarge--;
			if(room.getSize().equals("medium") && room.changeAvailable(start, end, id)) neededMedium--;
			if(room.getSize().equals("small") && room.changeAvailable(start, end, id)) neededSmall--;
		}
		if(neededLarge > 0 || neededMedium > 0 || neededSmall > 0) return false;
		return true;
	}
}
