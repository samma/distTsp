package tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;

import api.DAC;
import api.Task;

import permutation.PermUtil;
import tasks.TspReturn;
import tasks.FibTask.Fib;
import tasks.FibTask.Sum;

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
	private static final long serialVersionUID = 227L;		
	public double currentShortestPathLength = 1000000;
	public ArrayList<Integer> currentShortestPath = new ArrayList<Integer>();

	
	
	public class TspExplorer extends DAC implements Task, Serializable{
		private static final long serialVersionUID = 227L;		
		public Object [] args;
		public TspExplorer(Object... args){this.args = args;}
		public TspExplorer(){}
	
	    public ArrayList<Integer> path;
	    public double [][] distances;
	    public double sumPathLength;
	    public ArrayList<Integer> allTowns;
	    public int levelToSplitAt;
	    

		
		public Object execute() {
			//System.out.println("Tsp explore execute");
			TspInputArg in = (TspInputArg)args[0];

			path = in.getPath();
		    distances = in.getDistances();
		    sumPathLength = in.getSumPathLength() ;
		    allTowns = in.getAllTowns();
		    levelToSplitAt = in.getLevelToSplitAt() ;    
		    
		    if (path.size() < levelToSplitAt){
		    	//Explore more of the tree, that is add more elements to path and ant split the task up. 
		    	//Also add the traversed Length so far
		    		    	
				int i = 0;

				//for every child on path, that is every town except those visited on the path so far				
				for (Integer town : allTowns){
					if (!path.contains(town)){
						ArrayList<Integer> newPath = new ArrayList<Integer>();
						newPath.addAll(path);
						newPath.add(town);	    	

						double newSumPath = sumPathLength+(distances[path.get(path.size()-1)][newPath.get(newPath.size()-1)]);  //distance between the next town to visit and the previous one
						//System.out.println("newPath" +newPath+" with length " + newSumPath);	
						spawn(new TspExplorer((Object)new TspInputArg(newPath, distances, newSumPath, allTowns ,levelToSplitAt)));
						i++;
					}
				}
				spawn_next(new TspComposer(), i); //check the number of spawns, prob right
				i=0;

		    	
		    }
		    else {
		    	TspReturn lol = localTsp(in);
		    	
		    	
		    	double sumPathLengthX = lol.getSumPathLength();
		    	ArrayList<Integer> pathX = new ArrayList<Integer>(lol.getPath());
		    	
		    	TspReturn res = new TspReturn(pathX,sumPathLengthX);
		    	
				if (res.getSumPathLength() < 10000){

					send_argument(res);
				}
		    	//A subtree is reached
		    	//Explore the rest of the towns locally, that is those not in path
		    	//Calculate the shortest path of those to visit
		    	//remember to add distance back to town 0
		    }
		    

			
			

			return null;
		}
		public TspReturn localTsp(TspInputArg inn){
			
		    ArrayList<Integer> path = inn.getPath();
		    double [][] distances = inn.getDistances();
		    double sumPathLength = inn.getSumPathLength();
		    ArrayList<Integer> allTowns = inn.getAllTowns();
			
		
			if (path.size() < distances.length){
				
				//for every child on path, that is every town except those visited on the path so far				
				for (Integer town : allTowns){
					if (!path.contains(town)){
						ArrayList<Integer> newPath = new ArrayList<Integer>();
						newPath.addAll(path);
						newPath.add(town);	    	

						double newSumPath = sumPathLength+(distances[path.get(path.size()-1)][newPath.get(newPath.size()-1)]);  //distance between the next town to visit and the previous one
						//System.out.println("newPath" +newPath+" with length " + newSumPath);	
						TspExplorer localTask = new TspExplorer((Object)new TspInputArg(newPath, distances, newSumPath, allTowns ,levelToSplitAt));
						
						localTask.execute();
						

						//return new TspReturn(currentShortestPath, currentShortestPathLength);
					}
				}				
			}
			else{
				sumPathLength += (distances[path.get(path.size()-1)][0]); //adding the length back to town -
				
				if (sumPathLength < currentShortestPathLength){
					currentShortestPathLength = sumPathLength;
					currentShortestPath = path;
				}
			}			
			return new TspReturn(currentShortestPath, currentShortestPathLength);
		}
	}

	
	
	
	public class TspComposer extends DAC implements Task, Serializable{
		private static final long serialVersionUID = 227L;		
		public TspComposer(Object ... args){this.args = args;}
		public TspComposer(){}
		
		public Object execute(){
			//System.out.println("Tsp compose execute");
			
			TspReturn inputVal;
			ArrayList<Integer> currentShortestPath = new ArrayList<Integer>();
			double currentShortestPathLength = 1000000;
			
			for (int i = 0; i < args.length ; i++){
				inputVal = (TspReturn)args[i];
				if (inputVal.getSumPathLength() < currentShortestPathLength){
					currentShortestPathLength = inputVal.getSumPathLength();
					currentShortestPath = inputVal.getPath();
				}
			}
			
			TspReturn ret = new TspReturn(currentShortestPath,currentShortestPathLength);
			send_argument(ret);
			return null;
		}
	}
}

/*
s



*/