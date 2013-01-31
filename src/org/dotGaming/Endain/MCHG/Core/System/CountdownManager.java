package org.dotGaming.Endain.MCHG.Core.System;

import org.bukkit.ChatColor;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

public class CountdownManager implements Manager{
	private Game g;
	private int retries;
	private int announcements;
	private int countdown;
	
	public CountdownManager(Game g) {
		this.g = g;
	}
	
	@Override
	public boolean load() {
		// Set default values
		this.retries = 0;
		this.announcements = 3;
		this.countdown = 5;
		return true;
	}
	
	@Override
	public void reset() {
		// Restore default values
		this.retries = 0;
		this.announcements = 3;
		this.countdown = 5;
	}
	
	@Override
	public void kill() {
		// Nothing to do for now
	}
	
	public void waitForSync() {
		// Stall until the correct data has been loaded
		if(!(g.cm.isLoaded() && g.sm.isLoaded())) {
			g.p.getServer().broadcastMessage(ChatColor.RED + "Waiting for map sync!");
			g.p.getServer().getScheduler().runTaskLater(g.p, new Retry(), 20);
		} else {
			beginCountdown();
		}
	}
	
	private void beginCountdown() {
		g.p.getServer().broadcastMessage(ChatColor.AQUA + "Map sync complete!");
		// Move players to their spawn points
		g.sm.spawnPlayers();
		// Lock players until match starts
		g.pm.lockTributes();
		// Start long form countdown
		g.p.getServer().getScheduler().runTaskLater(g.p, new Announce(), 100);
	}
	
	class Retry implements Runnable {
		@Override
		public void run() {
			if(retries < 3) {
				// Increase the number of retries we've taken
				retries++;
				// Stall until the correct data has been loaded
				if(!(g.cm.isLoaded() && g.sm.isLoaded())) {
					g.p.getServer().broadcastMessage(ChatColor.RED + "Waiting for map sync! (Retry #" + retries + ")");
					g.p.getServer().getScheduler().runTaskLater(g.p, new Retry(), 100);
				} else {
					beginCountdown();
				}
			} else if(retries < 4) {
				// Increase the number of retries we've taken
				retries++;
				// Notify of critical server failure
				g.p.getServer().broadcastMessage(ChatColor.RED + "Map sync failed! Server will shut down!");
				g.p.getServer().getScheduler().runTaskLater(g.p, new Retry(), 100);
			} else {
				// Kill the plugin, we lost the DB connection
				g.kill();
			}
		}
	}
	
	class Announce implements Runnable {
		@Override
		public void run() {
			// Announce relevant message
			if(announcements > 0) {
				if(announcements == 3) {
					g.p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "The match will begin shortly!");
					g.p.getServer().broadcastMessage(ChatColor.GOLD + "Follow the rules! Type /rules if you do not know them!");
					g.p.getServer().broadcastMessage(ChatColor.GOLD + "May the odds be ever in your favor!");
				} else {
					g.p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + (announcements * 5 + 5) + " seconds" + ChatColor.RESET + " until the match begins!");
				}
				g.p.getServer().getScheduler().runTaskLater(g.p, new Announce(), 100);
			} else {
				// Fill the chests
				g.cm.refill();
				// Start the final countdown
				g.p.getServer().getScheduler().runTaskLater(g.p, new Countdown(), 0);
			}
			// Decrement the number of remaining announcements
			announcements--;
		}
	}
	
	class Countdown implements Runnable {
		@Override
		public void run() {
			// Announce relevant message
			if(countdown > 0) {
				g.p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + countdown + " second(s)" + ChatColor.RESET + " until the match begins!");
				g.p.getServer().getScheduler().runTaskLater(g.p, new Countdown(), 20);
			} else {
				// Unlock players
				g.pm.unlockTributes();
				// Notify players that the match has begun
				g.p.getServer().broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Let the Hunger Games begin!");
				// Flag countdown as finished in game machine
				g.gm.doneCountdown();
			}
			// Decrement the countdown value
			countdown--;
		}
	}
}
