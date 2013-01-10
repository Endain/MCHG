package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.event.Listener;
import org.dotGaming.Endain.MCHG.Core.Game;

//Contains events relating to environmental changes.
public class BlockListener implements Listener {
	private Game g;
	
	public BlockListener(Game g) {
		this.g = g;
	}
}
