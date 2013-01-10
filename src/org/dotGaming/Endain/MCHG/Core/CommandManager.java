package org.dotGaming.Endain.MCHG.Core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dotGaming.Endain.MCHG.Core.Game;

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
		}
		return false;
	}
}
