package com.bfg.veo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.bfg.veo.managers.EffectManager;
import com.bfg.veo.managers.EnvironmentManager;
import com.bfg.veo.managers.FunctionManager;
import com.bfg.veo.managers.MovementManager;
import com.bfg.veo.managers.Sequences;
import com.bfg.veo.objs.DamagerParticle;
import com.bfg.veo.objs.FlyAwayParticle;
import com.bfg.veo.objs.LoopingParticle;
import com.bfg.veo.objs.ZParticle;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.Particles;

public class BossFight {
	
	private static int time = 0, healthLoop = 0, effectLoop = 0, healthLoops = 0;
	public static double bossHealth = 2500, defaultHealth = 2500, effectRadius = 0.5, dY = 0, prevY = 0; // i should stop doing this...
	public static boolean running = false;
	public static List<Fireball> fireballs = new ArrayList<>();
	private static List<FallingBlock> thrownBlocks = new ArrayList<>();
	private static ArmorStand boss;
	private static IronGolem bossHitbox;
	public static List<Entity> exceptions = new ArrayList<>(), despawnables = new ArrayList<>();
	public static List<Player> targets = new ArrayList<>();
	public static HashMap<Fireball, Integer> fireballTime = new HashMap<>();
	public static BossBar bossBar;
	public static LoopingParticle floatCloud;
	
