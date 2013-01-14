package org.dotGaming.Endain.MCHG.Core.System;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Map.Map;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class VoteManager {
	private Game g;
	private ArrayList<Map> maplist;
	private ArrayList<Integer> votelist;
	private ArrayList<Tribute> voters;
	private int skipvotes;
	private int skips;
	private boolean open;
	
	public VoteManager(Game g) {
		this.g = g;
		this.maplist = new ArrayList<Map>();
		this.votelist = new ArrayList<Integer>();
		this.voters = new ArrayList<Tribute>();
		this.skipvotes = 0;
		this.skips = 0;
		this.open = false;
	}
	
	public void startVote() {
		if(!open) {
			// Declare voting open
			this.open = true;
			// Clear the old data
			maplist.clear();
			votelist.clear();
			voters.clear();
			skipvotes = 0;
			skips = 0;
			// Get a new maplist and initialize votes
			maplist = g.mm.getRandomMaps(3);
			for(int i = 0; i < maplist.size(); i++)
				votelist.add(0);
			// Spawn announcer/timer task
			// TODO
		}
	}
	
	public void stopVote() {
		// TODO
	}
	
	public void castVote(Tribute t, int map) {
		// Make sure they have not yet voted
		if(!voters.contains(t)) {
			// Make sure it is a valid vote
			if(map <= votelist.size()) {
				// Add them to the list of voters and count their vote
				voters.add(t);
				votelist.set(map - 1, votelist.get(map - 1) + 1);
			} else {
				t.p.sendMessage(ChatColor.RED + "Invalid vote! Enter a choice between 1 and " + votelist.size());
			}
		} else {
			t.p.sendMessage(ChatColor.RED + "You have already voted and cannot change your vote!");
		}
		// Check for any triggering conditions
		update();
	}
	
	public void castSkip(Tribute t) {
		// Make sure we are allowing skip votes
		if(skips < 2) {
			// Make sure they have not yet voted
			if(!voters.contains(t)) {
				voters.add(t);
				skipvotes++;
			} else {
				t.p.sendMessage(ChatColor.RED + "You have already voted and cannot change your vote!");
			}
		} else {
			t.p.sendMessage(ChatColor.RED + "You have reached the maximum number of skips allowed (2)!");
		}
		// Check for any triggering conditions
		update();
	}
	
	public Map getWinningMap() {
		int highest = 0;
		ArrayList<Map> finalists = new ArrayList<Map>();
		Random rand = new Random();
		// Find the highest vote count
		for(int i = 0; i < votelist.size(); i++)
			if(votelist.get(i) > highest)
				highest = votelist.get(i);
		// Find all maps with the highest vote count
		for(int j = 0; j < votelist.size(); j++)
			if(votelist.get(j) == highest)
				finalists.add(maplist.get(j));
		// Pick a random map from the finalists
		Map winner = finalists.get(rand.nextInt(finalists.size()));
		// Announce the winner
		g.p.getServer().broadcastMessage(ChatColor.RED + "===================================================");
		g.p.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + winner.getName() + ChatColor.RESET + " has won! (" + highest + "/" + voters.size() + ")");
		g.p.getServer().broadcastMessage(ChatColor.GRAY + "---------------------------------------------------");
		g.p.getServer().broadcastMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + winner.getDescription());
		g.p.getServer().broadcastMessage(ChatColor.GOLD + "Author: " + ChatColor.RESET + winner.getAuthor());
		g.p.getServer().broadcastMessage(ChatColor.GOLD + "Version: " + ChatColor.RESET + winner.getVersion());
		g.p.getServer().broadcastMessage(ChatColor.RED + "---------------------------------------------------");
		// Return winning map
		return winner;
	}
	
	private void update() {
		// Check if minimum skip votes needed to skip is reached
		if((float) ((float)skipvotes / (float)g.pm.getNumberOfPlayers()) > .6) {
			// TODO
		}
	}
	
	class Announce implements Runnable {
		private VoteManager v;
		
		public Announce(VoteManager v) {
			this.v = v;
		}
		
		@Override
		public void run() {
			// TODO
		}
		
	}
}
