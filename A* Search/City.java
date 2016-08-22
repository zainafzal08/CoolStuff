import java.util.ArrayList;

public class City {
	//fields
	private int transferTime;
	private String name;
	private ArrayList<Rail> rails;
	
	/**
	 * the constructor just sets up the rails ArrayList and sets the transfertime and name to
	 * <code> transTimeINput </code> and <code> nameInput </code>
	 * @precondition <code> rails </code> should be un-initilized
	 * @postcondition <code> rails </code> should now be initilized and empty.
	 * @param transTimeInput - transfer time
	 * @param nameINput - name
	 */
	City(int transTime, String nameInput){
		rails = new ArrayList<Rail>();
		transferTime = transTime;
		name = nameInput;
	}
	/**
	 * adds a rail from the current city to the specifed <code> otherCity </code>
	 * 
	 * @precondition no rail exists between this city and otherCity, time is >=0
	 * @postcontion a rail now exists between this city and otherCity with a time value 
	 * equal to that of the input <code> time </code>
	 * @param otherCity
	 * @param time
	 */
	public void addRailTo(City otherCity, int time){
		rails.add(new Rail(this, otherCity, time));
	}

	/**
	 * name getter
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * tranfer time getter
	 * @return transfer time
	 */
	public int getTransferTime(){
		return transferTime;
	}
	
	/**
	 * rails getter
	 * @return rail connections
	 */
	public ArrayList<Rail> getRailConnections(){
		return rails;
	}
	
	/**
	 * returns true of other city has equal name and transfer time
	 * @param otherCity - the other city object
	 * @return
	 */
	public boolean equals(City otherCity){
		if(otherCity.getName() == name && otherCity.getTransferTime() == transferTime) return true;
		else return false;
	}
	
	/**
	 * returns true of other city has an equal name field to this object. 
	 * @param cityName - name of other city
	 * @return
	 */
	public boolean equalsName(String cityName){
		if(cityName.equals(name)) return true;
		else return false;
	}
	
}
