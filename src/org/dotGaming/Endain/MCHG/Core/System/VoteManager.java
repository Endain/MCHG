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
	private int task;
	private int count;
	private boolean open;
	
	public VoteManager(Game g) {
		this.g = g;
		this.maplist = new ArrayList<Map>();
		this.votelist = new ArrayList<Integer>();
		this.voters = new ArrayList<Tribute>();
		this.skipvotes = 0;
		this.skips = 0;
		this.task = -1;
		this.count = 0;
		this.open = false;
	}
	
	public void startVote() {
		if(!open) {
			// Declare voting open
			open = true;
			// Clear the old data
			maplist.clear();
			votelist.clear();
			voters.clear();
			skipvotes = 0;
			count = 0;
			// Get a new maplist and initialize votes
			maplist = g.mm.getRandomMaps(3);
			for(int i = 0; i < maplist.size(); i++)
				votelist.add(0);
			// Spawn announcer/timer task
			task = g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Announce());
		}
	}
	
	public void stopVote(boolean clearskips) {
		// Disallow any more votes
		if(open) {
			open = false;
			// Should we reset the skip votes?
			if(clearskips)
				skips = 0;
		}
	}
	
	public void castVote(Tribute t, int map) {
		// Make sure voting is open
		if(open) {
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
		} else
			t.p.sendMessage(ChatColor.RED + "Voting is currently not allowed!");
	}
	
	public void castSkip(Tribute t) {
		// Make sure voting is open
		if(open) {
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
		} else
			t.p.sendMessage(ChatColor.RED + "Voting is currently not allowed!");
	}
	
	public Map getWinningMap() {
		// Make some temporary variables
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
			// Cancel any impending announce tasks
			g.p.getServer().getScheduler().cancelTask(task);
			// Stop voting
			stopVote(false); // Don't clear skips
			// Increment number of successful skips
			skips++; 
			// Announce a successful skip vote
			g.p.getServer().broadcastMessage(ChatColor.RED + "---------------------------------------------------");
			g.p.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "The skip vote has passed! " + ChatColor.RESET + "(" + skipvotes + "/" + g.pm.getNumberOfPlayers() + ")");
			g.p.getServer().broadcastMessage("New maps will be selected in 5 seconds. " + skips + "/2 skips used.");
			g.p.getServer().broadcastMessage(ChatColor.RED + "---------------------------------------------------");
			// Schedule a skip task 5 seconds out
			task = g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Skip(), 100);
		}
	}
	
	public void kill() {
		// Clear all lists
		maplist.clear();
		votelist.clear();
		voters.clear();
	}
	
	class Announce implements Runnable {
		@Override
		public void run() {
			if(count <= 6) {
				// Find minutes and seconds left
				int minutes = (120 - count * 20) / 60;
				int seconds = (120 - count * 20) % 60;
				// Announce progress update
				g.p.getServer().broadcastMessage(ChatColor.RED + "----------------------------------------------------");
				g.p.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY + minutes + " minute(s) and " + seconds + " seconds(s)" + ChatColor.RESET + "until voting closes!");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "---------------------------------------------------");
				for(int i = 0; i < votelist.size(); i++)
					g.p.getServer().broadcastMessage((i + 1) + ".) " + maplist.get(i).getName() + ChatColor.GRAY + " (" + votelist.get(i) + " votes!) " + ChatColor.GOLD + "(/v " + (i + 1) + " to vote!)");
				g.p.getServer().broadcastMessage(skipvotes + " skip votes! " + (int)((float)g.pm.getNumberOfPlayers() * .6) + " votes needed to skip!");
				g.p.getServer().broadcastMessage(ChatColor.RED + "---------------------------------------------------");
				// Increment count
				count++;
				// Schedule next announcement
				task = g.p.getServer().getScheduler().scheduleSyncDelayedTask(g.p, new Announce(), 400);
			} else {
				
			}
		}
		
	}
	
	class Skip implements Runnable {
		@Override
		public void run() {
			// Restart the the voting process
			startVote();
		}
		
	}
}
