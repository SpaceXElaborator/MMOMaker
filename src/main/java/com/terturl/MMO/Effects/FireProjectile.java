package com.terturl.MMO.Effects;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple effect that will fire a projectile from Minecrafts basic projectiles
 * https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Projectile.html
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class FireProjectile extends Effect {

	@Getter
	@Setter
	private String projectile = "";

	public FireProjectile(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		Player p = ei.getPlayer();
		try {
			Class<? extends Projectile> projToSpawn = Class.forName("org.bukkit.entity." + getProjectile())
					.asSubclass(Projectile.class);
			
			Projectile proj = p.launchProjectile(projToSpawn,
					p.getLocation().getDirection().normalize().multiply(ei.getRange()));
			
			// Add the projectile fired to the players projectile list so that it can be identified if it hurts or kills an entity
			MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).getProjectileMapping().put(proj.getUniqueId(),
					ei.getDamage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}