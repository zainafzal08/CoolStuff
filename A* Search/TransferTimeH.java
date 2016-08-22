	import java.util.ArrayList;
/**
 * TransferTimeH uses TripsLeftH's basic system but also includes the transfer cost of the cities
 * involved in each trip. special consideration is made to make sure the cost of the end/start cities 
 * is not included so this is still a underestimation is every circumstance. 
 * @author zainafzal
 * @performance
 * 3 city map with 1 trip: 4 nodes expanded in 0.0028 seconds
 * 3 city map with 3 trips: 22 nodes expanded in 0.0031
 * 5 city map with 2 trips: 19 nodes exanded in 0.0033 seconds
 * 5 city map with 4 trips(sample): 112 nodes expanded in 0.0050 seconds
 * 5 city map with 5 trips: 166 nodes expanded in 0.0061 seconds
 * 8 city map with 2 trips: 56 nodes expanded in 0.0046 seconds
 * 8 city map with 6 trips: 2028 nodes expanded in 0.054 seconds
 * 8 city map with 8 trips: 427 nodes expanded in 0.016 seconds
 * 9 city map with 7 trips: 14828 nodes expanded in 3.40 seconds
 * 9 city map with 2 trips: 72 nodes expanded in 0.005 seconds
 */
public class TransferTimeH implements Stratagy{
		@Override
		public int calculateH(State s) {
			int hCost = 0;
			ArrayList<Trip> tripsLeft = s.getTrips();
			ArrayList<Rail> rails = s.getCity().getRailConnections();
			boolean exit = false;
			City currentCity = s.getCity();
			for(Trip T : tripsLeft){
				hCost = hCost + T.getCost();
			}
			
			for(Rail r : rails){
				City otherCity = r.getOtherCity(currentCity);
				for(Trip T : tripsLeft){
					if(otherCity.equals(T.getEndCity()) && currentCity.equals(T.getStartCity())){
						if(tripsLeft.size() > 1) hCost = hCost + otherCity.getTransferTime();
						exit = true;
						break;
					}
				}
				if(exit) break;
			}
			return hCost;
		}
}
