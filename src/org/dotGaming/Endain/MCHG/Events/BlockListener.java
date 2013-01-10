package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.dotGaming.Endain.MCHG.Core.Game;

//Contains events relating to environmental changes.
public class BlockListener implements Listener {
	private Game g;
	
	public BlockListener(Game g) {
		this.g = g;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
		// Only allow the following blocks to be broken:
		// Leaves/Vines/Grass/Mushrooms/Wheat/Flowers/Shrubs
		Block b = event.getBlock();
		if(b.getType() != Material.LEAVES)
			if(b.getType() != Material.VINE)
				if(b.getType() != Material.LONG_GRASS)
					if(b.getType() != Material.RED_MUSHROOM)
						if(b.getType() != Material.BROWN_MUSHROOM)
							if(b.getType() != Material.RED_ROSE)
								if(b.getType() != Material.YELLOW_FLOWER)
									if(b.getType() != Material.WHEAT)
										if(b.getType() != Material.DEAD_BUSH)
											event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
		// Only allow the following blocks to be damaged:
		// Leaves/Vines/Grass/Mushrooms/Wheat/Flowers/Shrubs
		Block b = event.getBlock();
		if(b.getType() != Material.LEAVES)
			if(b.getType() != Material.VINE)
				if(b.getType() != Material.LONG_GRASS)
					if(b.getType() != Material.RED_MUSHROOM)
						if(b.getType() != Material.BROWN_MUSHROOM)
							if(b.getType() != Material.RED_ROSE)
								if(b.getType() != Material.YELLOW_FLOWER)
									if(b.getType() != Material.WHEAT)
										if(b.getType() != Material.DEAD_BUSH)
											event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockExp(BlockExpEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onEntityBlockform(EntityBlockFormEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		// Log block event 
		g.bm.log(event.getBlock());
	}
}
