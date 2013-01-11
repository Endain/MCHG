package org.dotGaming.Endain.MCHG.Core.Chests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class RefillingChest {
	private Refiller filler;
	private Chest chest;
	
	public RefillingChest(Location loc, Refiller filler) {
		this.filler = filler;
		Block b = loc.getBlock();
		if(b.getType() == Material.CHEST)
			this.chest = (Chest)b;
		else
			this.chest = null;
	}
	
	public void refill() {
		// Make sure we have a valid chest and re-filler
		if(chest != null )
			if(filler != null)
				filler.fill(chest);
	}
}
