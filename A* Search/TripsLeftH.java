import java.util.ArrayList;
/**
 * tripsLeftH uses how many trips a state has left to complete and the cost
 * of those trips to estimate how much each state will cost to get to the end. 
 * this never produces an overestimation because in every path there MUST be the given
 * trips traversed. 
 * @author zainafzal
 * @performance
 * 3 city map with 1 trip: 4 nodes expanded in 0.0028 seconds
 * 3 city map with 3 trips: 24 nodes expanded in 0.0029 seconds
 * 5 city map with 2 trips: 20 nodes expanded in 0.0031 seconds
 * 5 city map with 4 trips (sample): 133 nodes expanded in 0.0049 seconds
 * 5 city map with 5 trips: 232 nodes expanded in 0.0064 seconds
 * 8 city map with 2 trips: 54 nodes expanded in 0.0040 seconds
 * 8 city map with 6 trips: 2385 nodes expanded in 0.072 seconds
 * 8 city map with 8 trips: 599 nodes expanded in 0.02 seconds
 * 9 city map with 7 trips: 17825 nodes expanded in 5.67 seconds
 * 9 city map with 2 trips: 75 nodes expanded in 0.0047 seconds
 */
class TripsLeftH implements Stratagy{

	@Override
	public int calculateH(State s) {
		int hCost = 0;
		ArrayList<Trip> tripsLeft = s.getTrips();
		for(Trip T : tripsLeft){
			hCost = hCost + T.getCost();
		}
		return hCost;
	}

}
