package org.dotGaming.Endain.MCHG.Core.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Map.Map;
import org.dotGaming.Endain.MCHG.Core.Player.Tribute;

public class CommandManager implements CommandExecutor{
	private Game g;
	
	public CommandManager(Game g) {
		this.g = g;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Only handle commands from players
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(command.getName().equalsIgnoreCase("startLogging")) {
				// Start logging block changes
				g.bm.start();
				return true;
			} else if(command.getName().equalsIgnoreCase("stopLogging")) {
				// Stop logging block changes
				g.bm.stop();
				return true;
			} else if(command.getName().equalsIgnoreCase("revert")) {
				// Revert logged block changes
				g.bm.revert();
				return true;
			} else if(command.getName().equalsIgnoreCase("maps")) {
				// List a random map's details
				ArrayList<Map> maps;
				maps = g.mm.getRandomMaps(1);
				for(int i = 0; i < maps.size(); i++) {
					p.sendMessage("==============================================");
					p.sendMessage("Name: " + maps.get(i).getName() + " (ID: " + maps.get(i).getId() + ")");
					p.sendMessage("Author: " + maps.get(i).getAuthor());
					p.sendMessage("Version: " + maps.get(i).getVersion());
					p.sendMessage("Description: " + maps.get(i).getDescription());
					p.sendMessage("Support: " + maps.get(i).getSupport());
					p.sendMessage("Center: (" + maps.get(i).getX() + ", " + maps.get(i).getY() + ", " + maps.get(i).getZ() + ")");
					p.sendMessage("Radius: " + maps.get(i).getRadius());
					p.sendMessage("----------------------------------------------");
				}
				return true;
			} else if(command.getName().equalsIgnoreCase("v")) {
				// Cast a map vote
				Tribute t = g.pm.getTribute(p);
				// Make sure we have a valid tribute
				if(t != null && args.length > 0) {
					g.vm.castVote(t, Integer.parseInt(args[0]));
					return true;
				}
			} else if(command.getName().equalsIgnoreCase("skip")) {
				// Cast a skip vote
				Tribute t = g.pm.getTribute(p);
				// Make sure we have a valid tribute
				if(t != null) {
					g.vm.castSkip(t);
					return true;
				}
				return true;
			}
		}
		return false;
	}
}