	public static void spawn(double x, double y, double z) {	
		
		bossBar = Bukkit.createBossBar(ChatColor.DARK_RED + "Herobrine", BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
		
		boss = (ArmorStand) Bukkit.getWorld(Main.world).spawnEntity(new Location(Bukkit.getWorld(Main.world), x, y - 2.5, z), EntityType.ARMOR_STAND);
		bossHitbox = (IronGolem) Bukkit.getWorld(Main.world).spawnEntity(new Location(Bukkit.getWorld(Main.world), x, y - 2.5, z), EntityType.IRON_GOLEM);
		
		floatCloud = new LoopingParticle(new ZParticle(Particles.CLOUD, new Location(Bukkit.getWorld(Main.world), x, y - 2.5, z), 30, new double[]{1.5, 1.5, 1.5}));
		
		boss.addScoreboardTag("aj.herobrine.root_entity");
		boss.addScoreboardTag("aj.herobrine");
		boss.addScoreboardTag("new");
		boss.setInvisible(true);
		boss.setInvulnerable(true);
		bossHitbox.setAI(true);
		bossHitbox.setInvulnerable(true);
		bossHitbox.setGravity(false);
		bossHitbox.setSilent(true);
		bossHitbox.setInvisible(true);
		bossHitbox.addScoreboardTag("aj.herobrine.unspawnable");
		
		FunctionManager.runFunction("summon_model", boss);
		MovementManager.initMovement();
		MovementManager.inAnimation = true;
		
		effectRadius = 0;
		effectLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				floatCloud.update(Particles.CLOUD, new Location(Bukkit.getWorld(Main.world), 0, -10, 0), 10, new double[]{0.7, 0.3, 0.7});
				
				if (effectRadius <= 5) {
					
					effectRadius += 0.05;
					double angle = 360 / (5 / 0.05) * (effectRadius + 5);
					
					ZParticle spiral = new ZParticle(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), x, y, z), 1, new double[]{0.1, 0.1, 0.1});
					double sin = Math.sin(angle) * 2,
							cos = Math.cos(angle) * 2;
					spiral.setLocation(new Location(Bukkit.getWorld(Main.world),
							x + sin,
							(y - 2.5) + effectRadius,
							z + cos));
					spiral.playParticle(true, null);
					
					Location loc = bossHitbox.getLocation();
					loc.add(0, 0.05, 0);
					loc.setYaw((float) (angle - 180));
					bossHitbox.teleport(loc);
					BossFight.getBossHitbox().setRotation((int) (angle - 180), 0);
					
					MovementManager.playAnimation("heal");
					
				} else {
					
					Bukkit.getWorld(Main.world).createExplosion(new Location(Bukkit.getWorld(Main.world), x, y ,z), 6, false);
					Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
					Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
					Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
					Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
					floatCloud.update(Particles.CLOUD, boss.getLocation().subtract(0, 0.5, 0), 10, new double[]{0.7, 0.3, 0.7});
					bossHitbox.teleport(new Location(Bukkit.getWorld(Main.world), x, y, z));
					for (Entity current : Bukkit.getWorld(Main.world).getEntities()) {
						
						if (current.getType().equals(EntityType.ARMOR_STAND)) { exceptions.add(current); }
						
					}
					exceptions.add(bossHitbox);
					targets.addAll(Bukkit.getOnlinePlayers());
					for (Player current : Bukkit.getOnlinePlayers()) {
						
						bossBar.addPlayer(current);
						
					}
					
					explodeFireballs();
					BossFight.running = true;
					MovementManager.inAnimation = false;
					
					Bukkit.getScheduler().cancelTask(effectLoop);
					effectRadius = 0.5;
					
				}
				
			}
			
		}, 120L, 1L);
		
	}
	
	public static void pulsate() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				for (Fireball fireball : fireballs) {
					
					fireballTime.put(fireball, fireballTime.get(fireball) + 1);
					
				}
				time++;
				if (time == 15) {
						
					switch (new Random().nextInt(4)) {
						
					case 0:
						a1();
						break;
						
					case 1:
						a2();
						break;
						
					case 2:
						a3();
						break;
						
					case 3:
						a4();
						break;
						
					case 4:
						a5();
						break;
					
					}
					
				} else if (time == 30) {
					
					if (bossHealth > ((defaultHealth * 80) / 100)) {
						
						switch (new Random().nextInt(4)) {
						
						case 0:
							a1();
							break;
						
						case 1:
							a2();
							break;
						
						case 2:
							a3();
							break;
						
						case 3:
							a4();
							break;
						
						case 4:
							a5();
							break;
					
						}
						
					} else if (bossHealth < ((defaultHealth * 30) / 100)) {
						
						heal(false);
						
						Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
						Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
						Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
						Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
						
						Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
							
							public void run() {
							
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
							
							}
							
						}, 15L);
						
						Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
							
							public void run() {
							
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10));
								Bukkit.getWorld(Main.world).strikeLightning(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10));
							
							}
							
						}, 30L);
						// no i dont wanna do these in for loops ty :)
						
						effectRadius = 5;
						effectLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
							
							public void run() {
								
								if (effectRadius != 0.5) {
									
									EffectManager.circleEffect(new ZParticle(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10), 1, new double[]{0.05, 0.05, 0.05}), effectRadius);
									EffectManager.circleEffect(new ZParticle(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10), 1, new double[]{0.05, 0.05, 0.05}), effectRadius);
									EffectManager.circleEffect(new ZParticle(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + 10), 1, new double[]{0.05, 0.05, 0.05}), effectRadius);
									EffectManager.circleEffect(new ZParticle(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() - 10, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() - 10), 1, new double[]{0.05, 0.05, 0.05}), effectRadius);
									effectRadius -= 0.5;
									
								} else {
									
									int a = 0;
									
									Bukkit.getScheduler().cancelTask(effectLoop);
									for (double x = -10; x <= 10; x += 20) {
										
										for (double z = -10; z <= 10; z += 20) {
											
											EffectManager.mobSpawnEffect(Particles.FLAME, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + x, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + z));
											Entity despawnable = Bukkit.getWorld(Main.world).spawnEntity(new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + x, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + z), EntityType.BLAZE);
											despawnables.add(despawnable);
											List<Player> selectedTargets = new ArrayList<>();
											for (Player current : targets) {
												
												if (Math.round(Math.random()) == 1) { selectedTargets.add(current); }
												else {
													
													// shift mechanism
													for (int i = 0; i < Math.round(Math.random() * 10); i++) {
														
														Player removable = targets.get(0);
														targets.remove(targets.indexOf(removable));
														targets.add(removable);
														
													}
													
												}
												
											}
											if (selectedTargets.isEmpty()) { selectedTargets.add(targets.get(0)); }
											new DamagerParticle(new ZParticle(Particles.ANGRY_VILLAGER, new Location(Bukkit.getWorld(Main.world), bossHitbox.getLocation().getX() + x, bossHitbox.getLocation().getY(), bossHitbox.getLocation().getZ() + z), 1, new double[]{0.1, 0.1, 0.1}),
													selectedTargets.get(a).getLocation());
											
											Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
												
												public void run() {
													
													Iterator<Entity> i = despawnables.iterator();
													while (i.hasNext()) {
														
														Entity entity = i.next();
														entity.remove();
														i.remove();
														EffectManager.mobSpawnEffect(Particles.SMOKE, entity.getLocation());
														
													}
													
												}
												
											}, 600L);
											a++;
											
										}
										
									}
									
								}
								
							}
							
						}, 0L, 5L);
						
					} else { heal(true); }
					time = 0;
					
				}
				
			}
				
		}, 0L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				if (bossHealth <= 5 && bossHealth > -1) {
					
					death();
					bossHealth = -1;
					
				}
				double firstThird = defaultHealth / 3,
				secondThird = 2 / 3 * defaultHealth;
				if (bossHealth > secondThird) {
					
					bossBar.setColor(BarColor.GREEN);
					
				} else if (bossHealth > firstThird) {
					
					bossBar.setColor(BarColor.YELLOW);
					
				} else if (bossHealth < firstThird) {
					
					bossBar.setColor(BarColor.RED);
					
				}
				
			}
			
		}, 0L, 1L);
		
	}
	
	public static void death() {
		
		for (Player current : Bukkit.getOnlinePlayers()) {
			
			current.playSound(bossHitbox.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 100, 1);
			
		}
		Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
			
			public void run() {
				
				effectRadius = 0; // im not using this as a radius in this case buuuuuuut it does its job c:
				effectLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
						
					public void run() {
							
						Bukkit.getWorld(Main.world).createExplosion(new Location(Bukkit.getWorld(Main.world),
								bossHitbox.getLocation().getX() + Math.sin(effectRadius) * 10,
								bossHitbox.getLocation().getY(),
								bossHitbox.getLocation().getZ() + Math.cos(effectRadius) * 10), 1, false);
						
						if (effectRadius == 360) {
							
							Bukkit.getScheduler().cancelTask(effectLoop);
							effectRadius = 0.5; // this is useless but idgaf
							MovementManager.playAnimation("death");
							bossHitbox.setGravity(true);
							floatCloud.kill();
							
							ArmorStand sword = (ArmorStand) Bukkit.getWorld(Main.world).spawnEntity(bossHitbox.getLocation().add(0, 10, 0), EntityType.ARMOR_STAND);
							sword.setInvisible(true);
							sword.setInvulnerable(true);
							sword.setArms(true);
							sword.setRightArmPose(new EulerAngle(Math.toRadians(90), Math.toRadians(270), Math.toRadians(0)));
							sword.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
							while (bossHitbox.isOnGround()) {
								
								sword.teleport(bossHitbox.getLocation().subtract(0, 1, 0)); //check for offset when im home :)
								for (Entity player : bossHitbox.getNearbyEntities(20, 10, 20)) {
									
									if (player instanceof Player) {
										
										((Player) player).playSound(bossHitbox.getLocation(), Sound.BLOCK_ANVIL_LAND, 20, 1);
										
									}
									
								}
								break;
								
							}
							
							Sequences.end();
							
						}
						effectRadius++;
							
					}
						
				}, 0L, 2L);
				
			}
			
		}, 100L);
		
	}
	
	public static void heal(boolean effect) {
		
		for (Player current: Bukkit.getOnlinePlayers()) {
			
			current.playSound(bossHitbox.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 50, 1);
			
		}
		Location previousLocation = bossHitbox.getLocation();
		MovementManager.playAnimation("heal");

		Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
			
			public void run() {
				
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					
					public void run() {
						
						bossHitbox.teleport(new Location(Bukkit.getWorld(Main.world), 0, -10, 0));
						healthLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
							
							public void run() {
								
								if (healthLoops == 10 && effectRadius == 15.0) {
									
									bossHitbox.teleport(previousLocation);
									Bukkit.getScheduler().cancelTask(healthLoop);
									if (effect) {
								
										effectRadius = 0.5;
										Bukkit.getScheduler().cancelTask(effectLoop);
									
									}
								
								}
								
								bossHealth += (defaultHealth / 100);
								healthLoops++;
								
							}
							
						}, 0L, 20L);
						
						if (effect) {
						
							effectLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
								
								@SuppressWarnings("deprecation")
								public void run() {
									
									if (effectRadius != 15.0) {
										
										EffectManager.sphereEffect(new ZParticle(Particles.FLAME, previousLocation, 1, new double[]{0.05, 0.05, 0.05}), effectRadius);
										for (Entity entity : bossHitbox.getNearbyEntities(effectRadius, effectRadius, effectRadius)) {
												
											((LivingEntity) entity).damage(((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth() < 0.25
													? 15 - Math.sqrt(Math.sqrt(Math.pow(bossHitbox.getLocation().getX() - entity.getLocation().getX(), 2) + Math.pow(bossHitbox.getLocation().getZ() - entity.getLocation().getZ(), 2)) + Math.pow(bossHitbox.getLocation().getY() - entity.getLocation().getY(), 2)) + 1
													: (15 - Math.sqrt(Math.sqrt(Math.pow(bossHitbox.getLocation().getX() - entity.getLocation().getX(), 2) + Math.pow(bossHitbox.getLocation().getZ() - entity.getLocation().getZ(), 2)) + Math.pow(bossHitbox.getLocation().getY() - entity.getLocation().getY(), 2))) * 2
													);
											
											// this causes performance issues
											
										}
										effectRadius += 0.5;
									
									}
									
								}
								
							}, 0L, 5L);
							
						}
					
					}
					
				}, 40L);
				
			}
			
		}, 69L);
		
	}
	
	//attakcs
	
	public static void a1() {
		
		List<Player> selectedTargets = new ArrayList<>();
		for (Player current : targets) {
			
			if (Math.round(Math.random()) == 1) { selectedTargets.add(current); }
			else {
				
				// shift mechanism
				for (int i = 0; i < Math.round(Math.random() * 10); i++) {
					
					Player removable = targets.get(0);
					targets.remove(targets.indexOf(removable));
					targets.add(removable);
					
				}
				
			}
			
		}
		if (selectedTargets.isEmpty()) { selectedTargets.add(targets.get(0)); }
		for (Player target : selectedTargets) {
			
			Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				
				public void run() {
					
					MovementManager.playAnimation("throw");
					
					Fireball fireball = bossHitbox.launchProjectile(Fireball.class);
					Vector velocity = target.getLocation().toVector().subtract(bossHitbox.getLocation().toVector()).normalize();
					fireball.setVelocity(velocity);
					Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
						
						public void run() {
							
							fireballs.add(fireball);
							fireballTime.put(fireball, 0);
							
						}
						
					}, 30L);
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						player.playSound(bossHitbox.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 50, 1);
						
					}
					
				}
				
			}, 60L);
			
		}
		
		//fix fireball offset

	}

	@SuppressWarnings("deprecation")
	public static void a2() {

		List<EntityType> mobs = new ArrayList<>(Arrays.asList(EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON));
		List<Player> selectedTargets = new ArrayList<>();
		for (Player current : targets) {
			
			if (Math.round(Math.random()) == 1) { selectedTargets.add(current); }
			else {
				
				// shift mechanism
				for (int i = 0; i < Math.round(Math.random() * 10); i++) {
					
					Player removable = targets.get(0);
					targets.remove(targets.indexOf(removable));
					targets.add(removable);
					
				}
				
			}
			
		}
		if (selectedTargets.isEmpty()) { selectedTargets.add(targets.get(0)); }
		for (Player target : selectedTargets) {
			
			EffectManager.displayText(target, "Watch out!", "");
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 255, true), true);
			Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
				
				public void run() {
					
					Bukkit.getWorld(Main.world).spawnEntity(target.getLocation().add(target.getLocation().getDirection().multiply(-2)), mobs.get(new Random().nextInt(mobs.size())));
					EffectManager.sendWorldBorder(target, Integer.MAX_VALUE, Integer.MAX_VALUE, 60);
					target.playSound(target.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 5, 1);
					
				}
				
			}, ("Watch out!".length() + "".length()) * 2L);
			
		}
	
	}

	public static void a3() {
		
		Location location = new Location(Bukkit.getWorld(Main.world), boss.getLocation().getX() + Math.random() * Math.round(Math.random()) == 1 ? 10 : -10,
				boss.getLocation().getY() + Math.random() * Math.round(Math.random()) == 1 ? 10 : -5,
				boss.getLocation().getZ() + Math.random() * Math.round(Math.random()) == 1 ? 10 : -10);
		EffectManager.mobSpawnEffect(Particles.FLAME, location);
		FlyAwayParticle fpar = new FlyAwayParticle(new ZParticle(Particles.FLAME, location, 1, new double[]{0.1, 0.1, 0.1}), bossHitbox.getLocation().add(0, 10, 0));
		while (!fpar.getState()) {
			
			Firework firework = (Firework) Bukkit.getWorld(Main.world).spawnEntity(bossHitbox.getLocation().add(0, 10, 0), EntityType.FIREWORK);
			FireworkMeta fwm = firework.getFireworkMeta();
			FireworkEffect fwe;
			List<EntityType> mobs = new ArrayList<>(Arrays.asList(EntityType.PHANTOM, EntityType.GHAST, EntityType.BLAZE));
			EntityType randomEntity = mobs.get(new Random().nextInt(mobs.size()));
			Bukkit.getWorld(Main.world).spawnEntity(location, randomEntity);
			switch (randomEntity) {
			
			case BLAZE:
				
				fwe = FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).withFade(Color.RED).with(Type.STAR).trail(true).build();
				fwm.addEffect(fwe);
				fwm.setPower(3);
				firework.setFireworkMeta(fwm);
				EffectManager.mobSpawnEffect(Particles.DRIPPING_HONEY, location);
				for (Player current : Bukkit.getOnlinePlayers()) {
					
					new DamagerParticle(new ZParticle(Particles.DRIPPING_HONEY, bossHitbox.getLocation().add(0, 10, 0), 1, new double[]{0.1, 0.1, 0.1}), current);
					
				}
				
				break;
				
			case PHANTOM:
				
				fwe = FireworkEffect.builder().flicker(false).withColor(Color.BLUE).withFade(Color.BLUE).with(Type.BALL_LARGE).trail(true).build();
				fwm.addEffect(fwe);
				fwm.setPower(3);
				firework.setFireworkMeta(fwm);
				EffectManager.mobSpawnEffect(Particles.DRIPPING_WATER, location);
				for (Player current : Bukkit.getOnlinePlayers()) {
					
					new DamagerParticle(new ZParticle(Particles.BUBBLE, bossHitbox.getLocation().add(0, 10, 0), 1, new double[]{0.1, 0.1, 0.1}), current);
					
				}
				
				
				break;
				
			default:
				
				fwe = FireworkEffect.builder().flicker(false).withColor(Color.WHITE).withFade(Color.RED).with(Type.CREEPER).trail(true).build();
				fwm.addEffect(fwe);
				fwm.setPower(3);
				firework.setFireworkMeta(fwm);
				for (Player current : Bukkit.getOnlinePlayers()) {
					
					new DamagerParticle(new ZParticle(Particles.WHITE_ASH, bossHitbox.getLocation().add(0, 10, 0), 1, new double[]{0.1, 0.1, 0.1}), current);
					
				}
				
				break;
			
			}
			
			break;
			
		}
	
	}

	public static void a4() {
	
		List<Block> surfaceBlocks = new ArrayList<>();
		boolean isThereBlock = false;
		for (int x = -5; x <= 5; x++) {
			
			for (int z = -5; z <= 5; z++) {
				
				for (int y = 0; y <= 10; y++) {
					
					if (Bukkit.getWorld(Main.world).getBlockAt(new Location(Bukkit.getWorld(Main.world),
							bossHitbox.getLocation().getX() + x,
							bossHitbox.getLocation().getY() - y,
							bossHitbox.getLocation().getZ() + z)).getType() != Material.AIR) {
						
						surfaceBlocks.add(Bukkit.getWorld(Main.world).getBlockAt(new Location(Bukkit.getWorld(Main.world),
							bossHitbox.getLocation().getX() + x,
							bossHitbox.getLocation().getY() - y,
							bossHitbox.getLocation().getZ() + z)));
						EnvironmentManager.brokenBlocks.put(Bukkit.getWorld(Main.world).getBlockAt(new Location(Bukkit.getWorld(Main.world),
							bossHitbox.getLocation().getX() + x,
							bossHitbox.getLocation().getY() - y,
							bossHitbox.getLocation().getZ() + z)), Bukkit.getWorld(Main.world).getBlockAt(new Location(Bukkit.getWorld(Main.world),
											bossHitbox.getLocation().getX() + x,
											bossHitbox.getLocation().getY() - y,
											bossHitbox.getLocation().getZ() + z)).getType());
						isThereBlock = true;
						break;
						
					}
					if (x == 5 && y == 10 && z == 5 && surfaceBlocks.isEmpty()) {
						
						isThereBlock = false;
						
					}
					
				}
				
			}
			
		}
		if (isThereBlock) {
			
			List<Player> selectedTargets = new ArrayList<>();
			for (Player current : targets) {
				
				if (Math.round(Math.random()) == 1) { selectedTargets.add(current); }
				else {
					
					// shift mechanism
					for (int i = 0; i < Math.round(Math.random() * 10); i++) {
						
						Player removable = targets.get(0);
						targets.remove(targets.indexOf(removable));
						targets.add(removable);
						
					}
					
				}
				
			}
			if (selectedTargets.isEmpty()) { selectedTargets.add(targets.get(0)); } 
			for (Player target : selectedTargets) {
				
				Block block = surfaceBlocks.get(new Random().nextInt(surfaceBlocks.size()));
				FallingBlock fBlock = (FallingBlock) Bukkit.getWorld(Main.world).spawnFallingBlock(block.getLocation().add(0, 1.25, 0), block.getType().createBlockData());
				double magY = (bossHitbox.getLocation().getY() - block.getY()) / 10;
				fBlock.setVelocity(new Vector(0, magY, 0));
				surfaceBlocks.remove(surfaceBlocks.indexOf(block));
				thrownBlocks.add(fBlock);
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
					
					public void run() {
						
						dY = fBlock.getLocation().getY() - prevY;
						if (dY < 0.2) {
							
							MovementManager.playAnimation("throw");
							Vector velocity = target.getLocation().toVector().subtract(bossHitbox.getLocation().toVector()).normalize().multiply(10);
							fBlock.setVelocity(velocity);
							
						}
						prevY = fBlock.getLocation().getY();
						
					}
					
				}, 0L, 1L);
						
			}				
			
		} else {
			
			switch (new Random().nextInt(3)) {
			
			case 0:
				a1();
				break;
			
			case 1:
				a2();
				break;
			
			case 2:
				a3();
				break;
			
			case 3:
				a5();
				break;
		
			}
			
		}
	
	}

	public static void a5() {
		
		List<Player> selectedTargets = new ArrayList<>();
		for (Player current : targets) {
			
			if (Math.round(Math.random()) == 1) { selectedTargets.add(current); }
			else {
				
				// shift mechanism
				for (int i = 0; i < Math.round(Math.random() * 10); i++) {
					
					Player removable = targets.get(0);
					targets.remove(targets.indexOf(removable));
					targets.add(removable);
					
				}
				
			}
			
		}
		if (selectedTargets.isEmpty()) { selectedTargets.add(targets.get(0)); }
		for (Player target : selectedTargets) {
			
			Bukkit.getWorld(Main.world).strikeLightning(target.getLocation());
			for (double r = 0.1; r <= 2.0; r += 0.1) {
				
				final double radius = r;
				Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
					
					public void run() {
						
						EffectManager.circleEffect(new ZParticle(Particles.SMOKE, target.getLocation().add(0, 0.2, 0), 1, new double[]{0.1, 0.1, 0.1}), radius);
						
					}
					
				}, 1L);
				
				
			}
			
		}
	
	}
	
	public static IronGolem getBossHitbox() { return bossHitbox; }
	public static ArmorStand getBoss() { return boss; }
	
	private static void explodeFireballs() { // and check for thrownBlocks collisions
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), new Runnable() {
			
			public void run() {
				
				Iterator<Fireball> iterator1 = fireballs.iterator();
				while (iterator1.hasNext()) {
					
					Fireball fireball = iterator1.next();
					if (!fireball.getNearbyEntities(4, 4, 4).isEmpty() && !exceptions.containsAll(fireball.getNearbyEntities(4, 4, 4))) {
						
						for (Entity e : fireball.getNearbyEntities(4, 4, 4)) {
							
							if (e.getType().equals(EntityType.FIREBALL)) { break; }
							
						}
						
						Bukkit.getWorld(Main.world).createExplosion(fireball.getLocation(), 5, true);
						fireball.remove();
						iterator1.remove();
						fireballTime.remove(fireball);
						
					}
					
				}
				Iterator<Map.Entry<Fireball, Integer>> iterator2 = fireballTime.entrySet().iterator();
				while (iterator2.hasNext()) {
					
					Map.Entry<Fireball, Integer> fireball = (Map.Entry<Fireball, Integer>) iterator2.next();
					if (fireballTime.get(fireball.getKey()).equals(20)) {
						
						Bukkit.getWorld(Main.world).createExplosion(fireball.getKey().getLocation(), 5, true);
						fireball.getKey().remove();
						fireballs.remove(fireballs.indexOf(fireball.getKey()));
						iterator2.remove();
						
					}
					
				}
				Iterator<FallingBlock> iterator3 = thrownBlocks.iterator();
				while (iterator3.hasNext()) {
					
					FallingBlock fBlock = iterator3.next();
					if (fBlock.isOnGround()) {
						
						Bukkit.getWorld(Main.world).createExplosion(fBlock.getLocation(), 5, false);
						fBlock.remove();
						iterator3.remove();
						
					}
					
				}
				
			}
			
		}, 1L, 1L);
		
	}
	
}
