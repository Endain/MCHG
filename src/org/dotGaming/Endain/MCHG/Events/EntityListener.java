package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.event.Listener;
import org.dotGaming.Endain.MCHG.Core.Game;

//Contains events relating to entity changes, such as damage.
public class EntityListener implements Listener {
	private Game g;
	
	public EntityListener(Game g) {
		this.g = g;
	}
}
