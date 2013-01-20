package org.dotGaming.Endain.MCHG.Core.System;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class DistrictCell {
	private World w;
	private Tribute current;
	private int x;
	private int y;
	private int z;
	private int district;
	private boolean open;
	private Block sign;
	private Block light;
	
	public DistrictCell(World w, int x, int y, int z, int district) {
		// Assign given parameters
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.district = district;
		// Fetch other data
		this.light = w.getBlockAt(x, y - 1, z);
		this.sign = w.getBlockAt(x, y, z);
		// Default to closed
		this.open = false;
		// Open the cell
		open();
	}
	
	public void open() {
		// Set the cell to be open
		open = true;
		// Replace sign and turn off light
		light.setType(Material.GLOWSTONE);
		sign.setType(Material.SIGN_POST);
		Sign s = (Sign) sign.getState();
		s.setLine(2, "[District " + district + "]");
		s.update();
		// TODO - set sign direction, color text?
	}
	
	public void close() {
		// Set the cell to be open
		open = false;
		// Remove sign and turn off light
		light.setType(Material.BEACON);
		sign.setType(Material.AIR);
	}
	
	public boolean onCollide(Tribute t) {
		// Validate the location is inside the cell
		Location l = t.p.getLocation();
		if(l.getX() > x && l.getX() < x + 1)
			if(l.getY() > y - 1 && l.getY() < y + 1)
				if(l.getZ() > z && l.getZ() < z + 1) {
					// Make sure the cell is open
					if(open) {
						// Lock the tribute at the center, set Tribute district and close the cell
						current = t;
						t.p.teleport(new Location(w, x + .5, y, z + .5, t.p.getLocation().getYaw(), t.p.getLocation().getPitch()));
						t.lock();
						t.setDistrict(district);
						close();
						// Send confirmation message
						t.p.sendMessage(ChatColor.GREEN + "You have joined district " + district + "!");
						// Return successful collision
						return true;
					} else {
						if(w.getTime() % 10 == 0)
							if(current != null)
								if(current != t)
									t.p.sendMessage(ChatColor.RED + "This spot is already taken by " + current.p.getName() + "! ");
					}
				}
		// Return no collision
		return false;
	}
	
	public Tribute getTribute() {
		return current;
	}
	
	public void clearTribute() {
		current = null;
	}
	
	public int getDistrict() {
		return district;
	}
}
