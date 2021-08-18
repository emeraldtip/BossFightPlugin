package com.bfg.veo.objs;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.bfg.veo.Main;

public class RandomFlyAwayParticle {

	private boolean running = true;
	private Location location;
	private ZParticle particle;
	
	public RandomFlyAwayParticle(ZParticle particle, double delay) {
		
		this.location = particle.getLocation();
		this.particle = particle;
		particle.playParticle(true, null);
		iteration();
		Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
			
			public void run() {
				
				running = false;
				
			}
			
		}, (long) delay * 20L);
		
		
	}
	
	private void iteration() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				if (running) {
					
					location = new Location(Bukkit.getWorld(Main.world),
							location.getX() + (new Random().nextInt(1 + 1) - 1 ) + Math.random(),
							location.getY() + 0.2,
							location.getZ() + (new Random().nextInt(1 + 1) - 1 ) + Math.random());
					particle.setLocation(location);
					particle.playParticle(true, null);
					
				}
				
			}
			
		}, 1L, 1L);
		
	}
	
	public void kill() { running = false; }
	
}
