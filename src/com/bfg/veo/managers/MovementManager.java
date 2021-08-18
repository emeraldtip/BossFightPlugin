package com.bfg.veo.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;

public class MovementManager {
	
	public static Player targetPlayer;
	public static boolean inAnimation = false;

	public static void initMovement() {
		
		loopCatchUp();
		loopMovement();
		List<Player> players = new ArrayList<>();
		players.addAll(Bukkit.getOnlinePlayers());
		targetPlayer = players.get(new Random().nextInt(players.size()));
		loopFacing();
		
	}
	
	private static void loopCatchUp() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
					
				BossFight.getBoss().teleport(BossFight.getBossHitbox());
				BossFight.floatCloud.update(null, BossFight.getBossHitbox().getLocation().subtract(0, 0.5, 0), 0, null);
				
				if (!inAnimation) {
				
					FunctionManager.runFunction("animations/animation.herobrine.recover/reset", BossFight.getBoss());
					FunctionManager.runFunction("animations/animation.herobrine.throw/reset", BossFight.getBoss());
					FunctionManager.runFunction("animations/animation.herobrine.recover/reset", BossFight.getBoss());
				
				}
				
			}
			
		}, 1L, 1L);
		
	}
	
	private static void loopMovement() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				boolean isThereGround = false, isThereCeiling = false;
				for (int i = 0; i <= 6; i++) {
					
					if (Bukkit.getWorld(Main.world).getBlockAt(BossFight.getBossHitbox().getLocation().clone().subtract(0, i, 0)).getType() != Material.AIR) {
						
						isThereGround = true;
						
					}
					
				}
				for (int i = 0; i <= 6; i++) {
					
					if (Bukkit.getWorld(Main.world).getBlockAt(BossFight.getBossHitbox().getLocation().clone().add(0, i, 0)).getType() != Material.AIR) {
						
						isThereCeiling = true;
						
					}
					
				}
				if (isThereGround && isThereCeiling) {
					
					BossFight.getBossHitbox().setVelocity(new Vector((Math.random() - Math.random()) / 2,
							0,
							(Math.random() - Math.random()) / 2));
					
				} else if (isThereGround && !isThereCeiling) {
					
					BossFight.getBossHitbox().setVelocity(new Vector((Math.random() - Math.random()) / 2,
							Math.random() / 2,
							(Math.random() - Math.random()) / 2));
					
				} else if (!isThereGround && isThereCeiling) {
					
					BossFight.getBossHitbox().setVelocity(new Vector((Math.random() - Math.random()) / 2,
							(0 - Math.random() / 2),
							(Math.random() - Math.random()) / 2));
					
				} else {
					
					BossFight.getBossHitbox().setVelocity(new Vector((Math.random() - Math.random()) / 2,
							(Math.random() - Math.random()) / 2,
							(Math.random() - Math.random()) / 2));
					
				}
				
			}
			
		}, 200L, 200L);
		
	}
	
	private static void loopFacing() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				double dx, // this is so stupid aaaaa
				dz; // aAAAAAAAAAAAAA
				if (BossFight.getBossHitbox().getLocation().getX() < targetPlayer.getLocation().getX()) {
					
					dx = Math.abs(targetPlayer.getLocation().getX()) - Math.abs(BossFight.getBossHitbox().getLocation().getX());
					
				} else {
					
					dx = 0 - (Math.abs(BossFight.getBossHitbox().getLocation().getX()) - Math.abs(targetPlayer.getLocation().getX()));
					
				}
				if (BossFight.getBossHitbox().getLocation().getZ() < targetPlayer.getLocation().getZ()) {
					
					dz = Math.abs(targetPlayer.getLocation().getZ()) - Math.abs(BossFight.getBossHitbox().getLocation().getZ());
					
				} else {
					
					dz = 0 - (Math.abs(BossFight.getBossHitbox().getLocation().getZ()) - Math.abs(targetPlayer.getLocation().getZ()));
					
				} //yes i know this is useless my brain is small so ignore me
				
				float yaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI));
				Location loc = BossFight.getBossHitbox().getLocation().clone();
				loc.setYaw(yaw + 90);
				BossFight.getBossHitbox().teleport(loc);
				BossFight.getBossHitbox().setRotation((int) yaw, 0);
				
			}
			
		}, 0L, 1L);
		
	}
	
	public static void playAnimation(String animation) {
		
		inAnimation = true;
		switch (animation) {
		
		case "throw":
			
			FunctionManager.runFunction("animations/animation.herobrine.throw/start", BossFight.getBoss());
			Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				
				public void run() {
					
					inAnimation = false;
					FunctionManager.runFunction("animations/animation.herobrine.throw/reset", BossFight.getBoss());
					
				}
				
			}, 69L);
			
			break;
			
		case "death":
			
			FunctionManager.runFunction("animations/animation.herobrine.death/start", BossFight.getBoss());
			Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				
				public void run() {
					
					inAnimation = false;
					FunctionManager.runFunction("animations/animation.herobrine.death/reset", BossFight.getBoss());
					
				}
				
			}, 69L);
			
			break;
			
		case "heal":
			
			FunctionManager.runFunction("animations/animation.herobrine.recover/start", BossFight.getBoss());
			BossFight.getBossHitbox().setVelocity(new Vector(0, 5, 0));
			for (int i = -180; i <= 180; i += 10) {
				
				final int yaw = i;
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					
					public void run() {
						
						BossFight.getBossHitbox().setRotation(yaw, 0);
						
					}
					
				}, 1L);
				
			}
			Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				
				public void run() {
							
					inAnimation = false;
					FunctionManager.runFunction("animations/animation.herobrine.recover/reset", BossFight.getBoss());
						
				}
				
			}, 69L);
			
			break;
		
		}
		
	}
	
}
