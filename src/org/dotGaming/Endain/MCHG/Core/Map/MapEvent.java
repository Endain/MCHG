package org.dotGaming.Endain.MCHG.Core.Map;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.dotGaming.Endain.Chronos.Chronos;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

// This class will run the map's random event
public class MapEvent implements Runnable, Listener{
	private Game g;
	private int delay = 0;
	private Random rand;
	// 1 - First refill cancelled
	// 2 - Second refill cancelled
	// 3 - Heavy fog
	// 4 - Stormy weather
	// 5 - Mutt invasion
	private int event = 0;
	private BukkitTask recurringEffect;
	private BukkitTask endEffect;
	private static MapEvent instance;
	private static final int muttGroupSize = 3; // Is actually this value + 1
	private int muttCount;
	
	public MapEvent(Game g) {
		this.g = g;
		this.instance = this;
		this.rand = new Random();
		// Initialize event to nothing by default
		event = 0;
		delay = 0;
		// Choose a random event to happen
		float val = rand.nextFloat();
		if(val > 0.0 && val < .10) {
			// First refill cancelled
			event = 1;
			delay = 8400;
		} else if(val > .10 && val < .20) {
			// Second refill cancelled
			event = 2;
			delay = 24000;
		} else if(val > .20 && val < .30) {
			// Heavy fog
			event = 3;
			delay = rand.nextInt(24000) + 2400;
		} else if(val > .30 && val < .40) {
			// Stormy weather
			event = 4;
			delay = rand.nextInt(18000) + 1200;
		}
		// DEBUG
		event = 5;
		delay = 120;
		Chronos.setTicks(13800);
	}
	
	public int getDelay() {
		return delay;
	}
	
	@EventHandler
    public void onMobspawn(CreatureSpawnEvent event) {
    	// Only allow zombies to spawn
		if(event.getEntityType() == EntityType.ZOMBIE) {
			g.p.getServer().getLogger().info("Spawn event! (Zombie)");
			// Add potion effects
			if(rand.nextFloat() > .5) {
				event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, rand.nextInt(3)));
				event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 0));
			} else {
				event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0));
				event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, rand.nextInt(3)));
			}
			// Recusively spawn a group
			if(muttCount < muttGroupSize) {
				muttCount++;
				g.w.spawnEntity(event.getEntity().getLocation(), EntityType.ZOMBIE);
			} else {
				muttCount = 0;
			}
			
		} else if(event.getEntityType() == EntityType.PLAYER) {
			// Allow players to spawn
		} else {
			g.p.getServer().getLogger().info("Spawn event! (Other)");
			event.setCancelled(true);
		}
    }

	@Override
	public void run() {
		if(event == 1) { // Cancel first refill
			g.p.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Attention tributes!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "It has been decided that tonight's chest refill will be cancelled!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "May the odds be ever in your favor!");
			g.pm.playSoundEffectTributes(Sound.PORTAL_TRAVEL, .5f, 1);
			g.mm.cancelRefills(true, false);
		} else if(event == 2) { // Cancel second refill
			g.p.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Attention tributes!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "It has been decided that tonight's chest refill will be cancelled!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "May the odds be ever in your favor!");
			g.pm.playSoundEffectTributes(Sound.PORTAL_TRAVEL, .5f, 1);
			g.mm.cancelRefills(false, true);
		} else if(event == 3) { // Heavy fog
			g.p.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Attention tributes!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You feel a strange chill as everything around you falls silent.");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You notice that a thick fog has closed in around you.");
			g.pm.playSoundEffectTributes(Sound.PORTAL_TRAVEL, .5f, 1);
			g.pm.addPotionEffectTributes(new PotionEffect(PotionEffectType.BLINDNESS, 2400, 0));
			// Schedule end task
			g.p.getServer().getScheduler().runTaskLater(g.p, new End(), 2400);
		} else if(event == 4) { // Stormy weather
			g.p.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Attention tributes!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "There is a large storm entering the area!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Do try not to catch a cold.");
			g.pm.playSoundEffectTributes(Sound.PORTAL_TRAVEL, .5f, 1);
			g.w.setStorm(true);
			g.w.setWeatherDuration(9460);
			// Schedule recurring task
			recurringEffect = g.p.getServer().getScheduler().runTaskLater(g.p, new Recurring(), 600);
			// Schedule end task
			endEffect = g.p.getServer().getScheduler().runTaskLater(g.p, new End(), 9600);
		} else if(event == 5) { // Mutt invasion
			g.p.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Attention tributes!");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You hear a disturbing howl off in the distance.");
			g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You have a strange feeling that you are being hunted.");
			g.pm.playDistantSoundEffectTributes(Sound.WOLF_HOWL, .75f, .75f);
			// Register spawn event and set up server to spawn
			muttCount = 0;
			g.w.setSpawnFlags(true, false);
			g.w.setTicksPerMonsterSpawns(200);
			g.p.getServer().getPluginManager().registerEvents(this, g.p);
			// Schedule end task
			endEffect = g.p.getServer().getScheduler().runTaskLater(g.p, new End(), 2000);
		}
	}
	
	private class Recurring implements Runnable {
		@Override
		public void run() {
			if(event == 4) { // Stormy Weather
				Iterator<Tribute> iter = g.pm.getTributeIterator();
				while(iter.hasNext()) {
					Player p = iter.next().p;
					float f = rand.nextFloat();
					if(f < .33) {
						Location loc = p.getLocation();
						World w = loc.getWorld();
						if (w.getHighestBlockYAt(loc) < loc.getY()) {
							g.pm.addPotionEffectTributes(new PotionEffect(PotionEffectType.WEAKNESS, 600, 0));
							g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You feel cold and clamy. A mild sickness creeps in.");
							g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Perhaps you should try to stay out of the rain.");
							g.pm.playSoundEffectTributes(Sound.AMBIENCE_CAVE, .5f, 1);
						}
					}
				}
				// Reschedule continuing effect
				g.p.getServer().getScheduler().runTaskLater(g.p, new Recurring(), 600);
			}
		}
		
	}
	
	private class End implements Runnable {
		@Override
		public void run() {
			if(recurringEffect != null)
				g.p.getServer().getScheduler().cancelTask(recurringEffect.getTaskId());
			// Give a finishing message to notify of event closure
			if(event == 3) { // Heavy fog
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "The heavy fog leaves as swiftly as it came.");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You exhale deeply with a feeling of relief.");
				g.pm.playSoundEffectTributes(Sound.BREATH, .35f, .5f);
			} else if(event == 4) { // Stormy weather
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "The rain subsides almost instantly.");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "A new warmth permeates your body.");
				g.pm.playSoundEffectTributes(Sound.BREATH, .35f, .5f);
				g.w.setStorm(false);
			} else if(event == 5) { // Mutt invasion
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You hear leaves rustling around you.");
				g.p.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "The noise slowly fades and you feel alone again.");
				g.pm.playSoundEffectTributes(Sound.BREATH, .35f, .5f);
				// Disable any more spawning
				g.w.setSpawnFlags(false, false);
				if(instance != null)
					HandlerList.unregisterAll(instance);
				// Remove any remaining zombies
				g.mm.removeNonPlayers();
				g.pm.playDistantSoundEffectTributes(Sound.WOLF_HOWL, .75f, .75f);
			}
		}
		
	}
	
}
