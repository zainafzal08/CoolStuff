
public class Trip {
	//fields
	private City startCity;
	private City endCity;
	private int cost;
	/**
	 * sets up a trip form <code>city1</code> to <code>city2</code>
	 * 
	 * @precondition startCity, cost and endCity are uninitialized
	 * @postcondition startCity, cost and endCity now correspond to <code> city1 </code>, <code> costInput </code> and <code> city2 </code>  
	 * @param startCityInput - start city
	 * @param endCityInput - end city
	 */
	public Trip(City startCityInput, City endCityInput, int costInput) {
		startCity = startCityInput;
		endCity = endCityInput;
		cost = costInput;
	}
	
	/**
	 * start city getter
	 * @return start city obejct
	 */
	public City getStartCity(){
		return startCity;
	}
	/**
	 * end city getter
	 * @return end city object
	 */
	public City getEndCity(){
		return endCity;
	}
	/**
	 * cost getter
	 * @return cost
	 */
	public int getCost(){
		return cost;
	}
	
}
