import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author Zain Afzal
 * @author z5059449
 * @author 10am Thursday Tute (Tuba) (Brad Heap)
 * 
 */
public class VenueHireSystem {
	//fields
	private ArrayList<Venue> venues;
	private ArrayList<Reservation> reservations;
    private static Map<String, Integer> months;
	
    /**
     * main initlaises a VenueHireSystem hire system object
     * and calls the run method on whatever file is given in as argument one
     * @param args - arguments given from command line
     * 
     * @precondition args[0] contains a string
     * @postcondition if possible, file sepcifed in args[0] is opened and all valid commands read in 
     * and processed
     */
	public static void main(String[] args){
		VenueHireSystem system = new VenueHireSystem();
		system.run(args[0]);
	}
	
	/**
	 * Venue Hire System constructor 
	 * Initialises venue arrayList, reservation arrayList
	 * also sets up a hash map called <code> months </code> 
	 * that maps a string key, a 3 letter month, to a int (0-11) 
	 * 
	 * @precondion venues, reservations and months are declared but un-initialised . 
	 * @postcondition <code>venues</code> and <code>reservations</code> are now empty Arraylists 
	 *	and months is valid hash map that maps 12 months to 12 ints. with Jan = 0 Feb = 1 onwards to Dec = 11
	 */
	VenueHireSystem(){
		venues = new ArrayList<Venue>();
		reservations = 	new ArrayList<Reservation>();
    	months = new HashMap<String, Integer>(); 
    	months.put("Jan", 0);
    	months.put("Feb", 1);
    	months.put("Mar", 2);
    	months.put("Apr", 3);
    	months.put("May", 4);
    	months.put("Jun", 5);
    	months.put("Jul", 6);
    	months.put("Aug", 7);
    	months.put("Sep", 8);
    	months.put("Oct", 9);
    	months.put("Nov", 10);
    	months.put("Dec", 11);
	}
	
	/**
	 * request takes in a reservation id, start date, end date and needed rooms for a request. 
	 * if the request can be met it will assign bookings to the relevant rooms and create a reservation object
	 * to be stored within the venue hire system. on success of these actions it prints out the 
	 * reservation information. If the request can not be met the method prints out "Request rejected" to
	 * standard output. 
	 * @param id - reservation identification number
	 * @param start - start date of stay, as a Date object
	 * @param end - end date of stay, as a Date object
	 * @param smallRooms - int number of small rooms requested
	 * @param mediumRooms- int number of medium rooms requested
	 * @param largeRooms- int number of large rooms requested
	 * 
	 * @precondition all int input is >=0, Date input is valid and in 2016, id is unique , reservation and venues arrayLists
	 * have been created and the months hashmap has been created and initialized correctly.
	 * @postcondition if request is possible, a number of rooms of each type requested have had a booking object attached
	 * to them, marking them as occupied from <code> start </code> to <code> end </code>. A reservation obejct holding all 
	 * reserved rooms and other reservation information has been created and stored in the reservations arrayList  
	 */
	public void request(int id, Calendar start, Calendar end, int smallRooms, int mediumRooms, int largeRooms){
		String output = privateRequest(id, start, end, smallRooms, mediumRooms, largeRooms);
		if(output == null) System.out.println("Request rejected");	
		else System.out.println("Reservation " + output);
	}

	/**
	 * private request does the actual logic of the request functions at a higher abstracted level so it
	 * can be used by multiple commands. creates a reservation object and and attaches relevent bookings to 
	 * relevent rooms if possible, if not returns a null string to signal a fail
	 * @param id - reservation identification number
	 * @param start - start date of stay, as a Date object
	 * @param end - end date of stay, as a Date object
	 * @param smallRooms - int number of small rooms requested
	 * @param mediumRooms - int number of medium rooms requested
	 * @param largeRooms - int number of large rooms requested
	 * @return The reservation information, grabbed from the toString function of the created reservation object
	 * 
	 * @precondition all int input is >=0, Date input is valid and in 2016, id is unique , reservation and venues arrayLists
	 * have been created and the months hashmap has been created and initialized correctly.
	 * 
	 * @postcondition if request is possible, a number of rooms of each type requested have had a booking object attached
	 * to them, marking them as occupied from <code> start </code> to <code> end </code>. A reservation obejct holding all 
	 * reserved rooms and other reservation information has been created and stored in the reservations arrayList
	 */
	private String privateRequest(int id, Calendar start, Calendar end, int smallRooms, int mediumRooms, int largeRooms){
		for(Venue venue : venues){
				//see if you can make the reservation
				Reservation newReservation = venue.addReservation(id, start, end, smallRooms, mediumRooms, largeRooms);
				if (newReservation.isNull()){} //progress through loop
				else{
					reservations.add(newReservation);
					return newReservation.toString();
				}
			}
		return null; //if all else fails return null
	}
	
