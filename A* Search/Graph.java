import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
	//fields
	private ArrayList<City> cities;
	private ArrayList<Trip> neededTrips;
	/**
	 * the constructor initliased the cities arraylist.
	 * 
	 * @precondition cities is not initliased
	 * @postcondition cities is now an empty arrayList. 
	 */
	Graph(){
		cities = new ArrayList<City>();
		neededTrips = new ArrayList<Trip>();
	}
	
	/**
	 * creates a new city in the graph with name <code> name </code> and 
	 * a transfer time of <code> transferTime </code>
	 * 
	 * @precondition graph has been initilaised, transferTime is valid (>= 0) 
	 * @postcondition a new city mathcing the given name and transfer time exists in the graph
	 * @param transferTime
	 * @param name
	 */
	public void addCity(int transferTime, String name){
		cities.add(new City(transferTime, name));
	}
	
	/**
	 * creates a rail link between <code> city1 </code> and <code> city2 </code> that has a
	 * cost of <code> time </code>. It is bi directional and exists in both cities. 
	 * 
	 * @precondition graph has been initilaised, time is valid (>= 0), Both cities MUST already exist
	 * @postcondition a new rail with matching end cities and transfer time exists in both cities
	 * @param time
	 * @param city1
	 * @param city2
	 */
	public void addRail(int time, String city1Name, String city2Name){
		City city1 = getCity(city1Name);
		City city2 = getCity(city2Name);
		city1.addRailTo(city2, time);
		city2.addRailTo(city1, time);
	}
	/**
	 * Private function to find the city object corresponding to the name <code> cityName </code> 
	 * 
	 * @precondition city exists, cities array is initialized
	 * @postcondition the cities array is untouched
	 * @param cityName - name of the city ot be found
	 */
	private City getCity(String cityName){
		City city = null;
		for(City current : cities){
			if(current.equalsName(cityName)) city = current;
		}
		return city;
	}
	
	/**
	 * creates a uni directional trip from <code> city1 </code> and <code> city2 </code>
	 * 
	 * @precondition graph has been initilaised, both cities exist already and a connection exists between them.
	 * @postcondition the trip array now contains this trip array and is otherwise unaltered. 
	 * @param time
	 * @param city1
	 * @param city2
	 */
	public void addTrip(String city1Name, String city2Name){
		City city1 = getCity(city1Name);
		City city2 = getCity(city2Name);
		int cost = 0;
		//find cost
		ArrayList<Rail> railConnections = city1.getRailConnections();
		for(Rail r : railConnections){
			if(r.getOtherCity(city1).equals(city2)) cost = r.getCost();
		}
		
		neededTrips.add(new Trip(city1, city2, cost));
	}
	
	/**
	 * does the A* search using the heurstic defined in <code> searchStratagy </code>
	 * 
	 * @precondition State is comparable and is sorted by increasing fCost in the pQueue
	 * starting city is London and has 0 transfer cost. end City transfer cost is also 0
	 * @postcondition the nodes expanded, optimal cost and optimal journey are all printed to the screen, 
	 * no city object, rail object or trip object is altered.  
	 * 
	 */
	public void aStar(){
		//set up
		Queue<State> pQueue = new PriorityQueue<State>();
		ArrayList<State> visited = new ArrayList<State>();
		int costSoFar = 0;
		int nodesExpanded = 0;
		Stratagy searchStratagy = null;
		//choose a heuristic
		int cityCount = cities.size();
		int tripCount = neededTrips.size();
		//based off tests it seems as if tripsLeftH is a SLIGHTLY better choice in some cases 
		//for larger maps with less trips
		if(cityCount > 5 && tripCount < cityCount/2){
			searchStratagy = new TripsLeftH();
		}
		else{
			searchStratagy = new TransferTimeH();
		}
		//put in first state
		pQueue.add(new State(getCity("London"), 0, null, neededTrips, searchStratagy));
		//keep going while the pQueue has things in it
		while(!pQueue.isEmpty()){
			//pop
			State current = pQueue.poll();
			nodesExpanded++;
			//are we done?
			if (current.didCompleteAllTrip()){ //will check and handle the consequence
				current.printOutPath(nodesExpanded);
				break;
			}
			else if(visited.contains(current)){ 
				//do nothing
			}else{
				//grab all neighbours and put them in
				visited.add(current);
				ArrayList<Rail> connections = current.getCity().getRailConnections();
				for(Rail currRail : connections){
					City otherCity = currRail.getOtherCity(current.getCity());
					costSoFar = current.getCostSoFar() + currRail.getCost() + otherCity.getTransferTime();
					//see if this state is worth putting in.
					State toAdd = new State(otherCity, costSoFar, current, current.getTrips(), searchStratagy);
					toAdd.updateTrips(); //force a check
					boolean shouldAdd = true;
					boolean shouldDelete = false;
					State toDelete = null;
					for(State state : pQueue){
						if(state.getCity().equals(otherCity) && state.getTrips().containsAll(toAdd.getTrips())){
							if(state.getCostSoFar() > toAdd.getCostSoFar()){
								shouldAdd = true;
								shouldDelete = true;
								toDelete = state;
							}
							if(state.getCostSoFar() <= toAdd.getCostSoFar()){
								shouldAdd = false;
								shouldDelete = false;
							}
						}
					}
					//perform the relevent logic
					if(shouldAdd) pQueue.add(toAdd);
					if(shouldDelete) pQueue.remove(toDelete);
				}
			}
		}
	}
}
