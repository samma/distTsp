package api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DAC {
	public Object[] args;
	public Task spawn_next;
	public int spawn_nextJoin;
	public LinkedList spawn = new LinkedList();
	
	public Object send_argument;
	
	protected <T> void spawn(Task<T> t){
		spawn.add(t);
	}
	protected <T> void spawn_next(Task<T> t, int joinCounter){
		spawn_next = t;
		spawn_nextJoin = joinCounter;
	}
	protected void send_argument(Object value){
		send_argument = value;
	}
	//public void execute(){}
	
//	public WorkerResult start(){
//		this.execute();
//		return new WorkerResult(spawn, spawnArgs, spawn_next, spawn_nextArgs, send_argument);
//	}
}