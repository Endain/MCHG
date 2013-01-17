package org.dotGaming.Endain.MCHG.Core.System;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class DistrictCell {
	private Tribute t;
	private int x;
	private int y;
	private int z;
	private int district;
	private boolean open;
	private Block sign;
	private Block light;
	
	public DistrictCell(int x, int y, int z, int district, World w) {
		// Assign given parameters
		this.x = x;
		this.y = y;
		this.z = z;
		this.district = district;
		// Fetch other data
		this.light = w.getBlockAt(x, y, z - 1);
		this.sign = w.getBlockAt(x, y, z);
		// Init other variables
		this.t = null;
		this.open = false;
		// Open the cell
		open();
	}
	
	public void open() {
		if(t != null) {
			t.locked = false;
			this.t = null;
		} else {
			// TODO
		}
	}
}
