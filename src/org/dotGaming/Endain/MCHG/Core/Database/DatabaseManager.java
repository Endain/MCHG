package org.dotGaming.Endain.MCHG.Core.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import org.dotGaming.Endain.MCHG.Core.Game;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DatabaseManager {
	private Game g;
	private HashMap<String, BoneCP> pools;
	
	public DatabaseManager(Game g) {
		this.g = g;
		this.pools = new HashMap<String, BoneCP>();
	}
	
	public void addDatabase(String id, String user, String pass, String addr) {
		// Attempt to set up the connection pool
		try {
			// Load the driver
			Class.forName("com.mysql.jdbc.Driver"); 
			// Set up the config
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(addr);
			config.setUsername(user); 
			config.setPassword(pass);
			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(1);
			// Create the connection pool
			pools.put(id, new BoneCP(config));
		} catch (SQLException e) {
			// Error creating the connection pool
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Error loading driver
			e.printStackTrace();
		}
	}
	
	public Connection getDB(String id) {
		// Try to get a connection
		try {
			return pools.get(id).getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Otherwise return null
		return null;
	}
	
	public void kill() {
		// Close all connections
		Iterator<BoneCP> i = pools.values().iterator();
		while(i.hasNext()) {
			i.next().close();
		}
	}
}
