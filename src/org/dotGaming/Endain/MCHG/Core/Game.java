package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.plugin.Plugin;
import org.dotGaming.Endain.MCHG.Core.Player.PlayerManager;
import org.dotGaming.Endain.MCHG.Events.BlockListener;
import org.dotGaming.Endain.MCHG.Events.EntityListener;
import org.dotGaming.Endain.MCHG.Events.PlayerListener;

//Acts as a high level container and a bus between subsystems.
public class Game {
	public Plugin p;
	public GameMachine gm;
	public PlayerManager pm;
	public boolean initialized;
	
	public Game(Plugin p) {
		this.p = p;
		this.initialized = false;
	}
	
	public void init() {
		// Instantiate manager and modules
		this.gm = new GameMachine(this);
		this.pm = new PlayerManager(this);
		// Register event listeners
		p.getServer().getPluginManager().registerEvents(new PlayerListener(this), p);
		p.getServer().getPluginManager().registerEvents(new EntityListener(this), p);
		p.getServer().getPluginManager().registerEvents(new BlockListener(this), p);
		// Done initializing
		this.initialized = true;
	}
	
	public void kill() {
		// Called when the plugin is disabled.
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
