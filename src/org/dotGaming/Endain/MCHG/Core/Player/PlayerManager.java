package org.dotGaming.Endain.MCHG.Core.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

//Handles players as they join/leave. Handles their state/transitions/interactions.
public class PlayerManager implements Manager{
	private Game g;
	private HashMap<String, Tribute> citizens;
	private HashMap<String, Tribute> tributes;
	private HashMap<String, Tribute> spectators;
	private ArrayList<String> alumni;
	
	public PlayerManager(Game g) {
		this.g = g;
		this.citizens = new HashMap<String, Tribute>();
		this.tributes = new HashMap<String, Tribute>();
		this.spectators = new HashMap<String, Tribute>();
		this.alumni = new ArrayList<String>();
	}
	
	@Override
	public boolean load() {
		// Nothing to do for now
		return true;
	}

	@Override
	public void reset() {
		// Nothing to do for now
	}
	
	@Override
	public void kill() {
		// Kick all players
		Player p[] = g.p.getServer().getOnlinePlayers();
		for(int i = 0; i < p.length; i++)
			p[i].kickPlayer(ChatColor.RED + "Server shutting down!");
		// Clear all lists
		citizens.clear();
		tributes.clear();
		spectators.clear();
	}
	
	public boolean canConnect() {
		// Check if a player can connect
		int state = g.gm.getState();
		// State list:
		// 0 - Initializing
		// 1 - Grace period (new players only)
		// 2 - Voting
		// 3 - District selection
		// 4 - Count down
		// 5 - Game
		// 6 - Deathmatch
		// 7 - Victory
		if(state == 0 || state == 3 || state == 4 || state == 6 || state == 7)
			return false;
		return true;
	}
	
	public void addPlayer(Player p) {
		// First ensure that the player does not already exist
		removePlayer(p);
		// Filter player into correct list based on game state
		Tribute t = new Tribute(p);
		if(g.gm.getState() >= 1 && g.gm.getState() <= 2) {
			// Pre-game states
			citizens.put(p.getName(), t);
			t.initCitizen();
		} else if(g.gm.getState() >= 5 && g.gm.getState() <= 6) {
			// Mid-game states
			spectators.put(p.getName(), t);
			t.initSpectator();
		}
		// Load the player's data from the DB
		t.load(g);
	}
	
	public void removePlayer(Player p) {
		// Remove a player from the player manager
		if(p != null) {
			// Remove from lists
			if(citizens.containsKey(p.getName()))
				citizens.remove(p.getName());
			if(tributes.containsKey(p.getName()))
				tributes.remove(p.getName());
			if(spectators.containsKey(p.getName()))
				spectators.remove(p.getName());
		}
	}
	
	public Tribute getTribute(Player p) {
		// Get the tribute object for a given player
		if(citizens.containsKey(p.getName()))
			return citizens.get(p.getName());
		if(tributes.containsKey(p.getName()))
			return tributes.get(p.getName());
		if(spectators.containsKey(p.getName()))
			return tributes.get(p.getName());
		return null;
	}
	
	public boolean isCitizen(Player p) {
		// Determine if a player is a citizen
		if(citizens.containsKey(p.getName()))
			return true;
		return false;
	}
	
	public boolean isTribute(Player p) {
		// Determine if a player is a tribute
		if(tributes.containsKey(p.getName()))
			return true;
		return false;
	}
	
	public boolean isSpectator(Player p) {
		// Determine if a player is a spectator
		if(spectators.containsKey(p.getName()))
			return true;
		return false;
	}
	
	public int getNumberOfPlayers() {
		// Return a count of all online players
		return citizens.size() + tributes.size() + spectators.size();
	}
	
	public void citizensToTributes() {
		// Move all citizens to tributes
		Iterator<Tribute> i = citizens.values().iterator();
		while(i.hasNext()) {
			Tribute t = i.next();
			tributes.put(t.p.getName(), t);
		}
		// Clear citizens
		citizens.clear();
	}
	
	public void tributeToSpectator(Player p) {
		// Move a player from tribute to spectators
		Tribute t = tributes.remove(p.getName());
		if(t != null) {
			spectators.put(p.getName(), t);
			// Initialize as spectator
			t.initSpectator();
		}
	}
	
	public void addAlumni(Player p) {
		// Add a player to the list of alumni for hte match
		alumni.add(p.getName());
	}
	
	public void clearAlumni() {
		// Clear the list of alumni
		alumni.clear();
	}
	
	public boolean isAlumni(String name) {
		// Check if a name is part of the alumni
		if(alumni.contains(name))
			return true;
		return false;
	}
	
	public ArrayList<Tribute> getCitizensWithoutDistrict() {
		// Get a list of all citizen without a district
		ArrayList<Tribute> list = new ArrayList<Tribute>();
		Iterator<Tribute> i = citizens.values().iterator();
		// Check all citizens
		while(i.hasNext()) {
			Tribute t = i.next();
			// Add them if they have no district
			if(!t.hasDistrict())
				list.add(t);
		}
		// Return the list is district-less citizens
		return list;
	}
	
	public void lockCitizens() {
		// Lock all citizens
		Iterator<Tribute> i = citizens.values().iterator();
		while(i.hasNext())
			i.next().lock();
	}
	
	public void unlockCitizens() {
		// unlock all citizens
		Iterator<Tribute> i = citizens.values().iterator();
		while(i.hasNext())
			i.next().unlock();
	}
	
	public void lockTributes() {
		// Lock all citizens
		Iterator<Tribute> i = tributes.values().iterator();
		while(i.hasNext())
			i.next().lock();
	}
	
	public void unlockTributes() {
		// unlock all citizens
		Iterator<Tribute> i = tributes.values().iterator();
		while(i.hasNext())
			i.next().unlock();
	}
	
	public Iterator<Tribute> getTributeIterator() {
		// Return an iterator over the elements of tributes
		return tributes.values().iterator();
	}
	
	public void addPotionEffectTributes(PotionEffect pe) {
		// Add potion effect to all tributes
		Iterator<Tribute> iter = tributes.values().iterator();
		while(iter.hasNext())
			iter.next().p.addPotionEffect(pe);
	}
	
	public void playSoundEffectTributes(Sound s, float volume, float pitch) {
		// Add potion effect to all tributes
		Iterator<Tribute> iter = tributes.values().iterator();
		while(iter.hasNext()) {
			Player p = iter.next().p;
			p.playSound(p.getLocation(), s, volume, pitch);
		}
	}
	
	public void playDistantSoundEffectTributes(Sound s, float volume, float pitch) {
		// Add potion effect to all tributes
		Iterator<Tribute> iter = tributes.values().iterator();
		while(iter.hasNext()) {
			Player p = iter.next().p;
			Vector v;
			Random r = new Random();
			float f = r.nextFloat();
			// Choose a random direction
			if(f <= .25)
				v = new Vector(10,0,0);
			else if(f <= .5)
				v = new Vector(-10,0,0);
			else if(f <= .75)
				v = new Vector(0,0,10);
			else
				v = new Vector(0,0,-10);
			p.playSound(p.getLocation().add(v), s, volume, pitch);
		}
	}
	
	public void signalTributeDeath() {
		// Signal that a tribute has died
		Iterator<Tribute> iter = tributes.values().iterator();
		while(iter.hasNext()) {
			Player p = iter.next().p;
			p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1.5f);
			p.playSound(p.getLocation(), Sound.EXPLODE, 1, .5f);
		}
	}
}
