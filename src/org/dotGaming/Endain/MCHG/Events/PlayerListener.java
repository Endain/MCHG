package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

//Contains events relating to player actions, excluding combat.
public class PlayerListener implements Listener {
	private Game g;
	
	public PlayerListener(Game g) {
		this.g = g;
	}
	
	@EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
    	// Check if it is a valid game state to join on.
    	if(!g.pm.canConnect()) {
    		event.setLoginResult(Result.KICK_OTHER);
    		// Give a message based on game state.
    		if(g.gm.getState() == 0)
    			event.setKickMessage(ChatColor.RED + "The server is starting, please try again soon!");
    		else
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join the server at this time!");
    	}
    }
	
	@EventHandler
    public void onLogin(PlayerLoginEvent event) {
    	// Nothing to do here for now. This just confirms
    	// a valid login before player is manipulable.
    }
	
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
    	// Initialize player and move them to the correct location.
    	g.pm.addPlayer(event.getPlayer());
    }
	
	@EventHandler
    public void onQuit(PlayerQuitEvent event) {
    	// Code goes here.
    	// Call something to gracefully disconnect the player.
    	// and remove them from the system.
    }
	
	@EventHandler
    public void onMove(PlayerMoveEvent event) {
    	// Disallow movement if tribute is "locked".
    	Location from = event.getFrom();
    	Location to = event.getTo();
    	Tribute t = g.pm.getTribute(event.getPlayer());
    	// Ensure the player in question is in the server.
    	if(t != null) {
    		// Cancel movement if the tribute is "locked".
    		if(t.locked) {
    			Location look = new Location(from.getWorld(), from.getX(), from.getY(), from.getZ(), to.getYaw(), to.getPitch());
    			event.setTo(look);
    		}
    		// Pass onCollide event to relevant managers
    		g.tm.onCollide(t);
    	}
    }
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		// Give an MOTD based on state
		event.setMotd(g.gm.getMOTD());
	}
}
