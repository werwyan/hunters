package com.cubexis.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.cubexis.bounty.Collector;
import com.cubexis.commands.Commands;
import com.cubexis.event.Events;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
	
	private static Main instance = null;
	private static List<Collector> collectors = new ArrayList<>();
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private Configuration config;
	
	@Override 
	public void onEnable()
	{
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		instance = this;
		setConfig(new Configuration(this));
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		getCommand("bounty").setExecutor(new Commands());
	}
	
	@Override
	public void onDisable()
	{
	      log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static Economy getEcononomy() {
        return econ;
    }
	
	public static void addCollector(Collector col) {
		collectors.add(col);
	}
	
	public static void removeCollector(Collector col) {
		collectors.remove(col);
	}
	
	public static List<Collector> getCollectors() {
		return collectors;
	}
	
	/**
	 * Get the main class of the OminousBounty plugin
	 * @return OminousBounty instance
	 */
	public static Main getMain() {
		return instance;
	}

	public Configuration getAConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}
}