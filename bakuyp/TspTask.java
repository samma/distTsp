package tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;

import api.DAC;
import api.Task;

import permutation.PermUtil;
import tasks.TspReturn;

/**
 * This class implements a Traveling Salesman Problem solver as a task
 * which fits into the RMI framework implemented in the API.
 * 
 * The solver simply brute forces and finds all possible routes in both 
 * directions and then evaluates them all to find the most efficient one.
 * 
 * @author torgel
 *
 */

public class TspTask implements Serializable{
	public static final int sizeOfSmallestTree = 15;


	public class TspExplorer extends DAC implements Task, Serializable{
		
		public Object [] args;
		public TspExplorer(Object... args){this.args = args;}
		public TspExplorer(){}
		
		//private static final long serialVersionUID = 227L;
		public int [] subRouteTemp;
		public double [][] distances;
		public double subRouteLen;
		

		/**
		 * @param towns is the input sequence of the towns in the coordinate system. 
		 * takes a double[][] cities that codes the x and y coordinates of city[i]: 
		 * cities[i][0] is the x-coordinate of city[i] and cities[i][1] is the 
		 * y-coordinate of city[i]. 
		 * @param subRoute is the beginning of the subset of routes that will be searched in
		 * @param distances is the distances between each pair of towns
		 */
		/*public TspExplorer(TspInputArg input) {
			this.subRoute = input.getSubRoute();
			this.distances = input.getDistances();

		}*/

		/**
		 * Starts the TSP computation
		 * 
		 * @return an int[] tour that lists the order of the cities of a minimal distance tour. 
		 */
		public Object execute() {
			System.out.println("Tsp explore execute");
			TspInputArg in = (TspInputArg)args[0];

			subRouteTemp = in.getSubRoute();
			
			distances = in.getDistances();
			
			subRouteLen = in.getSubRouteLen();
			
			Integer [] subRoute= new Integer[subRouteTemp.length];
			for (int i = 0 ; i < subRouteTemp.length ; i++){
				subRoute[i] = subRouteTemp[i];
			}
			
			//How far will the splitting of the tree go
			int numTownsInSubTree = distances.length - subRoute.length;
			System.out.println("num towns "+numTownsInSubTree);
			if (  numTownsInSubTree > sizeOfSmallestTree){
				//TODO split the tree

				int [] townsToExplore = new int [numTownsInSubTree];
				Object [] tempStorage = new Object [numTownsInSubTree];
				
				
				//Removes to subRoutes from the set of routes, the result is the routes we want to explore
				ArrayList<Integer> townsToExploreList = new ArrayList<Integer>();
				for (int j = 0;j<distances.length;j++){
					if (!Arrays.asList(subRoute).contains((int)j)){
						townsToExploreList.add(j);
						
					}
				}
			
				//More stupid type conversion
				townsToExploreList.toArray(tempStorage);
				for (int i = 0;i<(townsToExplore.length);i++){				
					townsToExplore[i] = (Integer)tempStorage[i];
				}

				spawn_next(new TspComposer(), numTownsInSubTree);
				
			
				//even more conversion
				int [] subRouteTemp2 = new int[subRouteTemp.length];
				for (int i = 0 ; i < subRouteTemp.length ; i++){
					subRouteTemp2[i] = (int)subRouteTemp[i];
				}

			
				
				for (int i = 0; i < numTownsInSubTree; i++ ){
					double outSubRouteLen = subRouteLen;
					int [] newSubRoute = new int [subRoute.length + 1];
					for(int j = 0; j < subRouteTemp2.length;j++){
						newSubRoute[j] = subRouteTemp2[j];
					}
					newSubRoute[newSubRoute.length-1] = townsToExplore[i];
					
					outSubRouteLen+=distances[newSubRoute[newSubRoute.length-1]][newSubRoute[newSubRoute.length-2]];
					

								
					spawn(new TspExplorer((Object)new TspInputArg(newSubRoute,distances,outSubRouteLen)) );

				}				
				
			}
			else {
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + subRouteTemp.length);
				send_argument(computeRoute(subRouteTemp,distances,subRouteLen));	
			}			
			return null;
		}


