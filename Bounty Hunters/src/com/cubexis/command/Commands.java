package com.cubexis.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) 
	{
		if (sender instanceof Player)
		{  
			Player player = (Player) sender;
			player.sendMessage("The /destroy command");
		
			return true;
		}
		
		sender.sendMessage("You must be a player to use this command!");
		return false;
	}
}
