package tasks;

import java.io.Serializable;

import system.Continuation;
import api.DAC;
import api.Task;

public class FibTask implements Serializable{
	
	public class Fib extends DAC implements Task, Serializable{
		public Object [] args;
		public Fib(Object... args){this.args = args;}
		public Fib(){}
		
		public Object execute(){
			System.out.println("Fib");
			int n = (Integer) args[0];
			if (n < 2) {
				send_argument(n);
			}else{
				spawn_next(new Sum(), 2);
				spawn(new Fib(n-1));
				spawn(new Fib(n-2));
			}
			return null;
		}
		
	}
			
	public class Sum extends DAC implements Task, Serializable{
		//public Object [] args;
		//public Sum(Object... args){this.args = args;}
		public Sum(){}
		public Object execute(){
			int x = (Integer)(args[0]);
			int y = (Integer)(args[1]);
			send_argument( x + y );
			System.out.println("Sum is:  " + (x+y));
			return null;
		}
		
	}	
}

