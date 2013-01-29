package org.dotGaming.Endain.MCHG.Core.System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class CountdownManager {
	private Game g;
	private int waits;
	private int announcements;
	private int countdown;
	
	public CountdownManager(Game g) {
		this.g = g;
		this.waits = 4;
		this.announcements = 2;
		this.countdown = 5;
	}
	
	private void load() {
		// TODO
	}
	
	public void kill() {
		// TODO
	}
	
	class Announce implements Runnable {
		@Override
		public void run() {
			if(announcements > 0) {
				// Find seconds left
				int seconds = 10 * announcements;
				// Announce progress update
				g.p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + seconds + " seconds" + ChatColor.RESET + " until match commencement!");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "Please select a district to represent in this match!");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "Walk over a vacant glowstone block to register!");
				// Decrement remaining announcements
				announcements--;
				// Schedule next announcement
				if(announcements > 0)
					g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Announce(), 200);
				else
					g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Announce(), 100);
			} else {
				// Schedule count down
				g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Countdown());
			}
		}
	}
	
	class Countdown implements Runnable {
		@Override
		public void run() {
			if(countdown > 0) {
				// Announce progress update
				g.p.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + countdown + " second(s)" + ChatColor.RESET + " until match commencement!");
				// Decrement remaining announcements
				countdown--;
				// Schedule next announcement
				g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Countdown(), 20);
			} else {
				// Make sure every player has a district
				assignLeftovers();
				// Close district selection
				close();
				// Unlock all players
				g.pm.unlockCitizens();
				// Move all citizens to tributes
				g.pm.citizensToTributes();
				// Notify the GameMachine we are done
				g.gm.doneDistricting();
			}
		}
	}
}
