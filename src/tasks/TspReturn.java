package tasks;
import java.io.Serializable;
import java.util.ArrayList;  

public class TspReturn implements Serializable{

    static final long serialVersionUID = 227L; // Was missing 
	private double sumPathLength;
	private ArrayList<Integer> path;

    /**
     * 
     * @param taskCoordX where in the complete picture the sub-picture will be placed on the x axis. 
     * @param taskCoordY where in the complete picture the sub-picture will be placed on the y axis.
     * @param MandelbrotResults describes the number of iterations for each pixel in the array that each task returns. This will make up a small part of the complete picture.
     */
    public TspReturn( ArrayList<Integer> path, double sumPathLength) 
    {
    	this.sumPathLength = sumPathLength;
    	this.path = path;
    }

    public  ArrayList<Integer> getPath() { return path; }
    
    public double getSumPathLength(){ return sumPathLength;}

    

}