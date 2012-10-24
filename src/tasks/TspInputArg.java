package tasks;
import java.io.Serializable;
import java.util.ArrayList;
public class TspInputArg implements Serializable{

    static final long serialVersionUID = 227L; // Was missing 
    double[][] distances; 
	private ArrayList<Integer> path;
	private double sumPathLength;
	private ArrayList<Integer> allTowns;
	private int levelToSplitAt;

    
    public TspInputArg( ArrayList<Integer> path, double [][] distances, double sumPathLength, ArrayList<Integer> allTowns, int levelToSplitAt)
    {
    	this.distances = distances;
    	this.path = path;
    	this.sumPathLength = sumPathLength;
    	this.allTowns = allTowns;
    	this.levelToSplitAt = levelToSplitAt;
    	
    }

    public ArrayList<Integer> getPath() { return path; }
    
    public double [][] getDistances(){ return distances;}
    
    public double getSumPathLength() {return sumPathLength;}
    
    public ArrayList<Integer> getAllTowns(){ return allTowns; }
    
    public int getLevelToSplitAt() {return levelToSplitAt; }

    

}