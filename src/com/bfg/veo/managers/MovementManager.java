package com.bfg.veo.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;

public class MovementManager {
	
	public static Player targetPlayer;
	public static boolean inAnimation = false, bruh = false;
	private static BukkitTask yes, yes2, yes3, healing;
	private static int counter = 0;

	public static void initMovement() {
		
		loopCatchUp();
		loopMovement();
		List<Player> players = new ArrayList<>();
		players.addAll(Bukkit.getOnlinePlayers());
		targetPlayer = players.get(new Random().nextInt(players.size()));
		loopFacing();
		
	}
	
	private static void loopCatchUp() {
		
		yes2 = Bukkit.getScheduler().runTaskTimer(Main.getMain(), new Runnable() {
			
			public void run() {
				if (!BossFight.running)
				{
					yes2.cancel();
				}
				BossFight.getBoss().teleport(BossFight.getBossHitbox());
				BossFight.floatCloud.update(null, BossFight.getBossHitbox().getLocation().subtract(0, 0.5, 0), 0, null);
				
				if (!inAnimation) {
				
					FunctionManager.runFunction("animations/animation.herobrine.throw/reset", BossFight.getBoss());
				
				}
				
			}
			
		}, 1L, 1L);
		
	}
	
	private static void loopMovement() {
		yes3 = Bukkit.getScheduler().runTaskTimer(Main.getMain(), new Runnable()
        {
			public void run()
			{
				
				if(!BossFight.running)
				{
					yes3.cancel();
				}
				if(!inAnimation)
				{
					
				
					boolean isThereGround = false, isThereCeiling = false, tooHigh = false;
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
					if (BossFight.getBossHitbox().getLocation().getY() > Main.getMain().getConfig().getDouble("StartY")+20)
					{
						tooHigh = true;
					}
					else
					{
						tooHigh = false;
					}
					if (BossFight.getBossHitbox().getLocation().getY() > Main.getMain().getConfig().getDouble("StartY")+40)
					{
						BossFight.getBossHitbox().setVelocity(new Vector(0,
								-5, 
								0));
					}
					else if (!isThereGround && tooHigh)
					{
						BossFight.getBossHitbox().setVelocity(new Vector((Math.random() - Math.random()) / 2,
								(0 - Math.random()) / 2,
								(Math.random() - Math.random()) / 2));
					}
					else if (isThereGround && isThereCeiling) {
						
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
			}
			
		}, 200L, 200L);
		
	}
	
	private static void loopFacing() {
		if (BossFight.running)
		{
			yes = Bukkit.getScheduler().runTaskTimer(Main.getMain(), new Runnable() {
				
				public void run() {
					if(!BossFight.running)
					{
						yes.cancel();
					}
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
					loc.setYaw(yaw - 90);
					BossFight.getBossHitbox().teleport(loc);
					BossFight.getBossHitbox().setRotation((int) yaw-90, 0);
					
				}
				
			}, 0L, 1L);
		}
	}
	
	public static void playAnimation(String animation) {
		
		

			switch (animation) {
			
			case "throw":
				inAnimation = true;
				FunctionManager.runFunction("animations/animation.herobrine.throw/start", BossFight.getBoss());
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					
					public void run() {
						
						inAnimation = false;
						FunctionManager.runFunction("animations/animation.herobrine.throw/reset", BossFight.getBoss());
						
					}
					
				}, 69L);
				
				break;
				
			case "death":
				inAnimation = true;
				FunctionManager.runFunction("animations/animation.herobrine.death/start", BossFight.getBoss());
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					
					public void run() {
						
						inAnimation = false;
						FunctionManager.runFunction("animations/animation.herobrine.death/reset", BossFight.getBoss());
						
					}
					
				}, 69L);
				
				break;
				
			case "heal":
				counter = 0;
				inAnimation = true;
				BossFight.getBossHitbox().setVelocity(new Vector(0, 0.1, 0));
				BossFight.getBoss().setVelocity(new Vector(0, 0.1, 0));
				healing = Bukkit.getScheduler().runTaskTimer(Main.getMain(), new Runnable() {
					public void run() {
						FunctionManager.runFunction("animations/animation.herobrine.recover/loop", BossFight.getBoss());
						counter++;
						if (counter >= 33 && !bruh)
						{
							BossFight.getBossHitbox().setVelocity(new Vector(0, 0.5, 0));
							BossFight.getBoss().setVelocity(new Vector(0, 0.5, 0));
							bruh = true;
						}
						if (counter >= 70)
						{
							healing.cancel();
						}
					}
				}, 0, 1L);
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					public void run() {
								
						inAnimation = false;
						FunctionManager.runFunction("animations/animation.herobrine.recover/reset", BossFight.getBoss());
							
					}
					
				}, 60L);
				
				break;
			
			}
			
		}
		
	
	
}