	/**
	 * the Venue Command will take in a venue name, and room . 
	 * if the venue does not exist, the method will create the venue and add the first room
	 * if the venue does exist, the method will add the room into the venue. 
	 * @param venueName - requested name of the Venue, assumed to be a single word
	 * @param roomName - requested name of the room. 
	 * @param roomSize - size of the room, either "large" " medium" or "small"
	 * 
	 * @precondition roomSize is either "large" "medium" or "large, venues is initialized correctly, names must not be null.
	 * @postcondition if venue is non existent, this venue arrayList now holds a venue 
	 * matching the name of the given venue name. this venue has a room of the specified room name and size.
	 * if venue is existent, it now holds an extra room matching the specified room name and room size
	 * 
	 */
	public void VenueCommand(String venueName, String roomName, String roomSize){
		for (Venue venue : venues){
			if(venue.getName().equals(venueName)){
				venue.addRoom(roomName, roomSize);
				return;
			}
		}
		Venue newVenue = new Venue(venueName);
		newVenue.addRoom(roomName, roomSize);
		venues.add(newVenue);
	}
	
	
	/**
	 * print venue will find the specified venue and print out all rooms in
	 * order of input into system and after each room the dates and durations it is occupied for
	 * if the venue does not exist the method will print "Venue not found" and end
	 * @param venueName - the name of the venues whose occupancy is to be printed
	 * 
	 * @preconditions ArrayList venues is initialized correctly
	 * @postcondition a series of occupancy information is sent to the output stream. venue arraylist has not
	 * been altered
	 */
	public void printVenue(String venueName){
		for (Venue venue : venues){
			if(venue.getName().equals(venueName)){
				venue.printOccupancy();
				return;
			}
		}
		//if not found, venue was not found
		System.out.println("Venue not found");
	}
	
	/**
	 * cancel reservation looks for the reservation matching the id given and removes it
	 * from the reservations arrayList. in doing this it also removes all relevent bookings
	 * from relevent rooms. 
	 * if reservtion id can not be found "Cancel Rejected" is printed to screen
	 * @param id - Reservation identification number 
	 * 
	 * @precondition id is unique. reservations arrayList exists and was initilized correctly
	 * @postcondition any bookings and reservations matching the specifided id's in the system have been removed
	 * if the cancel request is rejected, venues, rooms and reservations are unlatered from before methods start
	 */
	public void cancelReservation(int id){
		if(privateCancel(id)) System.out.println("Cancel " + id);
		else System.out.println("Cancel rejected");
	}
	
	/**
	 * privateCancel is a private helper function to perform actual cancel logic. Thus is can be used
	 * as a more generalised functions within other commands. if found, the specifided reservation is removed
	 * from the <code> reservations </code> arrayList and relevent bookings are removed from rooms and the method
	 * returns true. if the reservation is not found the function returns false to signal a fail. 
	 * @param id - id of reservation to be removed
	 * @return true if Cancel successfuly removed the reservation, false otherwise
	 * 
	 * @precondition id is unique. reservations arrayList exists and was initilized correctly
	 * @postcondition any bookings and reservations matching the specifided id's in the system have been removed. 
	 * if the cancel request is rejected, venues, rooms and reservations are unlatered from before methods start
	 */
	private boolean privateCancel(int id){
		int index = -1;
		boolean found = false;
		for (Reservation res : reservations){
			if(res.getId() == id){
				index = reservations.indexOf(res);
				found = true;
				break;
			}
		}
		if(found){
			Reservation target = reservations.get(index);
			target.cancel();
			reservations.remove(target);
			return true;
		}else{
			//Reservation was not found
			return false;
		}
	}
	
