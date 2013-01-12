package org.dotGaming.Endain.MCHG.Core;

//Handles tracking and changing between game states.
public class GameMachine {
	private Game g;
	private int state;
	// State list:
	// 0 - Initializing
	
	public GameMachine(Game g) {
		this.g = g;
		this.state = 0;
	}
	
	public int getState() {
		return state;
	}
	
	public void doneInitializing() {
		if(state == 0)
			state = 1;
	}
	
	public void kill() {
		// TODO
	}
}
