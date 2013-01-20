package org.dotGaming.Endain.MCHG.Core.Player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

//Encapsulates a Minecraft player and adds HG specific attributes and functions.
public class Tribute {
	public Player p;
	public Player lastHitBy;
	public boolean locked;
	private int district;
	private boolean hasDistrict;
	private HashMap<String, Integer> damage;
	private PlayerData data;
	private PlayerFatalitiesData fatalities;
	
	public Tribute(Player p) {
		this.p = p;
		this.lastHitBy = null;
		this.locked = true;
		this.district = 0;
		this.hasDistrict = false;
		this.damage = new HashMap<String, Integer>();
		this.data = new PlayerData(p);
		this.fatalities = new PlayerFatalitiesData(p);
	}
	
	public void initCitizen() {
		// TODO
		// Set relevant parameters
		hasDistrict = false;
		p.setGameMode(GameMode.SURVIVAL);
		// TODO We want to give them max fullness, double check this
		p.setHealth(20);
		p.setSaturation(0);
		p.setFoodLevel(20);
		// Teleport to lobby spawn location
	}
	
	public void initTribute() {
		// TODO
		// Set relevant parameters
		p.setGameMode(GameMode.SURVIVAL);
		// TODO We want to give them max fullness, double check this
		p.setHealth(20);
		p.setSaturation(0);
		p.setFoodLevel(20);
		// Teleport to lobby spawn location
	}
	
	public void initSpectator() {
		// TODO
		// Set relevant parameters
		hasDistrict = false;
		p.setGameMode(GameMode.CREATIVE);
		// Teleport to current map center
	}
	
	public void lock() {
		// Lock the player
		locked = true;
	}
	
	public void unlock() {
		// Unlock the player
		locked = false;
	}
	
	public void setDistrict(int district) {
		// Set the player's district
		this.district = district;
		hasDistrict = true;
	}
	
	public int getDistrict() {
		return district;
	}
	
	public boolean hasDistrict() {
		return hasDistrict;
	}
	
	public void load(Game g) {
		// Lock, asynchronously fetch data, unlock when finished
		lock();
		g.p.getServer().getScheduler().runTaskAsynchronously(g.p, new AsyncLoad(g));
	}
	
	public void save(Game g) {
		// Spawn an async task to save data from the DB
		g.p.getServer().getScheduler().runTaskAsynchronously(g.p, new AsyncSave(g));
	}
	
	private class AsyncLoad implements Runnable {
		private Game g;
		
		public AsyncLoad(Game g) {
			this.g = g;
		}
		
		@Override
		public void run() {
			// Load the data
			data.load(g);
			fatalities.load(g);
			// Unlock and notify of sync
			unlock();
			if(p != null)
				p.sendMessage(ChatColor.AQUA + "Data synced with database!");
		}
	}
	
	private class AsyncSave implements Runnable {
		private Game g;
		
		public AsyncSave(Game g) {
			this.g = g;
		}
		
		@Override
		public void run() {
			// Load the data
			data.save(g);
			fatalities.save(g);
			// Unlock and notify of sync
			unlock();
			if(p != null)
				p.sendMessage(ChatColor.AQUA + "Data synced with database!");
		}
	}
}
