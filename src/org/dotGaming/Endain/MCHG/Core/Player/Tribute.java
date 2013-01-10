package org.dotGaming.Endain.MCHG.Core.Player;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

//Encapsulates a Minecraft player and adds HG specific attributes and functions.
public class Tribute {
	public Player p;
	public Player lastHitBy;
	public boolean locked;
	public int district;
	public int state;
	public String nickname;
	public HashMap<String, Integer> damage;
	
	public Tribute(Player p) {
		this.p = p;
		this.lastHitBy = null;
		this.locked = true;
		this.district = 0;
		this.state = 0;
		this.nickname = "Notch";
		this.damage = new HashMap<String, Integer>();
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void initCitizen() {
		// TODO
		// Set relevent parameters
		p.setGameMode(GameMode.SURVIVAL);
		// TODO We want to give them max fullness, double check this
		p.setHealth(20);
		p.setSaturation(0);
		p.setFoodLevel(20);
		// Teleport to lobby spawn location
	}
	
	public void initTribute() {
		// TODO
		// Set relevent parameters
		p.setGameMode(GameMode.SURVIVAL);
		// TODO We want to give them max fullness, double check this
		p.setHealth(20);
		p.setSaturation(0);
		p.setFoodLevel(20);
		// Teleport to lobby spawn location
	}
	
	public void initSpectator() {
		// TODO
		// Set relevent parameters
		p.setGameMode(GameMode.CREATIVE);
		// Teleport to current map center
	}
}
