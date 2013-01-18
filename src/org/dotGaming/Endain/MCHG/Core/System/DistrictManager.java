package org.dotGaming.Endain.MCHG.Core.System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class DistrictManager {
	private Game g;
	private ArrayList<DistrictCell> cells;
	private boolean open;
	
	public DistrictManager(Game g) {
		this.g = g;
		this.cells = new ArrayList<DistrictCell>();
		// Load the cells from the DB
		load();
		// close the cells by default
		close();
	}
	
	private void load() {
		// Load all district cells from the database
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getCells;
		    String query = "SELECT x, y, z, district FROM DistrictCells";
	    	try {
	    		// Attempt to create the query statement
				getCells = c.prepareStatement(query);
				// Execute the query
				ResultSet r = getCells.executeQuery();
				// Extract the results
				while(r.next()) {
					g.p.getLogger().info("CELL: x" + r.getInt(1) + " y" + r.getInt(2) + " z" + r.getFloat(3) + " d" + r.getInt(4));
					cells.add(new DistrictCell(g.w, r.getInt(1), r.getInt(2), r.getInt(3), r.getInt(4)));
				}
				// Close statements
				r.close();
				getCells.close();
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
	
	public void open() {
		// Set to open
		open = true;
		// Open all cells
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).open();
	}
	
	public void close() {
		open = false;
		// close all cells
		for(int i = 0; i < cells.size(); i++)
			cells.get(i).close();
	}
	
	public void onCollide(Tribute t) {
		// Check if in district select state
		if(g.gm.getState() == 3) {
			// Make sure the manager is open
			if(open) {
				// Make sure the player is a tribute
				//Tribute t = g.pm.getTribute(p);
				// TODO move tribute from citizen to tribute
				if(t != null) {
					for(int i = 0; i < cells.size(); i++) {
						if(cells.get(i).onCollide(t))
							break;
					}
				}
			}
		}
	}
	
	public void onQuit(Tribute t) {
		// Remove the tribute from a cell if they are in one and open it
		for(int i = 0; i < cells.size(); i++) {
			if(t == cells.get(i).getTribute()) {
				cells.get(i).open();
				cells.get(i).clearTribute();
				break;
			}
		}
	}
	
	public void kill() {
		// TODO
	}
}
