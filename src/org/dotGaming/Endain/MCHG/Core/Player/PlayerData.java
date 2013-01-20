package org.dotGaming.Endain.MCHG.Core.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerData {
	private Player p;
	private String name;
	public Date joined;
	public Date lastplayed;
	public int wins;
	public int highfinish;
	public int lowfinish;
	public double averagefinish;
	public long timeplayed;
	public long averagelifetime;
	public int kills;
	public int gamesplayed;
	public int rank;
	public long timestill;
	public double rawpoints;
	public int threatlevel;
	public InetAddress ip;
	
	public PlayerData(Player p) {
		this.p = p;
		this.name = p.getName();
	}
	
	public void load(Game g) {
		// Load the player data set
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getData;
		    String query = "SELECT joined, lastplayed, wins, highfinish, lowfinish, averagefinish, timeplayed, averagelifetime, kills, gamesplayed, rank, timestill, rawpoints, threatlevel, INET_NTOA(ip) FROM PlayerData WHERE name=?";
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
					joined = r.getDate(1);
					lastplayed = r.getDate(2);
					wins = r.getInt(3);
					highfinish = r.getInt(4);
					lowfinish = r.getInt(5);
					averagefinish = r.getDouble(6);
					timeplayed = r.getLong(7);
					averagelifetime = r.getLong(8);
					kills = r.getInt(9);
					gamesplayed = r.getInt(10);
					rank = r.getInt(11);
					timestill = r.getLong(12);
					rawpoints = r.getDouble(13);
					threatlevel = r.getInt(14);
					try {
						ip = InetAddress.getByName(r.getString(15));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				} else {
					// Initialize with new player values
					newData();
					PreparedStatement putData;
				    query = "INSERT INTO PlayerData values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,INET_ATON(?))";
				    // Attempt to create the query statement
					putData = c.prepareStatement(query);
					// Set the name to look up
					putData.setString(1, name);
					putData.setDate(2, joined);
					putData.setDate(3, lastplayed);
					putData.setInt(4, wins);
					putData.setInt(5, highfinish);
					putData.setInt(6, lowfinish);
					putData.setDouble(7, averagefinish);
					putData.setLong(8, timeplayed);
					putData.setLong(9, averagelifetime);
					putData.setInt(10, kills);
					putData.setInt(11, gamesplayed);
					putData.setInt(12, rank);
					putData.setLong(13, timestill);
					putData.setDouble(14, rawpoints);
					putData.setInt(15, threatlevel);
					putData.setString(16, ip.getHostAddress());
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
		    String query = "UPDATE PlayerData SET joined=?, lastplayed=?, wins=?, highfinish=?, lowfinish=?, averagefinish=?, timeplayed=?, averagelifetime=?, kills=?, gamesplayed=?, rank=?, timestill=?, rawpoints=?, threatlevel=?, INET_NTOA(ip=?) WHERE name=?";
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
				saveData.setInt(9, kills);
				saveData.setInt(10, gamesplayed);
				saveData.setInt(11, rank);
				saveData.setLong(12, timestill);
				saveData.setDouble(13, rawpoints);
				saveData.setInt(14, threatlevel);
				saveData.setString(15, name);
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
		kills = 0;
		gamesplayed = 0;
		rank = 0;
		timestill = 0;
		rawpoints = 0;
		threatlevel = 0;
		ip = p.getAddress().getAddress();
	}
}
