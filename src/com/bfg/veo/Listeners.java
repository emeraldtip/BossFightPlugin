package com.bfg.veo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.bfg.veo.managers.ConfigManager;
import com.bfg.veo.managers.EnvironmentManager;
import com.bfg.veo.managers.MovementManager;
import com.bfg.veo.objs.RandomFlyAwayParticle;
import com.bfg.veo.objs.ZParticle;

import net.minecraft.server.v1_16_R3.Particles;

public class Listeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		BossFight.bossBar.addPlayer(event.getPlayer());
		
	}
	
	@SuppressWarnings({"deprecation"})
	@EventHandler
	public void onSelect(PlayerInteractEvent event) {
		
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			
			switch (Main.state) {
			
			case 0:
				break;
				
			case 1:
				
				if (event.getPlayer().getInventory().getItemInHand().getType().equals(Material.STICK) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Select Floor")) {
				
					ConfigManager.editLocs("FloorX1", "" + event.getClickedBlock().getLocation().getX());
					ConfigManager.editLocs("FloorY1", "" + event.getClickedBlock().getLocation().getY());
					ConfigManager.editLocs("FloorZ1", "" + event.getClickedBlock().getLocation().getZ());
					event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "POS1 selected. (" + event.getClickedBlock().getLocation().getX() + ", " + event.getClickedBlock().getLocation().getY() + ", " + event.getClickedBlock().getLocation().getZ() + ")");
					event.setCancelled(true);
				
				}
				
				break;
				
			case 2:
				break;
			
			}
			
		}
		
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			if (event.getHand().equals(EquipmentSlot.OFF_HAND)) { return; }
			switch (Main.state) {
			
			case 0:
				break;
				
			case 1:
				
				if (event.getPlayer().getInventory().getItemInHand().getType().equals(Material.STICK) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Select Floor")) {
				
					ConfigManager.editLocs("FloorX2", "" + event.getClickedBlock().getLocation().getX());
					ConfigManager.editLocs("FloorY2", "" + event.getClickedBlock().getLocation().getY());
					ConfigManager.editLocs("FloorZ2", "" + event.getClickedBlock().getLocation().getZ());
					event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "POS2 selected. (" + event.getClickedBlock().getLocation().getX() + ", " + event.getClickedBlock().getLocation().getY() + ", " + event.getClickedBlock().getLocation().getZ() + ")");
					event.setCancelled(true);
				
				}
				
				break;
				
			case 2:
				break;
			
			}
			
		}
		
	}
	
	@EventHandler
	public void onDamageByProjectile(ProjectileHitEvent event) {
		
		if (BossFight.running) {
		
			if (BossFight.fireballs.contains(event.getEntity())) {
				
				Bukkit.getWorld(Main.world).createExplosion(event.getEntity().getLocation(), 5, true);
				event.getEntity().remove();
				BossFight.fireballs.remove(BossFight.fireballs.indexOf(event.getEntity()));
				BossFight.fireballTime.remove(event.getEntity());
				
			} else if (event.getHitEntity().equals(BossFight.getBossHitbox()) && event.getEntity().getType() == EntityType.ARROW) { event.getEntity().remove(); }
		
		}
		
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		
		if (BossFight.running) {
		
			if (event.getEntity() instanceof Player) {
				
				Player player = (Player) event.getEntity();
				if (Bukkit.getOnlinePlayers().contains(player)) {
					
					BossFight.bossHealth += event.getDamage() / 2;
					new RandomFlyAwayParticle(new ZParticle(Particles.HEART, player.getLocation().add(0, 1.5, 0), 1, new double[]{0.1, 0.1, 0.1}), 5);
					double p = BossFight.bossHealth / 2500 * 100;
					BossFight.bossBar.setProgress(p / 100);
					
				}
				
			} else if (event.getEntity().equals(BossFight.getBossHitbox())) {
				
				BossFight.bossHealth -= event.getDamage() * 2;
				event.setCancelled(true);
				if (event.getDamager() instanceof Player) {
					
					MovementManager.targetPlayer = (Player) event.getDamager();
					Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
						
						public void run() {
							
							List<Player> players = new ArrayList<>();
							players.addAll(Bukkit.getOnlinePlayers());
							MovementManager.targetPlayer = players.get(new Random().nextInt(players.size()));
							
						}
						
					}, 600L);
				
				}
				double p = BossFight.bossHealth / 2500 * 100;
				BossFight.bossBar.setProgress(p / 100);
				
			}
		
		}
		
	}
	
	@EventHandler
	public void onBreakByExplosion(BlockExplodeEvent event) { if (BossFight.running) { return; } EnvironmentManager.brokenBlocks.put(event.getBlock(), event.getBlock().getType()); }
	@EventHandler
	public void onBreakByPlayer(BlockBreakEvent event) { if (BossFight.running) { return; } EnvironmentManager.brokenBlocks.put(event.getBlock(), event.getBlock().getType()); }
	
	// i dont care about blocks being placed thats fine
	
}