	/**
	 * change will take a reservation id and paramaters.
	 * if the system allows it, the previous request will be replaced with a new request
	 * matching the new requirements but maintaining the same id. A confirmation line
	 * is then printed to screen outlining the changed reservation.
	 * if the system can not perform the change, "Change rejected" is printed to screen. 
	 * @param id - reservation identification number of preexisting reservation
	 * @param start - start date of stay, as a Date object
	 * @param end - end date of stay, as a Date object
	 * @param smallRooms - int number of small rooms requested
	 * @param mediumRooms- int number of medium rooms requested
	 * @param largeRooms- int number of large rooms requested
	 * 
	 * @precondition all int input is >=0, Date input is valid and in 2016, reservation and venues arrayLists
	 * have been created and the months hashmap has been created and initialized correctly.
	 * @postcondition if change is possible, the relevent rooms have been either assigned new bookings, updated bookings or deassigned bookings depending on request.
	 * the relevant Reservation object in the reservations array has been updated. 
	 * if the change request is rejected, venues, rooms and reservations are unlatered from before methods start
	 */
	public void change(int id, Calendar start, Calendar end, int smallRooms, int mediumRooms, int largeRooms){
		//check if possible
		boolean possible = true;
		boolean changePossible = false;
		boolean idExists = false;
		for(Reservation reservation : reservations){
			if(reservation.getId() == id) idExists = true;
		}
		for(Venue venue : venues){
			if(venue.isChangePossible(id, start, end, smallRooms, mediumRooms, largeRooms)) changePossible = true;
		}
		if(idExists == false) possible = false;
		if(changePossible == false) possible = false;
	
		//perform relevant action
		if(possible){
			privateCancel(id);
			String output = privateRequest(id, start, end, smallRooms, mediumRooms, largeRooms);
			System.out.println("Change " + output);
		}else{
			System.out.println("Change rejected");
		}
	}
	
	/**
	 * run will take in a fileName and using a Scanner object read in data from the file 
	 * if the file can not be opened or found, this method will exit with no output
	 * if the file is successfully opened for reading, every command in the file is identifed and the relevent
	 * input data is parsed into the relevent methods, meeting each methods preconditons.
	 * @param fileName - name of file to be read from
	 * @precondition input file is formatted correctly, after a command [Venue, Change...] all needed parameters are present in correct order
	 * @postconditons the input is parsed to the relevent functions in the correct form to be accepted by methods. 
	 */
	private void run(String fileName){
		Scanner sc = null;
		try{
			sc = new Scanner(new FileReader(fileName));
		    String command;
		    while(sc.hasNext()){
		    	command = sc.next();
		    	//Venue
		    	if(command.equals("Venue")){
			    	this.VenueCommand(sc.next(),sc.next(), sc.next());
			     }
		    	//Request
			    if(command.equals("Request")){
		    		  int id = sc.nextInt();
		    		  Calendar start = new GregorianCalendar(2016, months.get(sc.next()), sc.nextInt());
		    		  Calendar end = new GregorianCalendar(2016, months.get(sc.next()), sc.nextInt());
		    		  int largeRooms = 0;
		    		  int mediumRooms = 0;
		    		  int smallRooms = 0;
		    		  while(sc.hasNextInt()){
		    			  int number = sc.nextInt();
		    			  String type = sc.next();
		    			  if(type.equals("large")) largeRooms = number;
		    			  if(type.equals("medium")) mediumRooms = number;
		    			  if(type.equals("small")) smallRooms = number;
		    		  }
		    		  this.request(id, start, end, smallRooms, mediumRooms, largeRooms);
		    	 }
			    //Print
			    if(command.equals("Print")){
			    	this.printVenue(sc.next());
			     }
			    //Cancel
			    if(command.equals("Cancel")){
			    	this.cancelReservation(sc.nextInt());
			     }
			    //Change
			    if(command.equals("Change")){
					int id = sc.nextInt();
					Calendar start = new GregorianCalendar(2016, months.get(sc.next()), sc.nextInt());
					Calendar end = new GregorianCalendar(2016, months.get(sc.next()), sc.nextInt());
					int largeRooms = 0;
					int mediumRooms = 0;
					int smallRooms = 0;
					while(sc.hasNextInt()){
						int number = sc.nextInt();
						String type = sc.next();
						if(type.equals("large")) largeRooms = number;
						if(type.equals("medium")) mediumRooms = number;
						if(type.equals("small")) smallRooms = number;
					}
					this.change(id, start, end, smallRooms, mediumRooms, largeRooms);
			     }	
		    }	
		  }
		  catch (FileNotFoundException e) {System.out.println("lmao");}
		  finally{if (sc != null) sc.close();}
	}
}
