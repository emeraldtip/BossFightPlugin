package com.bfg.veo.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;
import com.bfg.veo.managers.ConfigManager;
import com.bfg.veo.managers.MovementManager;
import com.bfg.veo.managers.Sequences;

import net.md_5.bungee.api.ChatColor;

public class SetupCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			if (args.length >= 1) {
				
				switch (args[0]) {
				
				case "floor":
					
					Main.state = 1;
					ItemStack stick = new ItemStack(Material.STICK);
					ItemMeta stickMeta = stick.getItemMeta();
					stickMeta.setDisplayName("Select Floor");
					stick.setItemMeta(stickMeta);
					player.getInventory().addItem(stick);
					player.sendMessage(ChatColor.GREEN + "You are now in select mode! (FLOOR) Click LMB to select POS1, click RMB to select POS2!");
					
					break;
					
				case "area":
					
					Main.state = 2;
					ItemStack rod = new ItemStack(Material.BLAZE_ROD);
					ItemMeta rodMeta = rod.getItemMeta();
					rodMeta.setDisplayName("Select Area");
					rod.setItemMeta(rodMeta);
					player.getInventory().addItem(rod);
					player.sendMessage(ChatColor.GREEN + "You are now in select mode! (AREA) Click LMB to select POS1, click RMB to select POS2!");
					
					break;
					
				case "reset":
					
					Sequences.reset();
					Bukkit.broadcastMessage(ChatColor.RED + "Herobrine Boss Plugin is resetting! Please do not turn off/shut down the server!");
					
					break;
					
				case "world":
					
					ConfigManager.editWorld("world", player.getWorld().getName());
					player.sendMessage(ChatColor.GREEN + "World set to: " + player.getWorld().getName());
					
					break;
					
				case "start":
					
					ConfigManager.editLocs("StartX", args[1]);
					ConfigManager.editLocs("StartY", args[2]);
					ConfigManager.editLocs("StartZ", args[3]);
					player.sendMessage(ChatColor.GREEN + "Start location successfully set!");
					
					break;
					
				case "delay":
					
					ConfigManager.editLocs("Delay", args[1]);
					
					break;
					
				case "a":
					
					Bukkit.getServer().broadcastMessage(BossFight.targets.toString());
					
					break;
					
				case "test":
					
					switch (args[1]) {
					
					case "attack":
						
						int i = Integer.parseInt(args[2]);
						switch (i) {
						
						case 1:
							
							BossFight.a1();
							
							break;
							
						case 2:
							
							BossFight.a2();
							
							break;
							
						case 3:
							
							BossFight.a3();
							
							break;
							
						case 4:
							
							BossFight.a4();
							
							break;
							
						case 5:
							
							BossFight.a5();
							
							break;
					
						}
				case "animate":
					
					String f = args[2];
					switch (f) {
					
					case "throw":
						
						MovementManager.playAnimation("throw");
						
						break;
						
					case "death":
						
						MovementManager.playAnimation("death");
						
						break;
						
					case "heal":
						
						MovementManager.playAnimation("heal");
						
						break;
						
					}
						break;
						
					default:
						
						break;
					
					}
					
					break;
					
				default:
					
					Main.state = 0;
					player.sendMessage(ChatColor.GREEN + "No such argument found as '" + args[0] + "'. Selection mode turned off!");
					break;
				
				}
				
			} else {
				
				Main.state = 0;
				player.sendMessage(ChatColor.GREEN + "Please enter an argument. Selection mode turned off!");
				
			}
			
			
		}
		
		return false;
		
	}

}
