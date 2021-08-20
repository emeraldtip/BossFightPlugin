package com.bfg.veo.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;
import com.bfg.veo.objs.ZParticle;

import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R3.ParticleType;
import net.minecraft.server.v1_16_R3.WorldBorder;

public class EffectManager {
	
	private static HashMap<String[], String> titles = new HashMap<>();
	private static int[] lengths = {0, 0};
	private static double[][] coordinates;
	private static int loop, arrayLocation = 0;
	
	public static void displayText(Player player, String title, String subtitle) {
		
		titles.put(title.split(""), "");
		titles.put(subtitle.split(""), "");
		loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				if (lengths[0] != title.length()) {
					
					titles.put(title.split(""), titles.get(title.split("")) + title.split("")[lengths[0]]);
					player.sendTitle(titles.get(title.split("")), "", 0, 10, 10);
					
					lengths[0]++;
					
				} else {
					
					if (lengths[1] != subtitle.length()) {
						
						titles.put(subtitle.split(""), titles.get(subtitle.split("")) + subtitle.split("")[lengths[1]]);
						player.sendTitle(titles.get(title.split("")), titles.get(subtitle.split("")), 0, 10, 10);
						
						lengths[1]++;
						
					} else { Bukkit.getScheduler().cancelTask(loop); }
					
				}
				
			}
			
		}, 0L, 2L);
		
	}
	
	public static void mobSpawnEffect(ParticleType particle, Location location) {
		
		new ZParticle(particle, location, 5, new double[]{1.5, 2, 1.5}).playParticle(true, null);;
		
	}
	
	public static void explodeFloor(Location loc) {
		
		
		
	}
	
	public static void circleEffect(Particle particle, double radius, Location location) {
		coordinates = new double[11 + 10 * 2][];
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radiuss = Math.sin(i+radius);
			double y = 0;
			for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
			      double x = Math.cos(a) * radiuss;
			      double z = Math.sin(a) * radiuss;
			      location.add(x, y, z);
			      for(Player p : Bukkit.getWorld("world").getPlayers())
			      {
			    	  p.spawnParticle(particle, location, 0, 0,0,0);
			      }
			      location.subtract(x, y, z);
			}
		}
	}
	
	public static void sphereEffect(Particle particle, double radius) {
		Location location = BossFight.getBoss().getLocation();
		coordinates = new double[11 + 10 * 2][];
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radiuss = Math.sin(i+radius);
			double y = Math.cos(i+radius);
			for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
			      double x = Math.cos(a) * radiuss;
			      double z = Math.sin(a) * radiuss;
			      location.add(x, y, z);
			      for(Player p : Bukkit.getWorld("world").getPlayers())
			      {
			    	  p.spawnParticle(particle, location, 1);
			      }
			      location.subtract(x, y, z);
			      
			   }
			//coordinates[arrayLocation++] = new double[] { x, y, z };
			
	/*		
			circleEffect(new ZParticle(particle.getParticle(),
					new Location(Bukkit.getWorld(Main.world), particle.getLocation().getX(), particle.getLocation().getY() - Math.sin(i) * radius, particle.getLocation().getZ()),
					particle.getCount(),
					particle.getOffset()),
					Math.cos(i) * radius);
	 */
		}
		
	}
	
	// thanks to SANIGOR for this <3
	
		public static void sendWorldBorder(Player player, double size, int warningDistance, long time) {
			
	        WorldBorder worldBorder = new WorldBorder(); 
	        worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();
	        worldBorder.setCenter(player.getWorld().getWorldBorder().getCenter().getBlockX() + 0.5, player.getWorld().getWorldBorder().getCenter().getBlockZ() + 0.5);
	        worldBorder.setSize(size);   
	        worldBorder.setWarningDistance(warningDistance);
	        worldBorder.setWarningTime(0);
	        sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE), "set", player);
	        
	        Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
	        	
	        	public void run() {
	        		
	                worldBorder.setWarningDistance(0);
	                sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE), "set", player);
	        		
	        	}
	        	
	        }, time);
	        
	    }
	
	private static void sendPacket(@SuppressWarnings("rawtypes") Packet packet, String parameter, Player player) {
		
		switch (parameter) {
		
		case "set":
			
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			
			break;
			
		case "all":
			
			for (Player current : Bukkit.getOnlinePlayers()) {
				
				((CraftPlayer) current).getHandle().playerConnection.sendPacket(packet);
				
			}
			
			break;
		
		}
		
	}
	
}
