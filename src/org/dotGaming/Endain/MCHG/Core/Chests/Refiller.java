package org.dotGaming.Endain.MCHG.Core.Chests;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class Refiller {
	private ArrayList<Entry<Material, Float>> fill;
	private int tier;
	private Random rand;
	
	public Refiller(int tier) {
		this.tier = tier;
		this.fill = new ArrayList<Entry<Material, Float>>();
		this.rand = new Random();
		// Load from the database
		load();
	}
	
	private void load() {
		// Load refill data from the database bassed on tier
	}
	
	public void fill(Chest c) {
		// Clear the chest contents
		c.getInventory().clear();
		// Some variable to determine which item to try and add and if we should add it
		float r = 0;
		int x = 0;
		// Loop thru each chest slot and attempt to add an item
		for(int i = 0; i < c.getInventory().getSize(); i++) {
			r = rand.nextFloat();
			x = rand.nextInt(fill.size());
			if(r < fill.get(x).getValue()) {
				c.getInventory().setItem(i, new ItemStack(fill.get(x).getKey(), 1));
			}
		}
		// If nothing got added, add something
		if(c.getInventory().getContents().length == 0) {
			// Give it an apple in a random slot
			c.getInventory().setItem(rand.nextInt(c.getInventory().getSize()), new ItemStack(Material.APPLE, 1));
		}
		// If there are any diamond or iron remove all sticks or vice-versa
		// Stick takes precedence if lower tier chests, diamond/iron if upper tier
		if(tier >= 1) {
			if(c.getInventory().contains(Material.IRON_INGOT) || c.getInventory().contains(Material.DIAMOND)) {
				c.getInventory().remove(Material.STICK);
			}
		} else {
			if(c.getInventory().contains(Material.STICK)) {
				c.getInventory().remove(Material.IRON_INGOT);
				c.getInventory().remove(Material.DIAMOND);
			}
		}
	}
}
