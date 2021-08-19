package com.bfg.veo;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.bfg.veo.commands.SetupCommand;
import com.bfg.veo.commands.StartCommand;
import com.bfg.veo.managers.ConfigManager;
import com.bfg.veo.managers.Sequences;

public class Main extends JavaPlugin {
	
	public static int state = 0;
	private static Main main;
	public static String world;
	
	public void onEnable() {
		
		this.getCommand("startboss").setExecutor(new StartCommand());
		this.getCommand("setup").setExecutor(new SetupCommand());
		File configFile;
		configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()){
			this.getConfig().options().copyDefaults();
		}
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		main = this;
		world = ConfigManager.getWorld();
		
		System.out.println("Plugin started up successfully!");
		System.out.println(world);
		
	}
	
	public void onDisable() {
		
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "function herobrine:remove/all_models");
		Sequences.reset();
		System.out.println("Reset successful!");
		
	}
	
	public static Main getMain() { return main; }

}
