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
	}
	
	private void loadTiers() {
		// TODO
	}
	
	public void loadMap(int map) {
		// Clear out any old chests
		chests.clear();
		// Load the chests for the map
		// TODO
	}
}
