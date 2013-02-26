package org.dotGaming.Endain.MCHG.Core.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;
import org.dotGaming.Endain.MCHG.Core.Fireworks.FireworkFactory;

public class MapManager implements Manager{
	private Game g;
	private ArrayList<Map> maps;
	private Map current;
	private Random rand;
	private BukkitTask event;
	private BukkitTask firstRefill;
	private BukkitTask secondRefill;

	public MapManager(Game g) {
		this.g = g;
		this.maps = new ArrayList<Map>();
		this.current = null;
		this.rand = new Random();
		this.event = null;
		this.firstRefill = null;
		this.secondRefill = null;
	}
	
	@Override
	public boolean load() {
		// Track if successful
		boolean success = false;
		// Load map data from the database based on tier
		// Get a database connection to load data from
		Connection c = g.dm.getDB("MCHG");
		if(c != null) {
			PreparedStatement getMaps;
		    String query = "SELECT * FROM Maps";
	    	try {
	    		// Attempt to create the query statement
				getMaps = c.prepareStatement(query);
				// Execute the query
				ResultSet r = getMaps.executeQuery();
				// Extract the results
				while(r.next()) {
					// Create maps from data
					Map m = new Map();
					m.setId(r.getInt(1));
					m.setName(r.getString(2));
					m.setAuthor(r.getString(3));
					m.setVersion(r.getString(4));
					m.setDescription(r.getString(5));
					m.setSupport(r.getString(6));
					m.setX(r.getDouble(7));
					m.setY(r.getDouble(8));
					m.setZ(r.getDouble(9));
					m.setRadius(r.getDouble(10));
					// Add map to Maplist
					maps.add(m);
				}
				// Close statements
				r.close();
				getMaps.close();
				// Loading was successful
				success = true;
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
		// Return success state
		return success;
	}

	@Override
	public void reset() {
		// Nothing to do for now
	}
	
	@Override
	public void kill() {
		// Clear out all the maps
		maps.clear();
	}
	
	public Map getcurrentMap() {
		return current;
	}
	
	public ArrayList<Map> getRandomMaps(int max) {
		// Make a new maplist to return
		ArrayList<Map> maplist = new ArrayList<Map>();
		// Get random maps if more available then 'max'
		if(maps.size() > max) {
			// Keep adding until we reach the desired number of maps
			while(maplist.size() < max) {
				Map m = maps.get(rand.nextInt(maps.size()));
				// Only add the map if it is not already on the list
				if(!maplist.contains(m))
					maplist.add(m);
			}
		} else {
			// Just return all the maps in the manager otherwise
			for(int i = 0; i < maps.size(); i++)
				maplist.add(maps.get(i));
		}
		// Return our random map list
		return maplist;
	}
	
	public void chooseMap(Map m) {
		if(maps.contains(m))
			current = m;
	}
	
	public int getCurrentId() {
		if(current != null)
			return current.getId();
		return -1;
	}
	
	public void scheduleEvent() {
		MapEvent me = new MapEvent(g);
		g.p.getServer().getScheduler().runTaskLater(g.p, me, me.getDelay());
	}
	
	public void cancelEvent() {
		if(event != null)
			g.p.getServer().getScheduler().cancelTask(event.getTaskId());
	}
	
	public void scheduleRefills() {
		firstRefill = g.p.getServer().getScheduler().runTaskLater(g.p, new Refill(), 600);//9600);
		secondRefill = g.p.getServer().getScheduler().runTaskLater(g.p, new Refill(), 1200);//25200);
	}
	
	public void cancelRefills(boolean first, boolean second) {
		// Cancel first refill?
		if(first)
			if(firstRefill != null)
				g.p.getServer().getScheduler().cancelTask(firstRefill.getTaskId());
		// Cancel second refill?
		if(second)
			if(secondRefill != null)
				g.p.getServer().getScheduler().cancelTask(secondRefill.getTaskId());
	}
	
	public void removeNonPlayers() {
		// Remove any non-player entities by killing them
		Iterator<LivingEntity> iter = g.w.getEntitiesByClass(LivingEntity.class).iterator();
		while(iter.hasNext()) {
			LivingEntity le = iter.next();
			if(!(le instanceof Player)) {
				le.damage(le.getMaxHealth());
			}
		}
	}
	
	public void launchFirework(Location loc, boolean trail, boolean flicker, Type type, Collection<Color> color, Collection<Color> fade, int power) {
		// Build the firework effect
		Builder b = FireworkEffect.builder();
		if(trail)
			b.withTrail();
		if(flicker)
			b.withFlicker();
		b.with(type);
		b.withColor(color);
		b.withFade(fade);
		// Create a new firework
		Firework fw = (Firework)g.w.spawnEntity(loc, EntityType.FIREWORK);
		// Get and set the metadata
		FireworkMeta fm = fw.getFireworkMeta();
		fm.setPower(power);
		fm.addEffect(b.build());
		// Apply to our current firework
		fw.setFireworkMeta(fm);
	}
	
	private class Refill implements Runnable {
		@Override
		public void run() {
			// Refill all chests!
			g.cm.refill();
			// Announce the the chests have been refilled
			g.p.getServer().broadcastMessage(ChatColor.GREEN + "The chests have been refilled!");
			g.pm.playSoundEffectTributes(Sound.LEVEL_UP, 1f, .75f);
			Location loc = new Location(g.w, current.getX(), current.getY(), current.getZ());
			// Firework set colors
			Collection<Color> color = new LinkedList<Color>();
			color.add(Color.fromBGR(152, 102, 227));
			//color.add(Color.ORANGE); color.add(Color.YELLOW); color.add(Color.MAROON); color.add(Color.RED);
			Collection<Color> fade = new LinkedList<Color>();
			fade.add(Color.WHITE);
			// Launch fireworks
			FireworkFactory.launchFirework(loc, FireworkFactory.getCapitolFirework(), 1);
			FireworkFactory.launchFirework(loc, FireworkFactory.getCapitolFirework(), 2);
			FireworkFactory.launchFirework(loc, FireworkFactory.getCapitolFirework(), 3);
			//launchFirework(loc, true, false, Type.BURST, color, fade, 1);
			//launchFirework(loc, true, false, Type.BURST, color, fade, 2);
			//launchFirework(loc, true, true, Type.BURST, color, fade, 3);
		}
	}
}
