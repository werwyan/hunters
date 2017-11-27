package com.cubexis.commands;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubexis.bounty.Collector;

public class Commands implements CommandExecutor 
{
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) 
	{
		if (sender instanceof Player)
		{  
			Player player = (Player) sender;
			
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("top")) {
					if (player.hasPermission("bounty.top")) {
						List<Entry<OfflinePlayer, Integer>> count = Collector.count();
						
						player.sendMessage(ChatColor.GREEN + "Top bounties:");
						
						for (int i = 0; i < count.size(); i++) {
							Entry<OfflinePlayer, Integer> entry = count.get(i);
							player.sendMessage(ChatColor.GREEN + entry.getKey().getName() + ": " + entry.getValue());
						}
						
						if (args.length > 1) {
							player.sendMessage(ChatColor.RED + "Error. Syntax: /bounty top");
						}
					} else {
						player.sendMessage(ChatColor.RED + "You aren't allowed to use this command!");
					}
				}
			}
			return true;
		}
		sender.sendMessage("You must be a player to use this command!");
		return false;
	}
}
