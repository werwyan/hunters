package com.cubexis.plugin;

import java.util.HashMap;
import org.bukkit.block.Skull;
import org.bukkit.plugin.java.JavaPlugin;

import com.cubexis.event.Events;


public class OminousBounty extends JavaPlugin {
	
	private static OminousBounty instance = null;
	private HashMap<Integer, Skull> skulls;
	
	@Override 
	public void onEnable()
	{
		
		//getCommand("destroy").setExecutor(new Commands());
		
		instance = this;
		Configuration config = new Configuration(this);
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public void addSkull(int task, Skull skull) {
		skulls.put(task, skull);
	}
	
	public HashMap<Integer, Skull> getSkulls() {
		return skulls;
	}
	
	/**
	 * Get the main class of the Mercatus plugin
	 * @return Mercatus instance
	 */
	public static OminousBounty getBountyHunters() {
		return instance;
	}
}