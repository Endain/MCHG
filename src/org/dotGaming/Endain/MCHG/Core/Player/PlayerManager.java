package org.dotGaming.Endain.MCHG.Core.Player;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

//Handles players as they join/leave. Handles their state/transitions/interactions.
public class PlayerManager {
	private Game g;
	private HashMap<String, Tribute> citizens;
	private HashMap<String, Tribute> tributes;
	private HashMap<String, Tribute> spectators;
	
	public PlayerManager(Game g) {
		this.g = g;
		citizens = new HashMap<String, Tribute>();
		tributes = new HashMap<String, Tribute>();
		spectators = new HashMap<String, Tribute>();
	}
	
	public boolean canConnect() {
		int state = g.gm.getState();
		// 0 - plugin starting up
		// ... add more in the future
		if(state == 0)
			return false;
		return true;
	}
	
	public void addPlayer(Player p) {
		// First ensure that the player does not already exist
		removePlayer(p);
		// Add player to specific list based on state
		Tribute t = new Tribute(p);
		if(g.gm.getState() >= 0 && g.gm.getState() <= 0) {
			// Pre-game states
			citizens.put(p.getName(), t);
			t.initCitizen();
		} else if(g.gm.getState() >= 0 && g.gm.getState() <= 0) {
			// Mid-game states
			spectators.put(p.getName(), t);
			t.initSpectator();
		}
	}
	
	public void removePlayer(Player p) {
		// Remove a player from the player manager
		if(p != null) {
			// Remove from lists
			if(citizens.containsKey(p.getName()))
				citizens.remove(p.getName());
			if(tributes.containsKey(p.getName()))
				tributes.remove(p.getName());
			if(spectators.containsKey(p.getName()))
				spectators.remove(p.getName());
		}
	}
	
	public Tribute getTribute(Player p) {
		if(citizens.containsKey(p.getName()))
			return citizens.get(p.getName());
		if(tributes.containsKey(p.getName()))
			return tributes.get(p.getName());
		if(spectators.containsKey(p.getName()))
			return tributes.get(p.getName());
		return null;
	}
	
	public boolean isCitizen(Player p) {
		if(citizens.containsKey(p.getName()))
			return true;
		return false;
	}
	
	public boolean isTribute(Player p) {
		if(tributes.containsKey(p.getName()))
			return true;
		return false;
	}
	
	public boolean isSpectator(Player p) {
		if(spectators.containsKey(p.getName()))
			return true;
		return false;
	}
}
