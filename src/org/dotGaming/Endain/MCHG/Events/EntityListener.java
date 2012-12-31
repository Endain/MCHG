package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.event.Listener;
import org.dotGaming.Endain.MCHG.Core.Game;

public class EntityListener implements Listener {
	// Contains events relating to entity changes, such as damage.
	public Game g;
	
	public EntityListener(Game g) {
		this.g = g;
	}
}
