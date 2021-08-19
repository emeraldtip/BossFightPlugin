package com.bfg.veo.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class FunctionManager {
	
	public static void runFunction(String function, Entity entity) {
		
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute as @e[tag=aj.herobrine.root_entity] at @s run function herobrine:" + function);
		
	}

}
