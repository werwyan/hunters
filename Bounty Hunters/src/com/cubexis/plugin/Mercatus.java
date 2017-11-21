package com.cubexis.plugin;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

import com.cubexis.command.Commands;


public class Mercatus extends JavaPlugin {
	
	private static Mercatus instance = null;
	public File config = new File(getDataFolder(), "config.yml");
	
	@Override 
	public void onEnable()
	{
		getCommand("destroy").setExecutor(new Commands(this));
		
		instance = this;
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	/**
	 * Get the main class of the Mercatus plugin
	 * @return Mercatus instance
	 */
	public static Mercatus getMercatus() {
		return instance;
	}
}