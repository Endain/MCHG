package org.dotGaming.Endain.MCHG.Core.Chests;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class RefillingChest {
	private ArrayList<Entry<Material, Float>> fill;
	private int tier;
	private Location loc;
	private Chest chest;
	private Random rand;
	
	public RefillingChest(int tier, Location loc, ArrayList<Entry<Material, Float>> fill) {
		this.tier = tier;
		this.fill = fill;
		this.loc = loc;
		this.rand = new Random();
		Block b = loc.getBlock();
		if(b.getType() == Material.CHEST)
			this.chest = (Chest)b;
		else
			this.chest = null;
	}
	
	public void refill() {
		if(chest != null) {
			// Clear the chest contents
			chest.getInventory().clear();
			// Some variable to determine which item to try and add and if we should add it
			float r = 0;
			int x = 0;
			// Loop thru each chest slot and attempt to add an item
			for(int i = 0; i < chest.getInventory().getSize(); i++) {
				r = rand.nextFloat();
				x = rand.nextInt(fill.size());
				if(r < fill.get(x).getValue()) {
					chest.getInventory().setItem(i, new ItemStack(fill.get(x).getKey(), 1));
				}
			}
			// If nothing got added, add something
			if(chest.getInventory().getContents().length == 0) {
				// Give it an apple in a random slot
				chest.getInventory().setItem(rand.nextInt(chest.getInventory().getSize()), new ItemStack(Material.APPLE, 1));
			}
			// If there are any diamond or iron remove all sticks or vice-versa
			// Stick takes precedence if lower tier chests, diamond/iron if upper tier
			if(tier >= 1) {
				if(chest.getInventory().contains(Material.IRON_INGOT) || chest.getInventory().contains(Material.DIAMOND)) {
					chest.getInventory().remove(Material.STICK);
				}
			} else {
				if(chest.getInventory().contains(Material.STICK)) {
					chest.getInventory().remove(Material.IRON_INGOT);
					chest.getInventory().remove(Material.DIAMOND);
				}
			}
		}
	}
}
