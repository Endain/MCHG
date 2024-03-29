package org.dotGaming.Endain.MCHG.Core.System;

import org.dotGaming.Endain.Chronos.Chronos;
import org.dotGaming.Endain.MCHG.Core.Game;

//Handles tracking and changing between game states.
public class GameMachine {
	private Game g;
	private int state;
	private String motd;
	// State list:
	// -1 - Crashed/Error state 
	// 0  - Initializing
	// 1  - Grace period (new players only)
	// 2  - Voting
	// 3  - District selection
	// 4  - Count down
	// 5  - Game
	// 6  - Deathmatch
	// 7  - Victory
	
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
			// Load chests and spawns for the selected map
			g.cm.loadMap();
			g.sm.loadMap();
			// Open district selection
			g.dim.open();
		}
	}
	
	public void doneDistricting() {
		if(state == 3) {
			state = 4;
			// Change the servers list message
			motd = "[PREGAME] Waiting for game to start!";
			// Reset the map time and the time speed
			Chronos.setTime(1.5);
			Chronos.setTicks(0);
			// Enter the count down sequence
			g.cdm.waitForSync();
		}
	}
	
	public void doneCountdown() {
		if(state == 4) {
			state = 5;
			// Change the servers list message
			motd = "[GAME] Server open for spectators!";
			// Schedule chest refills
			g.mm.scheduleRefills();
			g.mm.scheduleEvent();
		}
	}
	
	public void kill() {
		// Set to crashed state
		state = -1;
		// Change the servers list message
		motd = "[ERROR] Please notify admin(s)!";
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
