package org.dotGaming.Endain.MCHG.Core.Map;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

// Manages changes to blocks on the server and can revert those blocks to their earliest state.
public class BlockManager implements Manager{
	private Game g;
	private boolean tracking;
	private HashMap<Location, BlockState> blocks;
	
	public BlockManager(Game g) {
		// Initialize block manager
		this.g = g;
		this.blocks = new HashMap<Location, BlockState>();
		this.tracking = false;
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
		// Clear the list of blocks tracked
		blocks.clear();
	}
	
	public void start() {
		// Start tracking if we were no previously
		if(!tracking)
			tracking = true;
	}
	
	public void stop() {
		// Stop tracking if we were previously
		if(tracking)
			tracking = false;
	}
	
	public void revert() {
		// Revert all blocks that have changes to their original state
		Iterator<BlockState> i = blocks.values().iterator();
		// Init a counter to see how many block get reverted
		int b = 0;
		while(i.hasNext()) {
			// Apply the old state to the block
			if(i.next().update(true))
				b++; // Increment counter
		}
		// Log message stating how many blocks were successfully restored
		g.p.getLogger().info(b + "/" + blocks.size() + " blocks restored to their original state!");
		// Clear the blocks hashmap
		blocks.clear();
	}
	
	public void log(Block b) {
		// Check if we should log
		if(tracking) {
			// Log a block change
			if(!blocks.containsKey(b.getLocation())) {
				blocks.put(b.getLocation(), b.getState());
				g.p.getLogger().info("Block change logged!"); // DEBUG
			}
		}
	}
}
