package org.dotGaming.Endain.MCHG.Core.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;

public class PlayerData {
	private Player p;
	private int id;
	private String name;
	public int rank;
	public double points;
	public int threat;
	public InetAddress ipv4;
	
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
		    String query = "SELECT id, name, rank, points, threat, INET_NTOA(ipv4) FROM Player WHERE name=?";
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
					id = r.getInt(1);
					name = r.getString(2);
					rank = r.getInt(3);
					points = r.getDouble(4);
					threat = r.getInt(5);
					try {
						ipv4 = InetAddress.getByName(r.getString(6));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				} else {
					// Initialize with new player values
					newData();
					PreparedStatement putData;
				    query = "INSERT INTO Player (name, rank, points, threat, ipv4) VALUES (?,?,?,?,INET_ATON(?))";
				    // Attempt to create the query statement
					putData = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					// Set the name to look up
					putData.setString(1, name);
					putData.setInt(2, rank);
					putData.setDouble(3, points);
					putData.setInt(4, threat);
					putData.setString(5, ipv4.getHostAddress());
					// Execute the query
					putData.executeUpdate();
					// Get the id generated
					ResultSet rs = putData.getGeneratedKeys();
				    rs.next();
				    id = rs.getInt(1);
				    rs.close();
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
		    String query = "UPDATE Player SET rank=?, points=?, threat=?, INET_NTOA(ipv4=?) WHERE id=?";
	    	try {
	    		// Attempt to create the query statement
				saveData = c.prepareStatement(query);
				// Set the data to save
				saveData.setInt(1, rank);
				saveData.setDouble(2, points);
				saveData.setInt(3, threat);
				saveData.setString(4, ipv4.getHostAddress());
				saveData.setInt(5, id);
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
		name = p.getName();
		rank = 0;
		points = 0;
		threat = 0;
		ipv4 = p.getAddress().getAddress();
	}
	
	public int getId() {
		// Return the player's ID in the database
		return id;
	}
}
