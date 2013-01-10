package org.dotGaming.Endain.MCHG;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Events.PreInitListener;

// Hunger Games plugin container class.
public final class MCHG extends JavaPlugin {
	public Game g;
	
	@Override
    public void onEnable(){
		// Bukkit automatically prints an "Enabled!" message
		getLogger().info("MCHG is initializing!");
		
		// Add the pre-init listener to disallow player joins until the plugin is initialized
		Listener preInit = new PreInitListener();
		getServer().getPluginManager().registerEvents(preInit, this);
		
		// Instantiate the game bus/container and initialize it.
		g = new Game(this);
		g.init();
		
		// Remove the pre-init listener to allow for logins
		AsyncPlayerPreLoginEvent.getHandlerList().unregister(preInit);
    }

    @Override
    public void onDisable() {
    	// Bukkit automatically prints an "Disabled!" message
    	getLogger().info("MCHG is shutting down!");
    	
    	// Shut down the game.
    	if(g != null)
    		g.kill();
    }
}