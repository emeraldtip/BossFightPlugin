package com.bfg.veo.objs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.bfg.veo.Main;

public class FlyAwayParticle {

	final double dD = 0.5;
	
	private boolean running = true;
	private ZParticle particle;
	private Location target;
	private FlyAwayParticle THIS;
	
	public FlyAwayParticle(ZParticle particle, Object target) {
		
		THIS = this;
		this.particle = particle;
		if (target instanceof Location) {
			
			this.target = (Location) target;
			
		} else {
			
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
				
				public void run() {
					
					if (running) { THIS.target = ((Entity) target).getLocation(); }
					
				}
				
			}, 0L, 1L);
			
		}
		
		particle.playParticle(true, null);
		iterate();
		
	}
	
	private void iterate() {
		
		if (running) {
			
			float yaw = (float) ((float) Math.atan2(target.getX() - particle.getLocation().getX(), target.getZ() - particle.getLocation().getZ()) * (180 / Math.PI));
			float pitch = (float) ((float) Math.atan2(target.getY() - particle.getLocation().getY(), target.getX() - particle.getLocation().getX()) * (180 / Math.PI));
			double
			x = particle.getLocation().getX() + Math.sin(yaw) * dD,
			y = particle.getLocation().getY() + Math.sin(pitch) * dD,
			z = particle.getLocation().getZ() + Math.cos(yaw) * dD;
			Location location = new Location(Bukkit.getWorld(Main.world), particle.getLocation().getX() + x, particle.getLocation().getY() + y, particle.getLocation().getZ() + z);
			particle.setLocation(location);
			particle.playParticle(true, null);
			
			if (particle.getLocation() == target) { running = false; }
			
		}
		
	}
	
	public ZParticle getParticle() { return particle; }
	public Location getTarget() { return target; }
	public boolean getState() { return running; }
	
	public void setParticle(ZParticle particle) { this.particle = particle; }
	public void setTarget(Location target) { this.target = target; }
	public void kill() { running = false; }
	
}
