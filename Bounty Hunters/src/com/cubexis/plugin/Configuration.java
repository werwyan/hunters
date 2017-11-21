package com.cubexis.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Configuration {
	
	/**
	 * Required for additional functionality
	 */
	private YamlConfiguration yaml = new YamlConfiguration();
	
	/**
	 * Plugin associated with this class
	 */
	private Plugin plugin = null;
	
	/**
	 * Main configuration file of this plugin
	 */
	private File config;
	
	/**
	 * File containing all data regarding this plugin
	 */
	private File data;
	
	/**
	 * All files that will be created for this plugin
	 */
	private List<File> files = new ArrayList<File>();
	
	/**
	 * Creates all files that this plugin requires to run
	 */
	public Configuration(Plugin plugin)
	{
		this.plugin = plugin;

		this.config  = new File(plugin.getDataFolder(), "config.yml");
		this.data = new File(plugin.getDataFolder(), "data.yml");
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}

		files.add(config);
		files.add(data);

		createFiles(files, true);
	}

	/**
	 * Creates all files from array
	 * @param files files you want to create
	 */
	public void createFiles(File files[])
	{
		for (File file : files)
		{
			if (!file.exists())
			{
				Bukkit.getLogger().info(file.getName() + " not found, creating new config file...");
				
				try 
				{
					file.createNewFile();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param files files you want to create
	 * @param custom set true if you want to use files from your workspace
	 */
	public void createFiles(List<File> files, boolean custom)
	{
		if (custom) {
			for (File file : files) 
			{
				if (!file.exists())	plugin.saveResource(file.getName(), false);
			}
		} else {
			for (File file : files) {
				if (!file.exists()) {
					try 
					{
						file.createNewFile();
					} 
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Creates a new configuration section in an existing file
	 * @param File file where to create the section
	 * @param String section name of the section
	 */
	public void createSection(File file, String section) {
		try 
		{
			// Throw exception if file doesn't exist
			if (file.exists()) {
				yaml.load(file);
				// If specified section doesn't exist, throw null pointer exception
				if (yaml.getConfigurationSection(section) == null) {
					yaml.createSection(section);
					yaml.save(file);
				} else {
					throw new NullPointerException(section + " does not exist in " + file.getName());
				}
			} else {
				throw new NoSuchFileException(null);
			}
		} 
		catch (IOException | InvalidConfigurationException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets an existing configuration section in an existing file
	 * @param File file where to create the section
	 * @param String section name of the section
	 */
	public ConfigurationSection getSection(File file, String section) {
		try 
		{
			// Throw exception if file doesn't exist
			if (file.exists()) {
				yaml.load(file);
				// Throw exception if section doesn't exist
				if (yaml.getConfigurationSection(section) == null) return null;
			} else {
				throw new NoSuchFileException(null);
			}
		} 
		catch (IOException | InvalidConfigurationException e) 
		{
			e.printStackTrace();
		}
		
		return yaml.getConfigurationSection(section);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public YamlConfiguration getYaml() {
		return this.yaml;
	}
	
	public void loadFile(File file) {
		try {
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFile(File file) {
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
