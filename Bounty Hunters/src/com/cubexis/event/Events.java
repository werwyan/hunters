package com.cubexis.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.cubexis.plugin.OminousBounty;

public class Events implements Listener, Runnable {
	
	private int task;
	private int delay = 0;
	private Location location;
	private double balance;
	
	@EventHandler
	public void on(PlayerDeathEvent event) {
		if (event.getEntity() != null) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				
				player.sendMessage("Oh no, you just died");
				
				// Add skull to variable in main
				Location loc = player.getLocation();
				Block sk = loc.getBlock();
				sk.setType(Material.SKULL);
				Skull skull = (Skull) sk.getState();
				skull.setOwningPlayer( (OfflinePlayer) player );
				skull.update();
				
				// Set timer runnable to remove skull after amount of time defined in config
				location = skull.getLocation();
				task = Bukkit.getScheduler().scheduleSyncRepeatingTask(OminousBounty.getBountyHunters(), this, 20L, 20L);
			}
		}
	}

	@Override
	public void run() {
		
		delay++;
		
		Bukkit.getServer().broadcastMessage("x: " + delay + " | task: " + task);
		
		if (delay > 10) {
			location.getBlock().setType(Material.AIR);
			Bukkit.getScheduler().cancelTask(task);;
		}
	}
	
}
