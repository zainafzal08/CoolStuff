
public interface Stratagy {
	/**
	 * calculate the hCost for a given State
	 * @param s - state which needs it's hCost calculated
	 * @return hCost
	 */
	public int calculateH(State s);
}
