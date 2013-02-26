package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.dotGaming.Endain.MCHG.MCHG;
import org.dotGaming.Endain.MCHG.Core.Chests.ChestManager;
import org.dotGaming.Endain.MCHG.Core.Commands.CommandManager;
import org.dotGaming.Endain.MCHG.Core.Database.DatabaseManager;
import org.dotGaming.Endain.MCHG.Core.Map.BlockManager;
import org.dotGaming.Endain.MCHG.Core.Map.MapManager;
import org.dotGaming.Endain.MCHG.Core.Map.SpawnManager;
import org.dotGaming.Endain.MCHG.Core.Player.PlayerManager;
import org.dotGaming.Endain.MCHG.Core.System.CountdownManager;
import org.dotGaming.Endain.MCHG.Core.System.DistrictManager;
import org.dotGaming.Endain.MCHG.Core.System.GameMachine;
import org.dotGaming.Endain.MCHG.Core.System.VoteManager;
import org.dotGaming.Endain.MCHG.Events.BlockListener;
import org.dotGaming.Endain.MCHG.Events.EntityListener;
import org.dotGaming.Endain.MCHG.Events.PlayerListener;

//Acts as a high level container and a bus between subsystems.
public class Game {
	public MCHG p;
	public World w;
	public DatabaseManager dm;
	public GameMachine gm;
	public PlayerManager pm;
	public BlockManager bm;
	public MapManager mm;
	public ChestManager cm;
	public SpawnManager sm;
	public VoteManager vm;
	public DistrictManager dim;
	public CountdownManager cdm;
	
	public Game(MCHG p) {
		this.p = p;
	}
	
	public boolean init() {
		// Get the primary world to be used
		this.w = p.getServer().getWorlds().get(0);
		
		// Instantiate and initialize critical managers
		this.dm = new DatabaseManager(this);
		if(!this.dm.load())
			return false;
		this.dm.addDatabase("MCHG", "root", "root", "jdbc:mysql://127.1.0.0:3306/mchg");
		
		// Instantiate manager and modules
		this.gm = new GameMachine(this);
		this.pm = new PlayerManager(this);
		this.bm = new BlockManager(this);
		this.mm = new MapManager(this);
		this.cm = new ChestManager(this);
		this.sm = new SpawnManager(this);
		this.vm = new VoteManager(this);
		this.dim = new DistrictManager(this);
		this.cdm = new CountdownManager(this);
		
		// Load modules, notify if failure occurs
		if(!(this.pm.load() && this.bm.load() && this.mm.load() && this.cm.load() &&
			 this.sm.load() && this.vm.load() && this.dim.load() && this.cdm.load())) {
			p.getServer().getLogger().info("A Module has failed to initialize!");
			return false;
		}
		
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
		p.getCommand("d").setExecutor(cmd);
		
		// Done initializing, kick off game machine
		gm.doneInitializing();
		// Successful, return true
		return true;
	}
	
	public void kill() {
		// Called when the plugin is disabled.
		// Kill the managers gracefully
		gm.kill();
		pm.kill();
		bm.kill();
		mm.kill();
		cm.kill();
		sm.kill();
		vm.kill();
		dim.kill();
		cdm.kill();
		// Kill the critical managers gracefully
		dm.kill();
	}
}
