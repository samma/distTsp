package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import api.DAC;
import api.Space;
import tasks.FibTask;

public class ClientImpl {

	public static void main(String[] args) {
		if (System.getSecurityManager() == null ){ 
			   System.setSecurityManager(new java.rmi.RMISecurityManager() ); 
			}
			try{
				String name = "Space";
	    		Registry registry = LocateRegistry.getRegistry(args[0]);
	    		Space space = (Space) registry.lookup(name);
	    		int [][] count = null;
	    		System.out.println("Starting job...");
	    		FibTask fibTask = new FibTask();
	    		System.out.println("Step 1");
	    		FibTask.Fib fib = fibTask.new Fib(10);
	    		System.out.println("Step 2");
	    		System.out.println("Step 3");
	    		space.put(fib);
	    		int result = (Integer) space.take();
	    		System.out.println("The result is: " + result);
			}catch (Exception e) {
				System.err.println("fib client exception:");
				e.printStackTrace();
			}
	}

}
