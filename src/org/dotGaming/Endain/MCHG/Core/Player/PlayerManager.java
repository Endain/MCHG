package org.dotGaming.Endain.MCHG.Core.Player;

import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerManager {
	// Handles players as they join/leave. Handles their state/transitions.
	public Game g;
	
	public PlayerManager(Game g) {
		this.g = g;
	}
}
