package gamedata;

import java.util.LinkedList;
import java.util.Queue;

import states.State;

public class StateMachine {
	private Queue<State> States;
	private boolean Adding,Replacing,Removing;
	private State NextState ;
	
	public StateMachine()
	{
		States = new LinkedList<State>();
		Adding = false;
		Replacing = false;
		Removing = false ;
	}
	public void  AddState(State newstate,boolean replace)
	{
		NextState =newstate;
		Replacing = replace;
		Adding = true;
	}
	public void  AddState(State newstate)
	{
		NextState =newstate;
		Replacing = true;
		Adding = true;
	}
	public void RemoveState()
	{
		Removing = true;
	}
	public boolean ProcessStateChanges()
	{
		if(Removing)
		{
			States.poll();
			Removing = false;
			if(States.size()>0)
			{
				States.peek().resume();
			}
		}
		if(Adding)
		{
			if(Replacing)
			{
				States.poll();
				Replacing = false;
			}
			else
			{
				if(States.size()>0)
				{
					States.peek().pause();
				}
			}
			States.add(NextState);
			Adding = false;
			NextState = null;
			return true;
		}
		return false;
	}
	public State CurrentState()
	{
		return States.peek();
	}


}
