package org.dotGaming.Endain.MCHG;

import org.bukkit.plugin.java.JavaPlugin;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Events.BlockListener;
import org.dotGaming.Endain.MCHG.Events.EntityListener;
import org.dotGaming.Endain.MCHG.Events.PlayerListener;

public final class MCHG extends JavaPlugin {
	public Game g;
	
	@Override
    public void onEnable(){
		// Bukkit automatically prints an "Enabled!" message
		getLogger().info("MCHG is initializing!");
		
		// Instantiate the game bus/container.
		g = new Game(this);
		// Initialize the game.
		if(g != null)
			g.init();
		
		// Register event listeners.
		getServer().getPluginManager().registerEvents(new PlayerListener(g), this);
		getServer().getPluginManager().registerEvents(new EntityListener(g), this);
		getServer().getPluginManager().registerEvents(new BlockListener(g), this);
    }

    @Override
    public void onDisable() {
    	// Bukkit automatically prints an "Disabled!" message
    	getLogger().info("MCHG is shutting down!");
    	
    	// Shut down the game.
    	if(g != null)
    		g.init();
    }
}