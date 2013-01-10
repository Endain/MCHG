package org.dotGaming.Endain.MCHG.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.dotGaming.Endain.MCHG.Core.Game;

//Contains events relating to environmental changes.
public class BlockListener implements Listener {
	private Game g;
	
	public BlockListener(Game g) {
		this.g = g;
	}
	
	@EventHandler
	public void onBlockEvent(BlockEvent event) {
		// Log blocks that change for any reason
		g.bm.log(event.getBlock());
		g.p.getLogger().info("Block Event logged");
		if(event instanceof BlockDamageEvent)
			g.p.getLogger().info("Block Damage Event logged");
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
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
	public void onBlockDamage(BlockDamageEvent event) {
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
}
