package com.cubexis.bounty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.cubexis.plugin.Configuration;
import com.cubexis.plugin.Main;

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
		this.end = Main.getMain().getConfig().getInt("time");
		this.skull = skull;
		this.died = died;
		this.id = UUID.randomUUID();
		
		balance = Math.floor(Main.getEcononomy().getBalance((OfflinePlayer) died) * (Main.getMain().getConfig().getDouble("bounty") / 100));
		
		FixedMetadataValue meta = new FixedMetadataValue(Main.getMain(), 4320765);
		FixedMetadataValue block = new FixedMetadataValue(Main.getMain(), this.id);
		skull.setMetadata("432.lkjh.4krewqre.wq34.253.v98.xcv7", meta);
		skull.setMetadata("blockID", block);
	}
	
	public UUID getId() {
		return id;
	}
	
	public void collect(Player p) {
		Configuration c = Main.getMain().getAConfig();
		c.loadFile(c.getData());
		YamlConfiguration yaml = c.getYaml();
		
		if (!yaml.contains(p.getUniqueId().toString())) {
			yaml.set(p.getUniqueId().toString(), 1);
			c.saveFile(c.getData());
		} else {
			int count = yaml.getInt(p.getUniqueId().toString());
			yaml.set(p.getUniqueId().toString(), count + 1);
			c.saveFile(c.getData());
		}
	}
	
	public static List<Entry<OfflinePlayer, Integer>> count() {
		Configuration c = Main.getMain().getAConfig();
		YamlConfiguration yaml = c.getYaml();
		c.loadFile(c.getData());
		HashMap<OfflinePlayer, Integer> count = new HashMap<>();
		Set<String> keys = yaml.getKeys(false);
		
		for (String k : keys) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(k));
			int value = yaml.getInt(k);
			
			count.put(player, value);
		}
		return sort(count);
	}
	
	private static <K,V extends Comparable<? super V>> 
	  List<Entry<K, V>> sort(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
		  new Comparator<Entry<K,V>>() {
		      @Override
		      public int compare(Entry<K,V> e1, Entry<K,V> e2) {
		          return e2.getValue().compareTo(e1.getValue());
		      }
		  }
		);

		return sortedEntries;
	}

	public void cancel() {
		skull.getBlock().setType(Material.AIR);
		Main.removeCollector(this);
		Bukkit.getScheduler().cancelTask(task);
	}
	
	public void start() {
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), this, 20L, 20L);
	}
	
	public static boolean collected(UUID id) {
		
		for (Collector c : Main.getCollectors()) {
			if (c.getDied().getUniqueId() == id) return true;
		}
		
		return false;
	}
	
	public void run() {
		
		
		
		start++;
		
		if (start > end) {
			skull.getBlock().setType(Material.AIR);
			Main.removeCollector(this);
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
