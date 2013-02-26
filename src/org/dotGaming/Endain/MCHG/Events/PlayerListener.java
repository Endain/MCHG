package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Fireworks.FireworkFactory;
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
    		int state = g.gm.getState();
    		if(state == 0)
    			event.setKickMessage(ChatColor.RED + "The server is starting, please try again soon!");
    		else if(state == 3)
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join during district selection!");
    		else if(state == 4)
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join during the match countdown!");
    		else if(state == 6)
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join during the deathmatch!");
    		else if(state == 7)
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join during the victory cooldown!");
    		else
    			event.setKickMessage(ChatColor.RED + "Sorry, you cannot join the server at this time!");
    	} else if(g.pm.isAlumni(event.getName()) && (g.gm.getState() == 1)) {
    		event.setLoginResult(Result.KICK_OTHER);
    		event.setKickMessage(ChatColor.RED + "Please give new players a chance to join, you may try again in very soon!");
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
    		g.dim.onCollide(t);
    	}
    }
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		// Give an MOTD based on state
		event.setMotd(g.gm.getMOTD());
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		// Notify other tributes that a death has occurred
		Tribute t = g.pm.getTribute(event.getEntity());
		if(t != null) {
			// Notify of tribute death
			g.pm.playSoundEffectTributes(Sound.AMBIENCE_THUNDER, 1f, 1.5f);
			g.pm.playSoundEffectTributes(Sound.EXPLODE, 1f, .5f);
			g.p.getServer().broadcastMessage(ChatColor.GOLD + "A tribute from " + ChatColor.BOLD + "" + ChatColor.WHITE + "district " + t.getDistrict() + ChatColor.RESET + "" + ChatColor.GOLD + " has perished!");
			FireworkFactory.launchFirework(event.getEntity().getLocation(), FireworkFactory.getDistrictFirework(t.getDistrict()), 2);
			event.setDeathMessage(null);
			event.setDroppedExp(10);
		}
	}
}
