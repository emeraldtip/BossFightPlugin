package com.bfg.veo.objs;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.bfg.veo.Main;

import net.minecraft.server.v1_16_R3.ParticleType;

public class LoopingParticle {

	private boolean running = true;
	private ZParticle particle;
	
	public LoopingParticle(ZParticle particle) {
		
		this.particle = particle;
		iterate();
		
	}
	
	private void iterate() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				if (running) {
				
					particle.playParticle(true, null);
				
				}
				
			}
			
		}, 1L, 1L);
		
	}
	
	public void update(ParticleType particle, Location location, int count, double[] offset) {
		
		if (particle != null) { this.particle.setParticle(particle); }
		if (location != null) { this.particle.setLocation(location); }
		if (count != 0) { this.particle.setCount(count); }
		if (offset != null) { this.particle.setOffset(offset); }
		
	}
	
	public void kill() { running = false; }
	
}