		/**
		 * Finds the shortest route, 1 to N, excludes towns found in subRoute
		 * 
		 * @param towns is the input sequence of the towns in the coordinate system
		 * @param subRoute is the beginning of the subset of routes that will be searched in
		 * @param distances is the distances between each pair of towns
		 * @return the sequence of towns to visit for the shortest route
		 */
		public TspReturn computeRoute(int [] subRouteIn, double [][] distances, double subRouteLen){
			int startTown;

			//Block for stupid type conversion
			Integer [] subRoute = new Integer[subRouteIn.length];
			for (int i = 0 ; i < subRoute.length ; i++){
				subRoute[i] = subRouteIn[i];
			}

			//The search should start in the last excluded town - startTown
			if (subRoute.length != 0){
				startTown = subRoute[subRoute.length-1];
				
			}
			else{
				startTown = 0;
			
			}
			Object [] tempStorage = new Object [(distances.length-subRoute.length)+1];
			int [] townsToExplore = new int [(distances.length-subRoute.length)+1];
			int [] tempTownStorage = new int [(distances.length-subRoute.length)+1];

			//Removes to subRoutes from the set of routes, the result is the routes we want to explore
			ArrayList<Integer> townsToExploreList = new ArrayList<Integer>();
			townsToExploreList.add(startTown);
			for (int j = 0;j<distances.length;j++){
				if (!Arrays.asList(subRoute).contains((int)j)){
					townsToExploreList.add(j);
				}
			}

			//More stupid type conversion
			townsToExploreList.toArray(tempStorage);
			for (int i = 0;i<(townsToExplore.length);i++){
				townsToExplore[i] = (Integer)tempStorage[i];

			}
			
			


			
			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" + townsToExplore.length);
			
			System.out.println();
			for ( int x = 0 ; x < townsToExplore.length;x++){
				System.out.print(" explore " + townsToExplore[x] );
			}
			System.out.println();
			
			//This is a type which can be called like permute_next to get another permutation
			PermUtil permute = new PermUtil(townsToExplore);

			//this next variable is infinite-ish
			double shortestRouteLength = 100000;
			double temp;
			int [] shortestRoute = tempTownStorage;
			for (int j = 0 ; j <  factorial(townsToExplore.length); j++){
				tempTownStorage = permute.next();
				temp = calcRouteLength(tempTownStorage,distances);
				if (temp < shortestRouteLength){
					shortestRouteLength = temp;
					shortestRoute = tempTownStorage;
				}
			}
			
			int [] finalRoute = new int [subRouteIn.length+shortestRoute.length];
			
			for (int i = 0; i < subRouteIn.length ; i++){
				finalRoute[i] = subRouteIn[i];
			}
			
			for (int i = 0; i < shortestRoute.length ; i++){
				finalRoute[i+subRouteIn.length] = shortestRoute[i];
			}
			
			shortestRouteLength+=subRouteLen;
			
			System.out.println("-------------------------------------------" + shortestRoute.length);
			
			TspReturn ret = new TspReturn(finalRoute,shortestRouteLength);
			
			return ret;

		}

		/**
		 * Calculates the length of one route.
		 * 
		 * @param route gives the sequence of towns to visits.
		 * @param distances gives the distances between each town
		 * @return
		 */
		public double calcRouteLength(int[] route, double[][] distances){
			double sum = 0;
			for (int i = 0;i<route.length-1;i++){
				sum += distances[route[i]][route[i+1]];
			}
			return sum;	
		}

		public int factorial(int n)
		{
			if (n == 0) return 1;
			return n * factorial(n-1);
		}


	}

	
	
	
	public class TspComposer extends DAC implements Task, Serializable{
		//private static final long serialVersionUID = 227L;		
		public TspComposer(Object ... args){this.args = args;}
		public TspComposer(){}
		
		public Object execute(){
			System.out.println("Tsp compose execute");
			double shortestRouteLength = 100000; //infinity
			int [] shortestRoute = {};

			
			//TODO compare the routes!			
			

			
			
			TspReturn tempReturn;
			for (int i = 0 ; i < args.length ; i++){
				tempReturn = (TspReturn)args[i];

				if (tempReturn.getRouteLength() < shortestRouteLength){
					System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR" + tempReturn.getRoute().length);
					shortestRouteLength = tempReturn.getRouteLength();
					shortestRoute = tempReturn.getRoute();
					
				}
			}
			
			
			TspReturn ret = new TspReturn(shortestRoute,shortestRouteLength);
			send_argument(ret);
			return null;
		}
		
	}


}