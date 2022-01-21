package com.terturl.MMO.Util.Items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Util.Items.ItemEnums.CraftRarity;
import com.terturl.MMO.Util.Items.ItemEnums.Rarity;
import com.terturl.MMO.Util.Items.ItemEnums.SlotType;

import lombok.Getter;
import lombok.Setter;

/**
 * A subclass of CustomItem that will hold a lot of information about the base
 * damage of the item and if it is ranged or not and how to handle attacking
 * 
 * @author Sean Rahman
 * @since 0.53.0
 *
 */
public class CustomWeapon extends MMOEquipable {

	@Getter
	@Setter
	private Boolean ranged;

	public CustomWeapon(String name, Material mat, Integer itemDamage, Integer level, Rarity rarity, CraftRarity craftingRarity, SlotType st) {
		super(name, mat, itemDamage, rarity, craftingRarity, level);
		setSlotType(st);
		ranged = false;
	}

	public CustomWeapon(String name, Material mat, Integer itemDamage, Integer level, Rarity rarity,
			CraftRarity craftingRarity, SlotType st, Boolean r) {
		super(name, mat, itemDamage, rarity, craftingRarity, level);
		ranged = r;
		setSlotType(st);
	}

	// NOT USED YET
	protected void particleBeamWithDamage(Player player) {
		Location startLoc = player.getEyeLocation();
		Location particleLoc = startLoc.clone();
		World world = startLoc.getWorld();
		Vector dir = startLoc.getDirection();
		Vector vecOffset = dir.clone().multiply(0.5);

		new BukkitRunnable() {
			int maxBeamLength = 30;
			int beamLength = 0;

			public void run() {
				for (Entity entity : world.getNearbyEntities(particleLoc, 5, 5, 5)) {
					if (entity instanceof LivingEntity) {
						if (entity == player) {
							continue;
						}

						Vector particleMinVector = new Vector(particleLoc.getX() - 0.25, particleLoc.getY() - 0.25,
								particleLoc.getZ() - 0.25);
						Vector particleMaxVector = new Vector(particleLoc.getX() + 0.25, particleLoc.getY() + 0.25,
								particleLoc.getZ() + 0.25);

						if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
							world.spawnParticle(Particle.FLASH, particleLoc, 0);
							world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
							entity.setVelocity(
									entity.getVelocity().add(particleLoc.getDirection().normalize().multiply(1.5)));
							((Damageable) entity).damage(5, player);
							this.cancel();
							return;
						}
					}
				}

				beamLength++;
				if (beamLength >= maxBeamLength) {
					world.spawnParticle(Particle.FLASH, particleLoc, 0);
					this.cancel();
					return;
				}
				particleLoc.add(vecOffset);
				world.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 0);
			}
		}.runTaskTimer(MinecraftMMO.getInstance(), 0, 1L);
	}
	
	// NOT USED YET
	protected void particleBeam(Player player) {
		Location startLoc = player.getEyeLocation();
		Location particleLoc = startLoc.clone();
		World world = startLoc.getWorld();
		Vector dir = startLoc.getDirection();
		Vector vecOffset = dir.clone().multiply(0.5);
		new BukkitRunnable() {
			public void run() {
				particleLoc.add(vecOffset);
				world.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 0);
			}
		}.runTaskTimer(MinecraftMMO.getInstance(), 0, 1L);
	}

}