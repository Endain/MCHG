package org.dotGaming.Endain.MCHG.Core.Chests;

import java.util.ArrayList;

import org.dotGaming.Endain.MCHG.Core.Game;

public class ChestManager {
	private Game g;
	private ArrayList<RefillingChest> chests;
	private ArrayList<Refiller> tiers;
	
	public ChestManager(Game g) {
		this.g = g;
		this.chests = new ArrayList<RefillingChest>();
		this.tiers = new ArrayList<Refiller>();
		// Load the tiers
		loadTiers();
	}
	
	private void loadTiers() {
	    // Loop to get all the tiers
	    for(int t = 0; t < 4; t++) { // 4 Tiers (0 to 3)
	    	tiers.add(new Refiller(g, t));
	    }
	}
	
	public void loadMap(int map) {
		// Clear out any old chests
		chests.clear();
		// Load the chests for the map
		// TODO
	}
	
	public void kill() {
		// Clear all lists
		chests.clear();
		tiers.clear();
	}
}
