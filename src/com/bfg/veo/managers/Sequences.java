package com.bfg.veo.managers;

import org.bukkit.Bukkit;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;

public class Sequences {
	
	public static void start(long delay, double x, double y, double z) {
		
		if (BossFight.running) {
			
			Bukkit.broadcastMessage("Boss already running! If you wish to run it again. Please do /setup reset and then run it again!");
			return;
		
		}
		Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
			
			public void run() {
				
				BossFight.spawn(x, y, z);
				BossFight.pulsate();
				
			}
			
		}, (delay * 20L));
		
	}
	
	public static void end() {
		
		BossFight.running = false;
		BossFight.bossBar.removeAll();
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "function herobrine:remove/all_models:");
		BossFight.floatCloud.kill();
		EnvironmentManager.resetFloor();
		//Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "plugman reload BossFightPlugin");
		//Bukkit.getPluginManager().disablePlugin(Main.getMain());
		
	}
	
	public static void reset() {
		
		BossFight.bossBar.removeAll();
		EnvironmentManager.resetFloor();
		
	}

}
