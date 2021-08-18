package com.bfg.veo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bfg.veo.managers.ConfigManager;
import com.bfg.veo.managers.Sequences;

public class StartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		double[] locs = ConfigManager.getStartLocs();
		Sequences.start(ConfigManager.getDelay(), locs[0], locs[1], locs[2]);
		return false;
		
	}

}
