package com.terturl.MMO.Effects.PlayerEffects;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import com.google.gson.JsonObject;
import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class ProjectileEffect extends Effect {

	@Getter 
	private double damage;
	
	@Getter
	private double speed;
	
	@Getter
	private String projectile = "";

	public void run(Player p) {
		try {
			Class<? extends Projectile> projToSpawn = Class.forName("org.bukkit.entity." + getProjectile())
					.asSubclass(Projectile.class);
			
			Projectile proj = p.launchProjectile(projToSpawn,
					p.getLocation().getDirection().normalize().multiply(getSpeed()));
			
			MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).getProjectileMapping().put(proj.getUniqueId(),
					getDamage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void load(JsonObject jo) {
		projectile = jo.get("Projectile").getAsString();
		speed = jo.get("Speed").getAsDouble();
		damage = jo.get("Damage").getAsDouble();
	}
	
}