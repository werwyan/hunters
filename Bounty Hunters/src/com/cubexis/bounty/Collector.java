package com.cubexis.bounty;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.cubexis.plugin.OminousBounty;

public class Collector implements Runnable {
	
	private int task;
	private int start;
	private int end;
	private Skull skull;
	private double balance;
	private Player died;
	private UUID id;
	
	public Collector(Skull skull, Player died) {
		this.start = 0;
		this.end = OminousBounty.getBountyHunters().getConfig().getInt("time");
		this.skull = skull;
		this.died = died;
		this.id = UUID.randomUUID();
		
		balance = Math.round(OminousBounty.getEcononomy().getBalance((OfflinePlayer) died) * (OminousBounty.getBountyHunters().getConfig().getDouble("bounty") / 100));
		
		//TODO identify this bounty
		FixedMetadataValue meta = new FixedMetadataValue(OminousBounty.getBountyHunters(), 4320765);
		FixedMetadataValue block = new FixedMetadataValue(OminousBounty.getBountyHunters(), this.id);
		skull.setMetadata("432.lkjh.4krewqre.wq34.253.v98.xcv7", meta);
		skull.setMetadata("blockID", block);
	}
	
	public UUID getId() {
		return id;
	}

	public void cancel() {
		skull.getBlock().setType(Material.AIR);
		OminousBounty.removeCollector(this);
		Bukkit.getScheduler().cancelTask(task);
	}
	
	public void start() {
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(OminousBounty.getBountyHunters(), this, 20L, 20L);
	}
	
	public static boolean collected(UUID id) {
		
		for (Collector c : OminousBounty.getCollectors()) {
			if (c.getDied().getUniqueId() == id) return true;
		}
		
		return false;
	}
	
	public void run() {
		
		
		
		start++;
		
		if (start > end) {
			skull.getBlock().setType(Material.AIR);
			OminousBounty.removeCollector(this);
			Bukkit.getScheduler().cancelTask(task);
		}
	}
	
	public Skull getSkull() {
		return skull;
	}


	public void setSkull(Skull skull) {
		this.skull = skull;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}


	public Player getDied() {
		return died;
	}


	public void setDied(Player died) {
		this.died = died;
	}

}
