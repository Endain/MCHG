package org.dotGaming.Endain.MCHG;

import org.bukkit.plugin.java.JavaPlugin;

public final class MCHG extends JavaPlugin {
	@Override
    public void onEnable(){
        // TODO Insert logic to be performed when the plugin is enabled
		// Bukkit automatically prints an "Enabled!" message
		getLogger().info("MCHG is initializing!");
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    	// Bukkit automatically prints an "Disabled!" message
    	getLogger().info("MCHG is shutting down!");
    }
}