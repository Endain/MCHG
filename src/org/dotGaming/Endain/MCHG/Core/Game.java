package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.command.CommandExecutor;
import org.dotGaming.Endain.MCHG.MCHG;
import org.dotGaming.Endain.MCHG.Core.Chests.ChestManager;
import org.dotGaming.Endain.MCHG.Core.Commands.CommandManager;
import org.dotGaming.Endain.MCHG.Core.Database.DatabaseManager;
import org.dotGaming.Endain.MCHG.Core.Map.BlockManager;
import org.dotGaming.Endain.MCHG.Core.Map.MapManager;
import org.dotGaming.Endain.MCHG.Core.Player.PlayerManager;
import org.dotGaming.Endain.MCHG.Core.System.GameMachine;
import org.dotGaming.Endain.MCHG.Core.System.VoteManager;
import org.dotGaming.Endain.MCHG.Events.BlockListener;
import org.dotGaming.Endain.MCHG.Events.EntityListener;
import org.dotGaming.Endain.MCHG.Events.PlayerListener;

//Acts as a high level container and a bus between subsystems.
public class Game {
	public MCHG p;
	public DatabaseManager dm;
	public GameMachine gm;
	public PlayerManager pm;
	public BlockManager bm;
	public MapManager mm;
	public ChestManager cm;
	public VoteManager vm;
	
	public Game(MCHG p) {
		this.p = p;
	}
	
	public void init() {
		// Instantiate and initialize critical managers
		this.dm = new DatabaseManager(this);
		this.dm.addDatabase("MCHG", "root", "root", "jdbc:mysql://127.1.0.0:3306/mchg");
		// Instantiate manager and modules
		this.gm = new GameMachine(this);
		this.pm = new PlayerManager(this);
		this.bm = new BlockManager(this);
		this.mm = new MapManager(this);
		this.cm = new ChestManager(this);
		this.vm = new VoteManager(this);
		// Register event listeners
		p.getServer().getPluginManager().registerEvents(new PlayerListener(this), p);
		p.getServer().getPluginManager().registerEvents(new EntityListener(this), p);
		p.getServer().getPluginManager().registerEvents(new BlockListener(this), p);
		// Register command executor
		CommandExecutor cmd = new CommandManager(this);
		p.getCommand("startLogging").setExecutor(cmd);
		p.getCommand("stopLogging").setExecutor(cmd);
		p.getCommand("revert").setExecutor(cmd);
		p.getCommand("maps").setExecutor(cmd);
		p.getCommand("v").setExecutor(cmd);
		p.getCommand("skip").setExecutor(cmd);
		// Done initializing
		gm.doneInitializing();
	}
	
	public void kill() {
		// Called when the plugin is disabled.
		// Kill the managers gracefully
		gm.kill();
		pm.kill();
		bm.kill();
		mm.kill();
		cm.kill();
		vm.kill();
		// Kill the critical managers gracefully
		dm.kill();
	}
}
