package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.command.CommandExecutor;
import org.dotGaming.Endain.MCHG.MCHG;
import org.dotGaming.Endain.MCHG.Core.Database.DatabaseManager;
import org.dotGaming.Endain.MCHG.Core.Map.BlockManager;
import org.dotGaming.Endain.MCHG.Core.Map.MapManager;
import org.dotGaming.Endain.MCHG.Core.Player.PlayerManager;
import org.dotGaming.Endain.MCHG.Events.BlockListener;
import org.dotGaming.Endain.MCHG.Events.EntityListener;
import org.dotGaming.Endain.MCHG.Events.PlayerListener;

//Acts as a high level container and a bus between subsystems.
public class Game {
	public MCHG p;
	public GameMachine gm;
	public PlayerManager pm;
	public BlockManager bm;
	public MapManager mm;
	public DatabaseManager dm;
	
	public Game(MCHG p) {
		this.p = p;
	}
	
	public void init() {
		// Instantiate manager and modules
		this.gm = new GameMachine(this);
		this.pm = new PlayerManager(this);
		this.bm = new BlockManager(this);
		this.mm = new MapManager(this);
		this.dm = new DatabaseManager(this);
		// Register event listeners
		p.getServer().getPluginManager().registerEvents(new PlayerListener(this), p);
		p.getServer().getPluginManager().registerEvents(new EntityListener(this), p);
		p.getServer().getPluginManager().registerEvents(new BlockListener(this), p);
		// Register command executor
		CommandExecutor cmd = new CommandManager(this);
		p.getCommand("startLogging").setExecutor(cmd);
		p.getCommand("stopLogging").setExecutor(cmd);
		p.getCommand("revert").setExecutor(cmd);
		// Done initializing
		gm.doneInitializing();
	}
	
	public void kill() {
		// Called when the plugin is disabled.
		gm.kill();
		pm.kill();
		bm.kill();
		mm.kill();
		dm.kill();
	}
}
