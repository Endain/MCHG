package org.dotGaming.Endain.MCHG.Core.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class SpawnManager implements Manager {
	private Game g;
	private boolean spawnsLoaded;
	private ArrayList<Location> spawns;
	private Random rand;
	
	public SpawnManager(Game g) {
		this.g = g;
		this.spawns = new ArrayList<Location>();
		this.rand = new Random();
	}
	@Override
	public boolean load() {
		// Set default values
		spawnsLoaded = false;
		spawns.clear();
		return true;
	}

	@Override
	public void reset() {
		// Reset to default values
		spawnsLoaded = false;
		spawns.clear();
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
	}
	
	public void loadMap() {
		// Clear out any old spawns
		spawns.clear();
		spawnsLoaded = false;
		// Load data asynchronously
		g.p.getServer().getScheduler().runTaskAsynchronously(g.p, new AsyncLoad());
	}
	
	public boolean isLoaded() {
		// Return if there are spawns loaded
		return spawnsLoaded;
	}
	
	public void spawnPlayers() {
		if(g.pm.getNumberOfPlayers() <= spawns.size()) {
			// Make a copy of the spawn list and exhaust the list
			ArrayList<Location> slots = new ArrayList<Location>();
			for(int i = 0; i < spawns.size(); i++)
				slots.add(spawns.get(i));
			// Get a list of all tribute to iterate over
			Iterator<Tribute> iter = g.pm.getTributeIterator();
			while(iter.hasNext())
				// Move the tributes
				iter.next().p.teleport(slots.remove(rand.nextInt(slots.size())), TeleportCause.PLUGIN);
		}
	}
	
	private class AsyncLoad implements Runnable {
		@Override
		public void run() {
			// Load spawn point data based on the current map
			// Get a database connection to load data from
			Connection c = g.dm.getDB("MCHG");
			if(c != null) {
				PreparedStatement getSpawns;
			    String query = "SELECT x, y, z FROM Spawns WHERE mapid = ?";
		    	try {
		    		// Attempt to create the query statement
					getSpawns = c.prepareStatement(query);
					// Set the tier to select
					getSpawns.setInt(1, g.mm.getCurrentId());
					// Execute the query
					ResultSet r = getSpawns.executeQuery();
					// Extract the results
					while(r.next()) {
						spawns.add(new Location(g.w, r.getDouble(1), r.getDouble(2), r.getDouble(3)));
					}
					// Close statements
					r.close();
					getSpawns.close();
					// Signal that loading is done
					spawnsLoaded = true;
					// Log that spawns were successfully loaded
					g.p.getLogger().info("Spawns have been loaded for map: " + g.mm.getCurrentId());
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
