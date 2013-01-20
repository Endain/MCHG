package org.dotGaming.Endain.MCHG.Core.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerFatalitiesData {
	private String name;
	public int player;
	public int monster;
	public int explosion;
	public int fire;
	public int drowning;
	public int hunger;
	public int fall;
	public int other;
	
	public PlayerFatalitiesData(Player p) {
		this.name = p.getName();
	}
	
	public void load(Game g) {
		// Load the player fatalities data set
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getData;
		    String query = "SELECT player, monster, explosion, fire, drowning, hunger, fall, other FROM PlayerFatalitiesData WHERE name=?";
	    	try {
	    		// Attempt to create the query statement
				getData = c.prepareStatement(query);
				// Set the name to look up
				getData.setString(1, name);
				// Execute the query
				ResultSet r = getData.executeQuery();
				// See if the record existed, read it if it did, otherwise enter it now
				if(r.next()) { // Entry exists
					// Extract the results
					player = r.getInt(1);
					monster = r.getInt(2);
					explosion = r.getInt(3);
					fire = r.getInt(4);
					drowning = r.getInt(5);
					hunger = r.getInt(6);
					fall = r.getInt(7);
					other = r.getInt(8);
				} else {
					// Initialize with new player values
					newData();
					PreparedStatement putData;
				    query = "INSERT INTO PlayerFatalitiesData values(?,?,?,?,?,?,?,?,?)";
				    // Attempt to create the query statement
					putData = c.prepareStatement(query);
					// Set the name to look up
					putData.setString(1, name);
					putData.setInt(2, player);
					putData.setInt(3, monster);
					putData.setInt(4, explosion);
					putData.setInt(5, fire);
					putData.setInt(6, drowning);
					putData.setInt(7, hunger);
					putData.setInt(8, fall);
					putData.setInt(9, other);
					// Execute the query
					putData.execute();
					// Close statements
					putData.close();
				}
				// Close statements
				r.close();
				getData.close();
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
	
	public void save(Game g) {
		// Save the player fatalities data set
		// Get a database connection to save data to
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement saveData;
		    String query = "UPDATE PlayerFatalitiesData SET player=?, monster=?, explosion=?, fire=?, drowning=?, hunger=?, fall=?, other=? WHERE name=?";
	    	try {
	    		// Attempt to create the query statement
				saveData = c.prepareStatement(query);
				// Set the data to save
				saveData.setInt(1, player);
				saveData.setInt(2, monster);
				saveData.setInt(3, explosion);
				saveData.setInt(4, fire);
				saveData.setInt(5, drowning);
				saveData.setInt(6, hunger);
				saveData.setInt(7, fall);
				saveData.setInt(8, other);
				saveData.setString(9, name);
				// Execute the query
				saveData.execute();
				// Close statements
				saveData.close();
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
	
	private void newData() {
		// Initialize first time values
		player = 0;
		monster = 0;
		explosion = 0;
		fire = 0;
		drowning = 0;
		hunger = 0;
		fall = 0;
		other = 0;
	}
}
