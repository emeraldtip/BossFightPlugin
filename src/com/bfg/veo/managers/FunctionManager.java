package com.bfg.veo.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class FunctionManager {
	
	public static void runFunction(String function, Entity entity) {
		
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute as " + entity.getUniqueId().toString() + " at " + entity.getUniqueId().toString() + " run function herobrine:" + function);
		
	}

}
