package org.dotGaming.Endain.MCHG.Core.Chests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Location;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

public class ChestManager implements Manager{
	private Game g;
	private ArrayList<RefillingChest> chests;
	private ArrayList<Refiller> tiers;
	private boolean chestsLoaded;
	
	public ChestManager(Game g) {
		this.g = g;
		this.chests = new ArrayList<RefillingChest>();
		this.tiers = new ArrayList<Refiller>();
		this.chestsLoaded = false;
	}
	
	@Override
	public boolean load() {
		// Load the tiers
		loadTiers();
		return true;
	}

	@Override
	public void reset() {
		// Nothing to do for now
	}
	
	@Override
	public void kill() {
		// Clear all lists
		chests.clear();
		tiers.clear();
	}
	
	private void loadTiers() {
	    // Loop to get all the tiers
	    for(int t = 0; t < 4; t++) { // 4 Tiers (0 to 3)
	    	tiers.add(new Refiller(g, t));
	    }
	}
	
	public void refill() {
		// Refill all the chests
		for(int i = 0; i < chests.size(); i++)
			chests.get(i).refill();
	}
	
	public void loadMap() {
		// Clear out any old chests
		chests.clear();
		// Load the chests for the map
		chestsLoaded = false;
		// Load data asynchronously
		g.p.getServer().getScheduler().runTaskAsynchronously(g.p, new AsyncLoad());
	}
	
	public boolean isLoaded() {
		// Return if there is a set of chests loaded
		return chestsLoaded;
	}
	
	private class AsyncLoad implements Runnable {
		@Override
		public void run() {
			// Load refill data from the database based on tier
			// Get a database connection to load data from
			Connection c = g.dm.getDB("MCHG");
			if(c != null) {
				PreparedStatement getChests;
			    String query = "SELECT x, y, z, tier FROM Chests WHERE mapid = ?";
		    	try {
		    		// Attempt to create the query statement
					getChests = c.prepareStatement(query);
					// Set the tier to select
					getChests.setInt(1, g.mm.getCurrentId());
					// Execute the query
					ResultSet r = getChests.executeQuery();
					// Extract the results
					while(r.next()) {
						chests.add(new RefillingChest(new Location(g.w, r.getInt(1), r.getInt(2), r.getInt(3)), tiers.get(r.getInt(4))));
					}
					// Close statements
					r.close();
					getChests.close();
					// Signal that loading is done
					chestsLoaded = true;
					// Log that chests were successfully loaded
					g.p.getLogger().info("Chests have been loaded for map: " + g.mm.getCurrentId());
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
		}
	}
}
