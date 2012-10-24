package tasks;
import java.io.Serializable;
public class TspInputArg implements Serializable{

    static final long serialVersionUID = 227L; // Was missing 
    double[][] distances; 
    int [] subRoute;
    double subRouteLen;

    
    public TspInputArg( int [] subRoute, double [][] distances, double subRouteLen)
    {
    	this.distances = distances;
    	this.subRoute = subRoute;
    	this.subRouteLen = subRouteLen;
    }

    public int [] getSubRoute() { return subRoute; }
    
    public double [][] getDistances(){ return distances;}
    
    public double getSubRouteLen() {return subRouteLen;}

    

}