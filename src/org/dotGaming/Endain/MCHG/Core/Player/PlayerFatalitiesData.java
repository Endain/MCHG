package org.dotGaming.Endain.MCHG.Core.Player;

import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerFatalitiesData {
	private Player p;
	private String name;
	public int player;
	public int monster;
	public int explosion;
	public int fire;
	public int drowning;
	public int hunger;
	public int fall;
	public int other;
	
	public PlayerFatalitiesData(Player p) {
		this.p = p;
		this.name = p.getName();
	}
	
	public void load(Game g) {
		// TODO
	}
	
	public void save(Game g) {
		// TODO
	}
}
