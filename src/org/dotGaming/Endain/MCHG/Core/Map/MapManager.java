package org.dotGaming.Endain.MCHG.Core.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.dotGaming.Endain.MCHG.Core.Game;

public class MapManager {
	private Game g;
	private ArrayList<Map> maps;
	private Map current;
	private Random rand;

	public MapManager(Game g) {
		this.g = g;
		this.maps = new ArrayList<Map>();
		this.current = null;
		this.rand = new Random();
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
		return current;
	}
	
	public ArrayList<Map> getRandomMaps(int max) {
		// get random maps if more available then 'max'
		if(maps.size() > max) {
			ArrayList<Map> maplist = new ArrayList<Map>();
			// Keep adding until we reach the desired number of maps
			while(maplist.size() < max) {
				Map m = maps.get(rand.nextInt(maps.size()));
				// Only add the map if it is not already on the list
				if(!maplist.contains(m))
					maplist.add(m);
			}
			// Return our random maplist
			return maplist;
		}
		// If less that or equal to 'max' just return current maps
		return maps;
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
