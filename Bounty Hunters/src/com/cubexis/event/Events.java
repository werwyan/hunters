package com.cubexis.event;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import com.cubexis.bounty.Collector;
import com.cubexis.plugin.Main;

public class Events implements Listener {
	
	@EventHandler
	public void on(PlayerDeathEvent event) {
		if (event.getEntity() != null) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				
				UUID id = player.getUniqueId();
				
				if (!Collector.collected(id)) {
					// Add skull to variable in main
					Location loc = player.getLocation();
					Block sk = loc.getBlock();
					sk.setType(Material.SKULL);
					Skull skull = (Skull) sk.getState();
					skull.setOwningPlayer( (OfflinePlayer) player );
					skull.update();
					 
					Collector collector = new Collector(skull, player);
					collector.start();
					Main.addCollector(collector);
				}
			}
		}
	}
	
	@EventHandler
	public void b(PlayerInteractEvent event) {
		if (event.getPlayer() != null) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (event.getClickedBlock().getType() == Material.SKULL) {
					// If this skull has bounty on it
					if (event.getClickedBlock().hasMetadata(Main.getCollectorid().toString())) {
						// If the player is allowed to collect the bounty of this skull
						if (event.getPlayer().hasPermission("bounty.collect")) {
							// Loop through all skulls that can be collected
							for (Collector collector : Main.getCollectors()) {
								List<MetadataValue> meta = event.getClickedBlock().getMetadata("blockID");
								UUID id = null;
								
								// If this block UUID is equal to the collectors block UUID
								for (MetadataValue mv : meta) {
									id = (UUID) mv.value(); break;
								}
								
								
								if (id != (collector.getId())) continue;
								
								event.getPlayer().sendMessage("here");
								
								// Prevent player from receiving his own bounty
								if (event.getPlayer() != collector.getDied()) {
									event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', 
											String.format(Main.getMain().getConfig().getString("collect"), 
											collector.getDied().getName(), (double) Math.round(collector.getBalance()))));
									
									collector.collect(event.getPlayer());
									Main.getEcononomy().depositPlayer(event.getPlayer(), collector.getBalance());
									Main.getEcononomy().withdrawPlayer(collector.getDied(), collector.getBalance());
									
									collector.cancel();
									return;
								} 
								else {
									event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', 
											String.format(Main.getMain().getConfig().getString("secure"), 
											event.getPlayer().getName(), (double) Math.round(collector.getBalance()))));
									
									event.getClickedBlock().setType(Material.AIR);
									collector.cancel(); return;
								}
							}
						}
					}
				}
			}
		}
	}
}
