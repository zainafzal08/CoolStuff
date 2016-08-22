
public class Rail {
	//fields
	City startCity;
	City endCity;
	int time;
	
	/**
	 * the constuctor just assigns the rail with a start city and end city and 
	 * defines how long the rail takes to travel along. 
	 * 
	 * @precondition all fields are uninitilized
	 * @postcondition all fields are initilized
	 * 
	 * @param start - start city for rail input
	 * @param end - end city for rail input
	 * @param timeI - time of rail journy input
	 */
	Rail(City start, City end, int timeI){
		startCity = start;
		endCity = end;
		time = timeI;
	}
	
	/**
	 * end of rail getter
	 * @param city
	 * @return the other end of the rail
	 */
	public City getOtherCity(City city){
		if(city.equals(startCity)) return endCity;
		if(city.equals(endCity)) return startCity;
		else return null;
	}
	
	/**
	 * rail cost getter
	 * @return time it takes to ride this rain. 
	 */
	public int getCost(){
		return time;
	}
}
