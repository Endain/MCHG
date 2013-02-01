package org.dotGaming.Endain.MCHG.Core.System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class DistrictManager implements Manager{
	private Game g;
	private ArrayList<DistrictCell> cells;
	private boolean open;
	private int announcements = 2;
	private int countdown = 5;
	
	public DistrictManager(Game g) {
		this.g = g;
		this.cells = new ArrayList<DistrictCell>();
	}
	
	@Override
	public boolean load() {
		// Track if successful
		boolean success = false;
		// Load all district cells from the database
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getCells;
		    String query = "SELECT x, y, z, district FROM DistrictCells";
	    	try {
	    		// Attempt to create the query statement
				getCells = c.prepareStatement(query);
				// Execute the query
				ResultSet r = getCells.executeQuery();
				// Extract the results
				while(r.next()) {
					//g.p.getLogger().info("CELL: x" + r.getInt(1) + " y" + r.getInt(2) + " z" + r.getFloat(3) + " d" + r.getInt(4));
					cells.add(new DistrictCell(g.w, r.getInt(1), r.getInt(2), r.getInt(3), r.getInt(4)));
				}
				// Close statements
				r.close();
				getCells.close();
				// Loading was successful
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	// Return the connection to the connection pool
	    	try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// close the cells by default
		close();
		// Return success state
		return success;
	}
	
	@Override
	public void reset() {
		// Close the cells by default
		close();
	}
	
	@Override
	public void kill() {
		// Clear the list of cells
		cells.clear();
	}
	
	private void assignLeftovers() {
		// Make sure all citizens has have a district
		ArrayList<Tribute> leftovers = g.pm.getCitizensWithoutDistrict();
		if(leftovers.size() > 0) {
			// Get the remaining spots open
			ArrayList<DistrictCell> openCells = new ArrayList<DistrictCell>();
			Iterator<DistrictCell> i = cells.iterator();
			// Iterate through all cells
			while(i.hasNext()) {
				DistrictCell dc = i.next();
				// Only add cells without a registered tribute
				if(dc.getTribute() == null)
					openCells.add(dc);
			}
			// Assign the remaining citizens
			for(int j = 0; j < leftovers.size(); j++) {
				leftovers.get(j).setDistrict(openCells.get(j).getDistrict());
				leftovers.get(j).p.sendMessage(ChatColor.RED + "You have been assigned to district " + openCells.get(j).getDistrict() + "!");
			}
		}
	}
	
	public void open() {
		// Set to open
		open = true;
		// Open all cells
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).open();
		// Reset count down variables
		announcements = 2;
		countdown = 5;
		// Start time tracking tasks
		g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Announce(), 200);
	}
	
	public void close() {
		open = false;
		// close all cells
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).close();
	}
	
	public void onCollide(Tribute t) {
		// Check if in district select state
		if(g.gm.getState() == 3) {
			// Make sure the manager is open
			if(open) {
				// Make sure the player is a tribute
				//Tribute t = g.pm.getTribute(p);
				// TODO move tribute from citizen to tribute
				if(t != null) {
					for(int i = 0; i < cells.size(); i++) {
						if(cells.get(i).onCollide(t))
							break;
					}
				}
			}
		}
	}
	
	public void onQuit(Tribute t) {
		// Remove the tribute from a cell if they are in one and open it
		for(int i = 0; i < cells.size(); i++) {
			if(t == cells.get(i).getTribute()) {
				cells.get(i).open();
				cells.get(i).clearTribute();
				break;
			}
		}
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
