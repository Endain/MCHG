package org.dotGaming.Endain.MCHG.Core.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;

import org.bukkit.Material;
import org.dotGaming.Endain.MCHG.Core.Game;

public class MapManager {
	private Game g;
	private ArrayList<Map> maps;
	private int index;

	public MapManager(Game g) {
		this.g = g;
		this.maps = new ArrayList<Map>();
		this.index = -1;
		// Load all maps form the database
		load();
	}
	
	private void load() {
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
					// Create maps
					// TODO
				}
				// Close statements
				r.close();
				getMaps.close();
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
	
	public void kill() {
		// Clear out all the maps
		maps.clear();
	}
	
	public Map getcurrentMap() {
		if(index > 0)
			return maps.get(index);
		return null;
	}
	
	public ArrayList<Map> getRandomMaps(int max) {
		// get random maps if more available then 'max'
		if(maps.size() > max) {
			ArrayList<Map> m = new ArrayList<Map>();
			// TODO
		}
		// If less that or equal to 'max' just return current maps
		return maps;
	}
}
