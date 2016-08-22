import java.util.ArrayList;

public class State implements Comparable<State>{
	//fields
	private State prevState;
	private City currentCity;
	private int gCost;
	private ArrayList<Trip> tripsLeft;
	private Stratagy strat;
	
	/**
	 * Constructor for a new state
	 * 
	 * @precondition cCity is a valid city object, trips is non-empty, costSoFar is >=0
	 * @postcondition tripsLeft is now initliased and holds the same content as trips. all fields
	 * are initlized. 
	 * @param currentCity -the current city
	 * @param costSoFar - the cost to this city so far (including transfer times)
	 * @param pState -the previous state
	 */
	public State(City cCity, int costSoFar,  State pState, ArrayList<Trip> trips, Stratagy stratagy) {
		currentCity = cCity;
		gCost = costSoFar;
		prevState = pState;
		tripsLeft = new ArrayList<Trip>();
		tripsLeft.addAll(trips);
		strat = stratagy;
	}
	
	/**
	 * city getter
	 * @return city
	 */
	public City getCity(){
		return currentCity;
	}
	
	/**
	 * previous city getter
	 * @return previous city
	 */
	public City getPreviousCity(){
		if(prevState == null) return null;
		return prevState.getCity();
	}
	
	
	/**
	 * cost so far getter
	 * 
	 * @return cost to far
	 */
	public int getCostSoFar(){
		return gCost;
	}
	
	/**
	 * f(x) calculator
	 * where f(x) = totalCost of trip + current nodes h(x) 
	 * @return f(x)
	 */
	public int getFCost(){
		return gCost + strat.calculateH(this);
	}
	
	/**
	 * trips array getter
	 * @return tripsLeft array
	 */
	public ArrayList<Trip> getTrips(){
		return tripsLeft;
	}
	
	/**
	 * returns if all the trips are complete or not
	 * @return true if the state path has completed a trip, false otherwise
	 */
	public boolean didCompleteAllTrip(){
		//check if the trips array is empty or not
		return tripsLeft.isEmpty();
	}
	/**
	 * checks to see if this state path has just completed a trip, 
	 * if so it removes the trips from tripsLeft
	 * @precondition tripsLeft is non empty and intilized
	 * @postcondition tripsLeft is unaltered if no trip has been traversed. if a trip has been traversed
	 * then the trip has been removed from the tripsLeft arrayList. 
	 * 
	 */
	public void updateTrips(){
		//first check is any trip has just been traversed
		Trip toRemove = null;
		for(Trip trip : tripsLeft){
			if(currentCity.equals(trip.getEndCity()) && prevState.getCity().equals(trip.getStartCity())){
				//we did! set it up to be removed from the list
				toRemove = trip;
				break;
			}
		}
		//if need be, remove from the list. 
		if(toRemove != null){
			tripsLeft.remove(toRemove);
		}
		//if we just removed the last trip, remove the final cities transfer cost
		if(tripsLeft.isEmpty()) removeLastTransfer(); 
	}

	/**
	 * compareTo is used by the pQueue to order the states. 
	 * @returns <0 if given state is larger then current state
	 * @returns >0 if given state is smaller then current state
	 * @returns 0 if states are equal
	 */
	@Override
	public int compareTo(State o) {
		return getFCost() - o.getFCost();
	}
	
	/**
	 * prints out path this state took and adds on the number
	 * of nodes expanded and total cost. 
	 * 
	 * @precondition total cost and nodes are initilized
	 * @postcondition a representation of this state's journy is printed to the screen with
	 * the total journy cost and number of nodes expanded in the process. 
	 * @param nodes - total nodes expanded
	 */
	public void printOutPath(int nodes){
		System.out.println(nodes + " nodes expanded");
		System.out.println("cost = " + gCost);
		String output = "";
		output = recurrsivePathTrace(output);
		String[] outputArray = output.split(" ");
		int size = outputArray.length;
		for(int i = 1; i+1 < size; i++){
			System.out.println("Trip " + outputArray[i] +" to " + outputArray[i+1]);
		}
	}
	
	/**
	 * removes the transfer cost of the current city, used when the a* search
	 * is over, and thus the final city has 0 transfer time. 
	 * 
	 * @precondition totalCost and currentCity are initilized. 
	 * @postcondition totalCost is now = totalCost - currentCity transfer time
	 */
	public void removeLastTransfer(){
		gCost = gCost - currentCity.getTransferTime();
	}
	
	/**
	 * recurrsivly traces through the state and generates a string outlining the path taken
	 * @precondition currentCity in this state is initilized
	 * @postcondition all states are unaltered and a string is generated outling the cities
	 * taken on the current state path seperated with a arrow. this is appended onto the given
	 * string and does not override it
	 * @param s - string to add on the journey to
	 * @return the String with appended information from the current state. 
	 */
	public String recurrsivePathTrace(String s){
		if(prevState != null){
			s = prevState.recurrsivePathTrace(s);
		}
		s = s + " " + currentCity.getName();
		return s;
	}
	/**
	 * returns if a given state <code> otherState </code> is equal to this state
	 * @param otherState
	 * @return true if both states have the same currentCity and the same trips left to traverse, false otherwise
	 */
	public boolean equals(State otherState){
		boolean equal = true;
		if(!currentCity.equals(otherState.getCity())) equal = false;
		if(!tripsLeft.containsAll(otherState.getTrips())) equal = false;
		return equal;
	}

}
