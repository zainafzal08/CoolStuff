import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class TripPlanner {
	//fields
	private Graph map;
	/**
	 * The constructor will inilise the graph. 
	 * 
	 * @precondition map is uninitilized
	 * @postcondition map is now set to be a new graph object. 
	 */
	TripPlanner(){
		map = new Graph();
	}
	
    /**
     * main initlaises a TripPlanner object
     * and calls the readINput method on whatever file is given in as argument one
     * @param args - arguments given from command line
     * 
     * @precondition args[0] contains a string
     * @postcondition if possible, file sepcifed in args[0] is opened and all valid commands read in 
     * and processed
     */
	public static void main(String[] args){
		Long startTime = System.nanoTime();
		TripPlanner system = new TripPlanner();
		system.readInput(args[0]);
		Long finalTime = System.nanoTime();
		Long total = finalTime - startTime;
		System.out.println();
		System.out.println("time taken: " + ((double)total/10000000000.0));
	}

	/**
	 * run will take in a fileName and using a Scanner object read in data from the file 
	 * if the file can not be opened or found, this method will exit with no output
	 * if the file is successfully opened for reading, every command in the file is identifed and the relevent
	 * input data is parsed into the graph. once all data is read in, the a* search is triggered.
	 * @param fileName - name of file to be read from
	 * @precondition input file is formatted correctly
	 * @postconditons the input is parsed to the relevent functions in the correct form to be accepted by methods. 
	 */
	private void readInput(String fileName) {
		Scanner sc = null;
		try{
			sc = new Scanner(new FileReader(fileName));
		    String command;
		    while(sc.hasNext()){
		    	command = sc.next();
		    	if(command.equals("Transfer")){
		    		int time = sc.nextInt();
		    		String name = sc.next();
		    		map.addCity(time, name);
		    	}
		    	else if(command.equals("Time")){
		    		int time = sc.nextInt();
		    		String start = sc.next();
		    		String end = sc.next();
		    		map.addRail(time, start, end);
		    	}
		    	else if(command.equals("Trip")){
		    		String start = sc.next();
		    		String end = sc.next();
		    		map.addTrip(start, end);
		    	}
		    }	
		    //all data read in, time to search
		    map.aStar();
		 }
		 catch (FileNotFoundException e) {}
		 finally{if (sc != null) sc.close();}
	}
}
