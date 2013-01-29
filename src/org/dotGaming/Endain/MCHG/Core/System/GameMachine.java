package org.dotGaming.Endain.MCHG.Core.System;

import org.dotGaming.Endain.MCHG.Core.Game;

//Handles tracking and changing between game states.
public class GameMachine {
	private Game g;
	private int state;
	private String motd;
	// State list:
	// 0 - Initializing
	// 1 - Grace period (new players only)
	// 2 - Voting
	// 3 - District selection
	// 4 - Count down
	// 5 - Game
	// 6 - Deathmatch
	// 7 - Victory
	
	public GameMachine(Game g) {
		this.g = g;
		this.state = 0;
		this.motd = "[STARTUP] Plugin is initializing!";
	}
	
	public int getState() {
		return state;
	}
	
	public void doneInitializing() {
		if(state == 0) {
			state = 1;
			// Schedule moving out of grace period in 10 seconds
			g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Grace(), 200);
			// Change the servers list message
			motd = "[LOBBY] New players only!";
		}
	}
	
	private void doneGrace() {
		if(state == 1) {
			state = 2;
			// Change the servers list message
			motd = "[LOBBY] All players welcome!";
			// Start the voting sequence
			g.vm.startVote();
		}
	}
	
	public void doneVoting() {
		if(state == 2) {
			state = 3;
			// Change the servers list message
			motd = "[PREGAME] Players are selecting districts!";
			// Set map to the winning map
			g.mm.chooseMap(g.vm.getWinningMap()); 
			// Open district selection
			g.tm.open();
		}
	}
	
	public void doneDistricting() {
		if(state == 3) {
			state = 4;
			// Change the servers list message
			motd = "[PREGAME] Waiting for game to start!";
			// TODO
		}
	}
	
	public void doneCountdown() {
		if(state == 4) {
			state = 5;
			// Change the servers list message
			motd = "[GAME] Server open for spectators!";
			// TODO
		}
	}
	
	public void kill() {
		// TODO
	}
	
	public String getMOTD() {
		return motd;
	}
	
	class Grace implements Runnable {
		@Override
		public void run() {
			// Move out of grace period
			doneGrace();
		}
		
	}
}
