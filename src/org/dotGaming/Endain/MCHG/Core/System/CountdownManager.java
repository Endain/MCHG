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
		this.announcements = 2;
		this.countdown = 5;
		return true;
	}
	
	@Override
	public void reset() {
		// Restore default values
		this.retries = 0;
		this.announcements = 2;
		this.countdown = 5;
	}
	
	@Override
	public void kill() {
		// Nothing to do for now
	}
	
	public void waitForSync() {
		// Stall until the correct data has been loaded
		if(!(g.cm.isLoaded() && g.sm.isLoaded())) {
			g.p.getLogger().info(ChatColor.RED + "Waiting for map sync!");
			g.p.getServer().broadcastMessage(ChatColor.RED + "Waiting for map sync!");
			g.p.getServer().getScheduler().runTaskLater(g.p, new Retry(), 100);
		} else {
			beginCountdown();
		}
	}
	
	private void beginCountdown() {
		g.p.getLogger().info(ChatColor.GREEN + "Map sync complete!");
		g.p.getServer().broadcastMessage(ChatColor.GREEN + "Map sync complete!");
		// Move players to their spawn points
		g.sm.spawnPlayers();
	}
	
	class Retry implements Runnable {
		@Override
		public void run() {
			if(retries < 3) {
				// Increase the number of retries we've taken
				retries++;
				// Stall until the correct data has been loaded
				if(!g.cm.isLoaded()) {
					g.p.getLogger().info(ChatColor.RED + "Waiting for map sync! (Retry #" + retries + ")");
					g.p.getServer().broadcastMessage(ChatColor.RED + "Waiting for map sync! (Retry #" + retries + ")");
					g.p.getServer().getScheduler().runTaskLater(g.p, new Retry(), 100);
				} else {
					beginCountdown();
				}
			} else if(retries < 4) {
				// Increase the number of retries we've taken
				retries++;
				// Notify of critical server failure
				g.p.getLogger().info(ChatColor.RED + "Map sync failed! Server will shut down!");
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
			// TODO
		}
	}
	
	class Countdown implements Runnable {
		@Override
		public void run() {
			// TODO
		}
	}
}
