package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

//A listener to prevent login to the server prior to the plugin initializing
public class PreInitListener implements Listener {
	@EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
		// Disallow all logins until the plugin is initialized
        event.setLoginResult(Result.KICK_OTHER);
        event.setKickMessage(ChatColor.RED + "The plugin is not finished initializing!");
    }
}
