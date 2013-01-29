package org.dotGaming.Endain.MCHG.Core.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

public class MapManager implements Manager{
	private Game g;
	private ArrayList<Map> maps;
	private Map current;
	private Random rand;

	public MapManager(Game g) {
		this.g = g;
		this.maps = new ArrayList<Map>();
		this.current = null;
		this.rand = new Random();
	}
	
	@Override
	public boolean load() {
		// Track if successful
		boolean success = false;
		// Load map data from the database based on tier
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getMaps;
		    String query = "SELECT * FROM Maps";
	    	try {
	    		// Attempt to create the query statement
				getMaps = c.prepareStatement(query);
				// Execute the query
				ResultSet r = getMaps.executeQuery();
				// Extract the results
				while(r.next()) {
					// Create maps from data
					Map m = new Map();
					m.setId(r.getInt(1));
					m.setName(r.getString(2));
					m.setAuthor(r.getString(3));
					m.setVersion(r.getString(4));
					m.setDescription(r.getString(5));
					m.setSupport(r.getString(6));
					m.setX(r.getDouble(7));
					m.setY(r.getDouble(8));
					m.setZ(r.getDouble(9));
					m.setRadius(r.getDouble(10));
					// Add map to Maplist
					maps.add(m);
				}
				// Close statements
				r.close();
				getMaps.close();
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
		// Return success state
		return success;
	}

	@Override
	public void reset() {
		// Nothing to do for now
	}
	
	@Override
	public void kill() {
		// Clear out all the maps
		maps.clear();
	}
	
	public Map getcurrentMap() {
		return current;
	}
	
	public ArrayList<Map> getRandomMaps(int max) {
		// Make a new maplist to return
		ArrayList<Map> maplist = new ArrayList<Map>();
		// Get random maps if more available then 'max'
		if(maps.size() > max) {
			// Keep adding until we reach the desired number of maps
			while(maplist.size() < max) {
				Map m = maps.get(rand.nextInt(maps.size()));
				// Only add the map if it is not already on the list
				if(!maplist.contains(m))
					maplist.add(m);
			}
		} else {
			// Just return all the maps in the manager otherwise
			for(int i = 0; i < maps.size(); i++)
				maplist.add(maps.get(i));
		}
		// Return our random map list
		return maplist;
	}
	
	public void chooseMap(Map m) {
		if(maps.contains(m))
			current = m;
	}
	
	public int getCurrentId() {
		if(current != null)
			return current.getId();
		return -1;
	}
}
