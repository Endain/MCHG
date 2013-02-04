package org.dotGaming.Endain.MCHG.Core.Player;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerHistoryData {
	private int id;
	public Date joined;
	public Date lastplayed;
	public int wins;
	public int highfinish;
	public int lowfinish;
	public double averagefinish;
	public long timeplayed;
	public long averagelifetime;
	public long timestill;
	public int kills;
	public int gamesplayed;
	public double rawpoints;
	
	public void load(Game g, int id) {
		// Save the ID
		this.id = id;
		// Load the player data set
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getData;
		    String query = "SELECT joined, lastplayed, wins, highfinish, lowfinish, averagefinish, timeplayed, averagelifetime, timestill, kills, gamesplayed FROM PlayerHistory WHERE id=?";
	    	try {
	    		// Attempt to create the query statement
				getData = c.prepareStatement(query);
				// Set the name to look up
				getData.setInt(1, id);
				// Execute the query
				ResultSet r = getData.executeQuery();
				// See if the record existed, read it if it did, otherwise enter it now
				if(r.next()) { // Entry exists
					// Extract the results
					joined = r.getDate(1);
					lastplayed = r.getDate(2);
					wins = r.getInt(3);
					highfinish = r.getInt(4);
					lowfinish = r.getInt(5);
					averagefinish = r.getDouble(6);
					timeplayed = r.getLong(7);
					averagelifetime = r.getLong(8);
					timestill = r.getLong(9);
					kills = r.getInt(10);
					gamesplayed = r.getInt(11);
				} else {
					// Initialize with new player values
					newData();
					PreparedStatement putData;
				    query = "INSERT INTO PlayerHistory values(?,?,?,?,?,?,?,?,?,?,?,?)";
				    // Attempt to create the query statement
					putData = c.prepareStatement(query);
					// Set the name to look up
					putData.setInt(1, id);
					putData.setDate(2, joined);
					putData.setDate(3, lastplayed);
					putData.setInt(4, wins);
					putData.setInt(5, highfinish);
					putData.setInt(6, lowfinish);
					putData.setDouble(7, averagefinish);
					putData.setLong(8, timeplayed);
					putData.setLong(9, averagelifetime);
					putData.setLong(10, timestill);
					putData.setInt(11, kills);
					putData.setInt(12, gamesplayed);
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
		// Save the player data set
		// Get a database connection to save data to
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement saveData;
		    String query = "UPDATE PlayerHistory SET joined=?, lastplayed=?, wins=?, highfinish=?, lowfinish=?, averagefinish=?, timeplayed=?, averagelifetime=?, timestill=?, kills=?, gamesplayed=? WHERE id=?";
	    	try {
	    		// Attempt to create the query statement
				saveData = c.prepareStatement(query);
				// Set the data to save
				saveData.setDate(1, joined);
				saveData.setDate(2, lastplayed);
				saveData.setInt(3, wins);
				saveData.setInt(4, highfinish);
				saveData.setInt(5, lowfinish);
				saveData.setDouble(6, averagefinish);
				saveData.setLong(7, timeplayed);
				saveData.setLong(8, averagelifetime);
				saveData.setLong(9, timestill);
				saveData.setInt(10, kills);
				saveData.setInt(11, gamesplayed);
				saveData.setInt(12, id);
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
		joined = new Date(System.currentTimeMillis());
		lastplayed = new Date(System.currentTimeMillis());
		wins = 0;
		highfinish = 24;
		lowfinish = 24;
		averagefinish = 24;
		timeplayed = 0;
		averagelifetime = 0;
		timestill = 0;
		kills = 0;
		gamesplayed = 0;
	}
}
