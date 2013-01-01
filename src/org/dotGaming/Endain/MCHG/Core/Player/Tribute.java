package org.dotGaming.Endain.MCHG.Core.Player;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Tribute {
	// Encapsulates a Minecraft player and adds HG specific attributes and functions.
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
}
