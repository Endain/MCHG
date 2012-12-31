package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.event.Listener;
import org.dotGaming.Endain.MCHG.Core.Game;

public class BlockListener implements Listener {
	// Contains events relating to environmental changes.
	public Game g;
	
	public BlockListener(Game g) {
		this.g = g;
	}
}
