package org.dotGaming.Endain.MCHG.Core.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Map.Map;

public class CommandManager implements CommandExecutor{
	private Game g;
	
	public CommandManager(Game g) {
		this.g = g;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("startLogging")) {
			g.bm.start();
			return true;
		} else if(command.getName().equalsIgnoreCase("stopLogging")) {
			g.bm.stop();
			return true;
		} else if(command.getName().equalsIgnoreCase("revert")) {
			g.bm.revert();
			return true;
		} else if(command.getName().equalsIgnoreCase("maps")) {
			ArrayList<Map> maps;
			maps = g.mm.getRandomMaps(1);
			for(int i = 0; i < maps.size(); i++) {
				sender.sendMessage("==============================================");
				sender.sendMessage("Name: " + maps.get(i).getName() + " (ID: " + maps.get(i).getId() + ")");
				sender.sendMessage("Author: " + maps.get(i).getAuthor());
				sender.sendMessage("Version: " + maps.get(i).getVersion());
				sender.sendMessage("Description: " + maps.get(i).getDescription());
				sender.sendMessage("Support: " + maps.get(i).getSupport());
				sender.sendMessage("Center: (" + maps.get(i).getX() + ", " + maps.get(i).getY() + ", " + maps.get(i).getZ() + ")");
				sender.sendMessage("Radius: " + maps.get(i).getRadius());
				sender.sendMessage("----------------------------------------------");
			}
			return true;
		}
		return false;
	}
}
