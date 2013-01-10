package org.dotGaming.Endain.MCHG.Core.Map;

import java.util.ArrayList;

import org.dotGaming.Endain.MCHG.Core.Game;

public class MapManager {
	private Game g;
	private ArrayList<Map> maps;

	public MapManager(Game g) {
		this.g = g;
		this.maps = new ArrayList<Map>();
	}
	
	public void load() {
		// TODO
		// Load all maps
	}
}
