package com.bfg.veo.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.bfg.veo.Main;

public class EnvironmentManager {
	
	private static double dx, dy, dz;
	private static String state1, world;
	private static String[] states2 = {"", "", ""};
	public static HashMap<Block, Material> brokenBlocks = new HashMap<>();
	
	/* hey you
	 * yes you
	 * if you are reading this
	 * please dont complain
	 * idk what i wrote here either
	 * i mean i do know what i wrote but
	 * its just so
	 * disgusting
	 * i tried my best. am not the most experienced coder but
	 * i tried and hey
	 * it works!!!
	 * so yea <3
	 * */

	public static void breakFloor() {
		
		world = Main.world;
		
		double[] f1 = ConfigManager.getFloor1();
		double[] f2 = ConfigManager.getFloor2();
		
		Bukkit.broadcastMessage(f1[0] + " " + f1[1] + " " + f1[2] + " - " + f2[0] + " " + f2[1] + " " + f2[2]);
		
		if (Math.abs(f2[0]) > Math.abs(f1[0])) {
			
			dx = Math.abs(f2[0]) - Math.abs(f1[0]) + 1;
			states2[0] = "+";
			
		} else {
			
			dx = Math.abs(f1[0]) - Math.abs(f2[0]) + 1;
			states2[0] = "-";
			
		}
		if (Math.abs(f2[1]) > Math.abs(f1[1])) {
			
			dy = Math.abs(f2[1]) - Math.abs(f1[1]) + 1;
			states2[1] = "+";
			
		} else {
			
			dy = Math.abs(f1[1]) - Math.abs(f2[1]) + 1;
			states2[1] = "-";
			
		}
		if (Math.abs(f2[2]) > Math.abs(f1[2])) {
			
			dz = Math.abs(f2[2]) - Math.abs(f1[2]) + 1;
			states2[2] = "+";
			
		} else {
			
			dz = Math.abs(f1[2]) - Math.abs(f2[2]) + 1;
			states2[2] = "-";
			
		}
		if (dx == 0) { dx = 1; }
		if (dy == 0) { dy = 1; }
		if (dz == 0) { dz = 1; }
		
		// states[0] = x states[1] = z
		Bukkit.broadcastMessage(dx + "*" + dy + "*" + dz + "=" + (dx * dy * dz));
		if (f1[0] >= 0 && f1[2] >= 0) {
			
			state1 = "++";
			
		} else if (f1[0] < 0 && f1[2] < 0) {
			
			state1 = "--";
			
		} else if (f1[0] >= 0 && f1[2] < 0) {
			
			state1 = "+-";
			
		} else {
			
			state1 = "-+";
			
		}
		String[] states1 = state1.split("");
		
		for (double x = 0; x < dx; x++) {
				
			if (new Random().nextInt(3) == 1) {
					
					if (states1[0].equals("+")) {
						
						if (states2[0].equals("+")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1], f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1], f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						}
						
						
					} else {
						
						if (states2[0].equals("+")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1], f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1], f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						}
						
					}
					
			}
			for (double y = 0; y < dy; y++) {
					
				if (new Random().nextInt(3) == 1) {
					
					if (states1[0].equals("+")) {
						
						if (states2[0].equals("+") && states2[1].equals("+")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else if (states2[0].equals("+") && states2[1].equals("-")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else if (states2[0].equals("-") && states2[1].equals("-")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						}
						
					} else {
						
						if (states2[0].equals("+") && states2[1].equals("+")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else if (states2[0].equals("+") && states2[1].equals("-")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else if (states2[0].equals("-") && states2[1].equals("-")) {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						} else {
							
							Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2]);
							spawnFallingBlock(loc);
							if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
								
								brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
								
							}
							Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
							
						}
						
					}
						
				}
				for (double z = 0; z < dz; z++) {
						
					if (new Random().nextInt(3) == 1) {
							
						switch (state1) {
						
						case "++":
							
							if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							}
							
							break;
							
						case "--":
							
							if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							}
							
							break;
							
						case "+-":
							
							if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							}
							
							break;
							
						case "-+":
							
							if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("+") && states2[1].equals("-") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] - x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("+") && states2[2].equals("-")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] + y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else if (states2[0].equals("-") && states2[1].equals("-") && states2[2].equals("+")) {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] + z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							} else {
								
								Location loc = new Location(Bukkit.getWorld(world), f1[0] + x, f1[1] - y, f1[2] - z);
								spawnFallingBlock(loc);
								if (!Bukkit.getWorld(world).getBlockAt(loc).getType().equals(Material.AIR)) {
									
									brokenBlocks.put(Bukkit.getWorld(world).getBlockAt(loc), Bukkit.getWorld(world).getBlockAt(loc).getType());
									
								}
								Bukkit.getWorld(world).getBlockAt(loc).setType(Material.AIR);
								
							}
							
							break;
						
						}
				
					}
					
				}
				
			}
			
		}
			
	}
	
	//@SuppressWarnings("deprecation")
	public static void spawnFallingBlock(Location loc) {
		
		Block block = Bukkit.getWorld(world).getBlockAt(loc);
		loc.getWorld().spawnFallingBlock(loc, block.getType(), (byte) 0);
		
	}
	
	public static void resetFloor() {
		
		for (Map.Entry<Block, Material> b : brokenBlocks.entrySet()) {
			
			Bukkit.getWorld(world).getBlockAt(b.getKey().getLocation()).setType(b.getValue());
			
		}
		
	}
	
}
