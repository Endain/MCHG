package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.event.Listener;
import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerListener implements Listener {
	// Contains events relating to player actions, excluding combat.
	public Game g;
	
	public PlayerListener(Game g) {
		this.g = g;
	}
}
