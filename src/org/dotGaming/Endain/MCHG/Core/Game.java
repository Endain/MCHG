package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.plugin.Plugin;
import org.dotGaming.Endain.MCHG.Core.Player.PlayerManager;

public class Game {
	// Acts as a high level container and a bus between subsystems.
	public Plugin p;
	public GameMachine gm;
	public PlayerManager pm;
	public boolean initialized;
	
	public Game(Plugin p) {
		this.p = p;
		this.initialized = false;
	}
	
	public void init() {
		this.gm = new GameMachine(this);
		this.pm = new PlayerManager(this);
		// Done initializing
		this.initialized = true;
	}
	
	public void kill() {
		// Called when the plugin is disabled.
	}
}
