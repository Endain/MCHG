package org.dotGaming.Endain.MCHG.Core;

public class GameMachine {
	// Handles tracking and changing between game states.
	public Game g;
	public int state;
	
	public GameMachine(Game g) {
		this.g = g;
		this.state = 0;
	}
}
