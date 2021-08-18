package com.bfg.veo.objs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.bfg.veo.BossFight;
import com.bfg.veo.Main;

public class DamagerParticle {
	
	final double dD = 0.5; // delta distance or smth idk
	
	private boolean running = true;
	private Location target;
	private ZParticle particle;
	private float yaw, pitch;
	private DamagerParticle THIS;
	
	public DamagerParticle(ZParticle particle, Object target) {
		
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
		
		ArmorStand trigger = (ArmorStand) Bukkit.getWorld(Main.world).spawnEntity(particle.getLocation(), EntityType.ARMOR_STAND);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				if (running) {
					
					yaw = (float) ((float) Math.toDegrees(Math.atan2(target.getX() - particle.getLocation().getX(), target.getZ() - particle.getLocation().getZ())));
					pitch = (float) ((float) Math.toDegrees(Math.atan2(target.getY() - particle.getLocation().getY(), target.getX() - particle.getLocation().getX())));
					double
					x = particle.getLocation().getX() + Math.sin(yaw) * dD,
					y = particle.getLocation().getY() + Math.sin(pitch) * dD,
					z = particle.getLocation().getZ() + Math.cos(yaw) * dD;
					Location location = new Location(Bukkit.getWorld(Main.world), particle.getLocation().getX() + x, particle.getLocation().getY() + y, particle.getLocation().getZ() + z);
					particle.setLocation(location);
					particle.playParticle(true, null);
					
					trigger.teleport(particle.getLocation());
					if (!trigger.getNearbyEntities(3, 3, 3).isEmpty() && !BossFight.exceptions.containsAll(trigger.getNearbyEntities(3, 3, 3))) {
						
						Bukkit.getWorld(Main.world).createExplosion(particle.getLocation(), 3, false);
						running = false;
						
					}
					
				}
				
			}
			
		}, 0L, 1L);
		
	}
	
	public ZParticle getParticle() { return particle; }
	public Location getTarget() { return target; }
	public void kill() { running = false; }
	
}
